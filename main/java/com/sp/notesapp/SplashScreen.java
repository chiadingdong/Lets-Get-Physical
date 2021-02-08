package com.sp.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class SplashScreen extends AppCompatActivity {
    MediaPlayer mp;
    Animation topAnim, btmAnim;
    ImageView img;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(); //auth object;
        //current user object = it is the currently logged in user. If not logged in, this is null.
        final FirebaseUser currentUser= firebaseAuth.getCurrentUser();

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        btmAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        img= findViewById(R.id.splashScreenImage);
        text= findViewById(R.id.splashScreenText);

        text.setAnimation(topAnim);
        img.setAnimation(btmAnim);

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
        },3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
    }


}