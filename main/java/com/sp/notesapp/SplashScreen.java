package com.sp.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(); //auth object;

        //current user object = it is the currently logged in user. If not logged in, this is null.
        final FirebaseUser currentUser= firebaseAuth.getCurrentUser();


        mp = MediaPlayer.create(getBaseContext(), R.raw.music);
        mp.start(); //Starts your sound



        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(currentUser!=null)
                {
                    //user is already logged in.
                    finish();
                    Intent homeIntent = new Intent(SplashScreen.this, Home.class);
                    startActivity(homeIntent);
                }
                else
                 {
                     finish();
                     //user is not logged in, then show login activity.
                    Intent loginIntent = new Intent(SplashScreen.this, Login.class);
                    startActivity(loginIntent);
                }

            }
        },2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
    }


}