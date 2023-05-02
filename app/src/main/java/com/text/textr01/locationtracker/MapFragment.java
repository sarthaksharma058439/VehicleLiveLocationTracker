package com.text.textr01.locationtracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    ArrayList<String> DriverLocationList = new ArrayList<>();
    ArrayList<String> DriverNameList = new ArrayList<>();
    Button GetDriversLocation;

    GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view=inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize map fragment
        final SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        GetDriversLocation = view.findViewById(R.id.getDriversLocation);

        GetDriversLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();

                DriverLocationList.clear();

                RootRef.child("Drivers").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren())
                        {
                            DriverLocationList.add(snapshot.child("locationname").getValue().toString());
                            DriverNameList.add(snapshot.child("name").getValue().toString());

                        }
                        //Toast.makeText(supportMapFragment.getContext(), DriverLocationList.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=0; i<DriverLocationList.size(); i++)
                        {
                            double latD = Double.parseDouble(DriverLocationList.get(i).split(" ")[0]);
                            double longtD = Double.parseDouble(DriverLocationList.get(i).split(" ")[1]);

                            MarkerOptions marker = new MarkerOptions().position(new LatLng(latD, longtD)).title("Bus Driver "+DriverNameList.get(i));
                            map.addMarker(marker);
                        }

                        LatLng India = new LatLng(Double.parseDouble(DriverLocationList.get(0).split(" ")[0]),
                                Double.parseDouble(DriverLocationList.get(0).split(" ")[1]));

                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(India, 15),3000, null);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                map = googleMap;
            }
        });
        return view;
    }
}