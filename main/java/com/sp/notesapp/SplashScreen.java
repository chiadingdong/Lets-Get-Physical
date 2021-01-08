package com.sp.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(); //auth object;

        //current user object = it is the currently logged in user. If not logged in, this is null.
        final FirebaseUser currentUser= firebaseAuth.getCurrentUser();


        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(currentUser!=null)
                {
                    //user is already logged in.
                    Intent homeIntent = new Intent(SplashScreen.this, Home.class);
                    startActivity(homeIntent);
                }
                else
                 {
                    //user is not logged in, then show login activity.
                    Intent loginIntent = new Intent(SplashScreen.this, Login.class);
                    startActivity(loginIntent);
                }

            }
        },1500); //1.5 sec

    }
}