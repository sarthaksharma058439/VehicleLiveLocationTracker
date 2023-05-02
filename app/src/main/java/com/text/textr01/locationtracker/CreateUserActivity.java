package com.text.textr01.locationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

public class CreateUserActivity extends AppCompatActivity {

    String Username, Password;

    TextView NewUserCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        NewUserCredentials = findViewById(R.id.newusercredentials);





        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Random rnd = new Random();
                int number = rnd.nextInt(999999);

                Username = "USR"+String.valueOf(number);
                Password = String.valueOf(number);

                if(!(dataSnapshot.child("Users").child(Username).exists())) {
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("username", Username);
                    userDataMap.put("password", Password);

                    RootRef.child("Users").child(Username).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            NewUserCredentials.setText("Username : "+Username+"\n"+"Password : "+Password);
                            Toast.makeText(CreateUserActivity.this, "New User Created..", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}