package com.sp.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExcDisplay extends AppCompatActivity {

    //private Button addExc;
    private RecyclerView mRecyclerView;
    private ArrayList<ExcModel> excList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exc_display);

        mRecyclerView = findViewById(R.id.excList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ExcDisplay.this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        readExcFromFirebase();

        //addExc = findViewById(R.id.addExcBtn);
        //addExc.setOnClickListener(add_Exc);
    }

    private void readExcFromFirebase() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference excReference = firebaseDatabase.getReference().child("Exercises");

        excReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                excList.clear();
                ExcModel exercise;

                for(DataSnapshot noteSnapshot : dataSnapshot.getChildren())
                {
                    exercise = noteSnapshot.getValue(ExcModel.class);

                    excList.add(exercise);
                }

                //Create adapter
                ExcAdapter myRecyclerViewAdapter = new ExcAdapter(excList, new ExcAdapter.MyRecyclerViewItemClickListener()
                {
                    //Handling clicks
                    @Override
                    public void onItemClicked(ExcModel exercise)
                    {
                        Intent intent = new Intent(ExcDisplay.this,ExcOnClick.class);
                        intent.putExtra("VIDEOURL",exercise.getVideoUrl());
                        intent.putExtra("DESCRIP",exercise.getDescription());
                        startActivity(intent);
                    }
                });

                //Set adapter to RecyclerView
                mRecyclerView.setAdapter(myRecyclerViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ExcDisplay.this, "Some error occurred.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    /*
    View.OnClickListener add_Exc = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(ExcDisplay.this,CreateExc.class));
        }
    }; */


}