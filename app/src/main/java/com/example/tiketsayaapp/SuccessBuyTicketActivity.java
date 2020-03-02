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

public class SuccessBuyTicketActivity extends AppCompatActivity {
    Button btn_dashboard, btn_view_ticket;
    Animation app_splash, top_to_bottom, bottom_to_top;
    ImageView icon_success_ticket;
    TextView textView, textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_buy_ticket);
        btn_dashboard = findViewById(R.id.btn_dashboard);
        btn_view_ticket = findViewById(R.id.btn_view_ticket);
        icon_success_ticket = findViewById(R.id.icon_success_ticket);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);

        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        top_to_bottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);
        bottom_to_top = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);

        btn_view_ticket.startAnimation(bottom_to_top);
        btn_dashboard.startAnimation(bottom_to_top);
        icon_success_ticket.startAnimation(app_splash);
        textView.startAnimation(top_to_bottom);
        textView2.startAnimation(top_to_bottom);


        btn_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDashboard = new Intent(SuccessBuyTicketActivity.this,HomeActivity.class);
                startActivity(toDashboard);
            }
        });
    }
}
