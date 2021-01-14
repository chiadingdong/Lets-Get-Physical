package com.sp.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class LocationDisplay extends AppCompatActivity {

    //private Button addLocation;
    private RecyclerView mRecyclerView;
    private ArrayList<LocationModel> locationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_display2);

        mRecyclerView = findViewById(R.id.locationsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LocationDisplay.this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        readLocationFromFirebase();

        //addLocation = findViewById(R.id.addLocation);
        //addLocation.setOnClickListener(add_Location);

    }

    private void readLocationFromFirebase() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference locationReference = firebaseDatabase.getReference().child("Location");

        locationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                locationList.clear();
                LocationModel location;

                for(DataSnapshot noteSnapshot : dataSnapshot.getChildren())
                {
                    location = noteSnapshot.getValue(LocationModel.class);

                    locationList.add(location);
                }

                //Create adapter
                LocationAdapter myRecyclerViewAdapter = new LocationAdapter(locationList, new LocationAdapter.MyRecyclerViewItemClickListener()
                {
                    //Handling clicks
                    @Override
                    public void onItemClicked(LocationModel location)
                    {
                        Intent intent = new Intent(LocationDisplay.this,LocationMaps.class);
                        intent.putExtra("LATITUDE",location.getLat());
                        intent.putExtra("LONGITUDE",location.getLon());
                        intent.putExtra("NAME",location.getTitle());
                        startActivity(intent);
                    }
                });

                //Set adapter to RecyclerView
                mRecyclerView.setAdapter(myRecyclerViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LocationDisplay.this, "Some error occurred.", Toast.LENGTH_SHORT).show();

            }
        });
    }
/*
    View.OnClickListener add_Location = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(LocationDisplay.this,CreateLocation.class));
        }
    };
    */


}