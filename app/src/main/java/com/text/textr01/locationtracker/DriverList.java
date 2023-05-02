package com.text.textr01.locationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DriverList extends AppCompatActivity {

    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String> arrayAdapter;
    Driver driver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_list);

        driver =new Driver();

        listView=(ListView)findViewById(R.id.listview);
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        list=new ArrayList<>();
        arrayAdapter=new ArrayAdapter<String>(this,R.layout.row,R.id.namelistitem,list);

        RootRef.child("Drivers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    driver=ds.getValue(Driver.class);
                    list.add("Phone : "+driver.getPhone()+"\n"+"EmpID : "+driver.getEmpid()+"\n"+"Address : "+driver.getAddress()+
                            "\n"+"Name : "+driver.getName()+"\n"+"Email : "+driver.getEmail()
                            +"\n"+"Password : "+driver.getPassword()+"\n"+"Status : "+driver.getStatus());
                }
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(DriverList.this, EditDriver.class);
                intent.putExtra("phone",list.get(i).substring(8,18));
                startActivity(intent);
            }
        });


    }
}
