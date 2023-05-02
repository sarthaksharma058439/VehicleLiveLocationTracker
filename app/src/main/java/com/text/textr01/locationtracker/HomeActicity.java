package com.text.textr01.locationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HomeActicity extends AppCompatActivity {

    Button CreateDriverbt,CreateUserBtn,DriverListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_acticity);

        CreateDriverbt=(Button)findViewById(R.id.createdriver);
        DriverListButton=(Button)findViewById(R.id.driverlist);
        CreateUserBtn = (Button)findViewById(R.id.createuserbtn);

        CreateDriverbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(HomeActicity.this, CreateDriver.class);
                startActivity(i);
            }
        });
        DriverListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(HomeActicity.this, DriverList.class);
                startActivity(i);
            }
        });
        CreateUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeActicity.this, CreateUserActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.updatepassword:
                Intent i=new Intent(HomeActicity.this,AdminUpdatePassword.class);
                startActivity(i);
                return true;
            case R.id.logout:
                SharedPreferences sharedPreferences
                        = getSharedPreferences("MySharedPref",
                        MODE_PRIVATE);

                SharedPreferences.Editor myEdit
                        = sharedPreferences.edit();

                myEdit.putString(
                        "active",
                        "newadmin");

                myEdit.commit();
                Intent intent=new Intent(HomeActicity.this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

    }
}
