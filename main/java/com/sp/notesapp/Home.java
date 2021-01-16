package com.sp.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Home extends AppCompatActivity implements View.OnClickListener{
    private TextView welcomeMsg;
    private Button excBtn, locationBtn, noteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        welcomeMsg = findViewById(R.id.welcomeMsg);
        excBtn = findViewById(R.id.excBtn);
        noteBtn = findViewById(R.id.notesBtn);
        locationBtn = findViewById(R.id.locationBtn);


        excBtn.setOnClickListener(this);
        noteBtn.setOnClickListener(this);
        locationBtn.setOnClickListener(this);

        //Display welcome message with user's name
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootReference = firebaseDatabase.getReference(); //app root in firebase database.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference nameReference = rootReference.child("Users").child(currentUser.getUid()).child("name");

        nameReference.addListenerForSingleValueEvent(new ValueEventListener() { //this will get triggered at least once.
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //dataSnapshot will have = {name: "Aman"} key =name, value = Aman.
                welcomeMsg.setText("Welcome, "+dataSnapshot.getValue().toString()+"!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()){
            case R.id.excBtn:
                intent = new Intent(Home.this,ExcDisplay.class);
                startActivity(intent);
                break;
            case R.id.notesBtn:
                intent = new Intent(Home.this,DisplayNote.class);
                startActivity(intent);
                break;
            case R.id.locationBtn:
                intent = new Intent(Home.this,LocationDisplay.class);
                startActivity(intent);
                break;

        }
    }






    //menu stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.userAccount:
                Intent userAccountIntent = new Intent(Home.this,UserAccount.class);
                startActivity(userAccountIntent);
                break;
            case R.id.calendarEvent:
                //Create event in calendar
                Calendar cal = Calendar.getInstance();
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra("beginTime", cal.getTimeInMillis());
                intent.putExtra("allDay", true);
                intent.putExtra("rrule", "FREQ=WEEKLY;WKST=SU;BYDAY=TU,TH");
                intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                intent.putExtra("title", "Workout using Lets Get Physical App!");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}