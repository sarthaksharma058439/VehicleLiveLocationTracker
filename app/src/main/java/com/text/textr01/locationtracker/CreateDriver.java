package com.text.textr01.locationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CreateDriver extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText Name,EmpId,Address,PhoneNumber,EmailId,Password;
    Button CreateDriverButton;
    String Nametxt,EmpIdtxt,Addresstxt,PhoneNumbertxt,EmailIdtxt,Passwordtxt,Statustxt;
    ProgressDialog loadingBar;
    Spinner statusSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_driver);

        loadingBar=new ProgressDialog(this);
        statusSpinner=findViewById(R.id.statusspinner);
        statusSpinner.setOnItemSelectedListener(this);

        Name=(EditText)findViewById(R.id.name);
        EmpId=(EditText)findViewById(R.id.empid);
        Address=(EditText)findViewById(R.id.address);
        PhoneNumber=(EditText)findViewById(R.id.phonenumber);
        EmailId=(EditText)findViewById(R.id.emailid);
        Password=(EditText)findViewById(R.id.createdriverpassword);

        CreateDriverButton=(Button)findViewById(R.id.createdriverbtn);

        CreateDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Nametxt=Name.getText().toString();
                EmpIdtxt=EmpId.getText().toString();
                Addresstxt=Address.getText().toString();
                PhoneNumbertxt=PhoneNumber.getText().toString();
                EmailIdtxt=EmailId.getText().toString();
                Passwordtxt=Password.getText().toString();

                if(TextUtils.isEmpty(Nametxt))
                {
                    Toast.makeText(CreateDriver.this, "Please enter name...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(EmpIdtxt))
                {
                    Toast.makeText(CreateDriver.this, "Please enter empid...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(Addresstxt))
                {
                    Toast.makeText(CreateDriver.this, "Please enter address...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(PhoneNumbertxt))
                {
                    Toast.makeText(CreateDriver.this, "Please enter phone number...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(EmailIdtxt))
                {
                    Toast.makeText(CreateDriver.this, "Please enter emailid...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(Passwordtxt))
                {
                    Toast.makeText(CreateDriver.this, "Please enter password...", Toast.LENGTH_SHORT).show();
                }
                else if(PhoneNumbertxt.length()<10 || PhoneNumbertxt.length()>10)
                {
                    Toast.makeText(CreateDriver.this, "Please enter correct phone number", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    loadingBar.setTitle("Create Account");
                    loadingBar.setMessage("Please wait, while we are checking credentials");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    ValidatePhoneNumber(Nametxt,EmpIdtxt,Addresstxt,PhoneNumbertxt,EmailIdtxt,Passwordtxt);
                }

            }
        });
    }

    private void ValidatePhoneNumber(final String nametxt, final String empIdtxt, final String addresstxt, final String phoneNumbertxt, final String emailIdtxt, final String passwordtxt)
    {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(!(dataSnapshot.child("Drivers").child(phoneNumbertxt).exists()))
                {
                    HashMap<String,Object> driverDataMap=new HashMap<>();
                    driverDataMap.put("name",nametxt);
                    driverDataMap.put("empid",empIdtxt);
                    driverDataMap.put("address",addresstxt);
                    driverDataMap.put("phone",phoneNumbertxt);
                    driverDataMap.put("email",emailIdtxt);
                    driverDataMap.put("password",passwordtxt);
                    driverDataMap.put("status",Statustxt);
                    driverDataMap.put("mode","off");

                    RootRef.child("Drivers").child(phoneNumbertxt).updateChildren(driverDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(CreateDriver.this, "Successfully created", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent i =new Intent(CreateDriver.this,HomeActicity.class);
                                        startActivity(i);
                                    }
                                    else
                                    {
                                        Toast.makeText(CreateDriver.this, "Network Error..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }
                else
                {
                    Toast.makeText(CreateDriver.this, "Driver with this number is already exist please try with another number..", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Intent i =new Intent(CreateDriver.this,HomeActicity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Statustxt=adapterView.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
