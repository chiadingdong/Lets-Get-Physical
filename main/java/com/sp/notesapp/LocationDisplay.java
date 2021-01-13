package com.sp.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class LocationDisplay extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<LocationModel> locationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_display2);

        mRecyclerView = findViewById(R.id.locationsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LocationDisplay.this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

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
        readLocationFromFirebase();
        mRecyclerView.setAdapter(myRecyclerViewAdapter);

    }

    byte[] fill;
    private void readLocationFromFirebase() {
        locationList.add(new LocationModel("Coney Island","Very long nice lane","Punggol",fill,1.4093835482694528, 103.9216029894231));
    }


    public byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}