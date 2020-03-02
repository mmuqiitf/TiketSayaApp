package com.example.tiketsayaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class TicketDetailActivity extends AppCompatActivity {
    Button btn_buy_ticket;
    DatabaseReference reference;
    TextView title_ticket,location_ticket,photo_spot_ticket,wifi_ticket,festival_ticket,desc_ticket;
    ImageView header_ticket;
    LinearLayout btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);
        //mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");
        Toast.makeText(getApplicationContext(), "Jenis Tiket " + jenis_tiket_baru, Toast.LENGTH_SHORT).show();
        title_ticket = findViewById(R.id.title_ticket);
        location_ticket = findViewById(R.id.location_ticket);
        photo_spot_ticket = findViewById(R.id.photo_spot_ticket);
        wifi_ticket = findViewById(R.id.wifi_ticket);
        festival_ticket = findViewById(R.id.festival_ticket);
        desc_ticket = findViewById(R.id.desc_ticket);
        header_ticket = findViewById(R.id.header_ticket);
        //mengambil data dari firebase berdasarkan intent
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                title_ticket.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                location_ticket.setText(dataSnapshot.child("lokasi").getValue().toString());
                photo_spot_ticket.setText(dataSnapshot.child("is_photo_spot").getValue().toString());
                wifi_ticket.setText(dataSnapshot.child("is_wifi").getValue().toString());
                festival_ticket.setText(dataSnapshot.child("is_festival").getValue().toString());
                desc_ticket.setText(dataSnapshot.child("short_desc").getValue().toString());
                Picasso.with(TicketDetailActivity.this).load(dataSnapshot.child("url_thumbnail").getValue().toString()).centerCrop().fit().into(header_ticket);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_buy_ticket = findViewById(R.id.btn_buy_ticket);
        btn_buy_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCheckout = new Intent(TicketDetailActivity.this, TicketCheckoutActivity.class);
                toCheckout.putExtra("jenis_tiket", jenis_tiket_baru);
                startActivity(toCheckout);
            }
        });

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHome = new Intent(TicketDetailActivity.this, HomeActivity.class);
                startActivity(goHome);
            }
        });
    }
}
