package com.text.textr01.locationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText Username,Password;
    Button LoginButton;
    String UsernameText,PasswordText;
    String adminusername,adminpassword;

    String Etphone,Etpass,Status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Username=(EditText)findViewById(R.id.username);
        Password=(EditText)findViewById(R.id.password);
        LoginButton=(Button)findViewById(R.id.loginbutton);



        SharedPreferences sh
                = getSharedPreferences("MySharedPref",
                MODE_PRIVATE);
        String s1 = sh.getString("active", "");
        String s2 = sh.getString("phone", "");

        if(s1.equals("admin"))
        {
            Intent i=new Intent(MainActivity.this,HomeActicity.class);
            startActivity(i);
        }
        if(s1.equals("driver"))
        {
            Intent i=new Intent(MainActivity.this, DriverActivty.class);
            i.putExtra("phone",s2);
            startActivity(i);
        }

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                adminusername=dataSnapshot.child("Admin").child("username").getValue().toString();
                adminpassword=dataSnapshot.child("Admin").child("password").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UsernameText=Username.getText().toString();
                PasswordText=Password.getText().toString();



                if(UsernameText.equals(adminusername))
                {

                    if(PasswordText.equals(adminpassword))
                    {
                        Intent i=new Intent(MainActivity.this,HomeActicity.class);
                        startActivity(i);

                        SharedPreferences sharedPreferences
                                = getSharedPreferences("MySharedPref",
                                MODE_PRIVATE);

                        SharedPreferences.Editor myEdit
                                = sharedPreferences.edit();

                        myEdit.putString(
                                "active",
                                "admin");
                        myEdit.commit();

                        Toast.makeText(MainActivity.this, "Welcome admin..", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Please enter correct password..", Toast.LENGTH_SHORT).show();
                    }

                }
                else if(UsernameText.startsWith("USR"))
                {
                    final DatabaseReference RootRef2;
                    RootRef2= FirebaseDatabase.getInstance().getReference().child("Users");

                    RootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.child(UsernameText).exists())
                            {
                                String CurrentUsername=dataSnapshot.child(UsernameText).child("username").getValue().toString();
                                String CurrentPassword=dataSnapshot.child(UsernameText).child("password").getValue().toString();

                                if(CurrentUsername.equals(UsernameText))
                                {
                                    if (CurrentPassword.equals(PasswordText))
                                    {
                                        Intent i = new Intent(MainActivity.this, UserActivity.class);
                                        startActivity(i);
                                    }
                                    else
                                    {
                                        Toast.makeText(MainActivity.this, "Please check your password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "user with this username does not exist..", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    if (UsernameText.matches("[0-9]+") && UsernameText.length() == 10)
                    {
                        final DatabaseReference RootRef2;
                        RootRef2= FirebaseDatabase.getInstance().getReference().child("Drivers");

                        RootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.child(UsernameText).exists())
                                {
                                    Etphone=dataSnapshot.child(UsernameText).child("phone").getValue().toString();
                                    Etpass=dataSnapshot.child(UsernameText).child("password").getValue().toString();
                                    Status=dataSnapshot.child(UsernameText).child("status").getValue().toString();

                                    Login(Etphone,Etpass,Status);
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "driver with this phone number not exist..", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Please enter correct phone number..", Toast.LENGTH_SHORT).show();
                    }




                }

            }
        });
    }

    private void Login(String etphone, String etpass,String status) {
        if(UsernameText.equals(etphone))
        {
            if(PasswordText.equals(etpass))
            {
                if(status.equals("Active"))
                {
                    SharedPreferences sharedPreferences
                            = getSharedPreferences("MySharedPref",
                            MODE_PRIVATE);

                    SharedPreferences.Editor myEdit
                            = sharedPreferences.edit();

                    myEdit.putString(
                            "active",
                            "driver");
                    myEdit.putString(
                            "phone",
                            etphone);
                    myEdit.commit();

                    Intent i=new Intent(MainActivity.this, DriverActivty.class);
                    i.putExtra("phone",etphone);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(this, "Your Account is Inactive, please contact admin for activation", Toast.LENGTH_SHORT).show();
                }

            }
            else

            {
                Toast.makeText(MainActivity.this, "Please enter correct password..", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onBackPressed() {

    }
}
