package com.example.tiketsayaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GetStartedActivity extends AppCompatActivity {
    Button btn_signin, btn_new_account;
    Animation top_to_bottom, bottom_to_top;
    ImageView imageView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        btn_signin = findViewById(R.id.btn_signin);
        btn_new_account = findViewById(R.id.btn_new_account);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);

        //load animation
        top_to_bottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);
        bottom_to_top = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);

        // start animation
        textView.startAnimation(top_to_bottom);
        imageView.startAnimation(top_to_bottom);
        btn_signin.startAnimation(bottom_to_top);
        btn_new_account.startAnimation(bottom_to_top);

        btn_signin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent goSignIn = new Intent(GetStartedActivity.this, SignInActivity.class);
                startActivity(goSignIn);
                finish();
            }
        });
        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goRegisterOne = new Intent(GetStartedActivity.this, RegisterOneActivity.class);
                startActivity(goRegisterOne);
                finish();
            }
        });
    }
}
