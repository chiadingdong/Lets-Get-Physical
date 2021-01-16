package com.sp.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Home extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    CardView exerciseCV, diaryCV, locationCV, planWorkoutCV, accountCv, logoutCv;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    FirebaseAuth mAuth;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mAuth= FirebaseAuth.getInstance();
        context = this;

        exerciseCV = findViewById(R.id.exerciseCV);
        diaryCV = findViewById(R.id.diaryCV);
        locationCV = findViewById(R.id.locationCV);
        planWorkoutCV = findViewById(R.id.planWorkoutCV);
        accountCv = findViewById(R.id.accountCV);
        logoutCv = findViewById(R.id.logoutCV);


        exerciseCV.setOnClickListener(this);
        diaryCV.setOnClickListener(this);
        locationCV.setOnClickListener(this);
        planWorkoutCV.setOnClickListener(this);
        accountCv.setOnClickListener(this);
        logoutCv.setOnClickListener(this);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        navigationView.setNavigationItemSelectedListener(this);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setCheckedItem(R.id.nav_home);
    }

    //Cardview
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exerciseCV:
                startActivity(new Intent(this, ExcDisplay.class));
                break;
            case R.id.diaryCV:
                startActivity(new Intent(this, DisplayNote.class));
                break;
            case R.id.locationCV:
                startActivity(new Intent(this, LocationDisplay.class));
                break;
            case R.id.planWorkoutCV:
                openCalendar();
                break;
            case R.id.accountCV:
                startActivity(new Intent(this, UserAccount.class));
                break;
            case R.id.logoutCV:
                showLogoutDialog();
                break;
        }
    }


    //Nav drawer
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_exercise:
                startActivity(new Intent(this, ExcDisplay.class));
                break;
            case R.id.nav_note:
                startActivity(new Intent(this, DisplayNote.class));
                break;
            case R.id.nav_location:
                startActivity(new Intent(this, LocationDisplay.class));
                break;
            case R.id.nav_planWorkOut:
                openCalendar();
                break;
            case R.id.nav_account:
                startActivity(new Intent(this, UserAccount.class));
                break;
            case R.id.nav_logout:
                showLogoutDialog();
                break;

        }
        //navigationView.setCheckedItem(R.id.nav_home);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void openCalendar() {
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", true);
        intent.putExtra("rrule", "FREQ=WEEKLY;WKST=SU;BYDAY=TU,TH");
        intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
        intent.putExtra("title", "Workout using Lets Get Physical App!");
        startActivity(intent);
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to logout?").setPositiveButton("Yes.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mAuth.signOut();
                ((Activity) context).finish();
                Intent loginActivity = new Intent(Home.this, Login.class);
                startActivity(loginActivity);
            }
        }).setNegativeButton("No!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}


//menu stuff
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.userAccount:
                Intent userAccountIntent = new Intent(Home.this, UserAccount.class);
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
    */

//Display welcome message with user's name
        /*
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
        }); */