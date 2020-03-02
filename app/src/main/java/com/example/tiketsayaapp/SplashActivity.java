package com.example.tiketsayaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    Animation app_splash, bottom_to_top;
    ImageView app_logo;
    TextView app_tagline;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        app_logo = findViewById(R.id.app_logo);
        app_tagline = findViewById(R.id.app_tagline);
        //load animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        bottom_to_top = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);

        app_logo.startAnimation(app_splash);
        app_tagline.startAnimation(bottom_to_top);
        getUsernameLocal();
    }
    public void getUsernameLocal()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
        if (username_key_new.isEmpty()){
            // Setting timer 2s
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //merubah activity lain
                    Intent goGetStarted = new Intent(SplashActivity.this, GetStartedActivity.class);
                    startActivity(goGetStarted);
                    finish();
                }
            }, 2000);
        }else{
            // Setting timer 2s
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //merubah activity lain
                    Intent goHome = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(goHome);
                    finish();
                }
            }, 2000);
        }
    }
}
