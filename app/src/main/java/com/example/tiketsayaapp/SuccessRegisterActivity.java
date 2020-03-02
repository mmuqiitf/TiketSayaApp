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

public class SuccessRegisterActivity extends AppCompatActivity {
    Button btn_explore;
    ImageView icon_regis_success;
    TextView title_regis_success, desc_regis_success;
    Animation top_to_bottom, bottom_to_top, app_splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_register);
        btn_explore = findViewById(R.id.btn_explore);
        icon_regis_success = findViewById(R.id.icon_regis_success);
        title_regis_success = findViewById(R.id.title_regis_success);
        desc_regis_success = findViewById(R.id.desc_regis_success);

        bottom_to_top = AnimationUtils.loadAnimation(this,R.anim.bottom_to_top);
        top_to_bottom = AnimationUtils.loadAnimation(this,R.anim.top_to_bottom);
        app_splash = AnimationUtils.loadAnimation(this,R.anim.app_splash);

        btn_explore.setAnimation(bottom_to_top);
        icon_regis_success.setAnimation(app_splash);
        title_regis_success.setAnimation(top_to_bottom);
        desc_regis_success.setAnimation(top_to_bottom);


        btn_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toHome = new Intent(SuccessRegisterActivity.this, HomeActivity.class);
                startActivity(toHome);
            }
        });
    }
}
