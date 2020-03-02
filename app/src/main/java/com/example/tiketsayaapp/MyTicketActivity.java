package com.example.tiketsayaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyTicketActivity extends AppCompatActivity {
    DatabaseReference reference;
    TextView nama_wisata, lokasi, date_wisata, time_wisata, ketentuan;
    LinearLayout btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket);

        Bundle bundle = getIntent().getExtras();
        final String wisata = bundle.getString("nama_wisata");

        nama_wisata = findViewById(R.id.nama_wisata);
        lokasi = findViewById(R.id.lokasi);
        date_wisata = findViewById(R.id.date_wisata);
        time_wisata = findViewById(R.id.time_wisata);
        ketentuan = findViewById(R.id.ketentuan);
        btn_back = findViewById(R.id.btn_back);

        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(wisata);
        Log.e("Reference", reference.toString());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                ketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());
                date_wisata.setText(dataSnapshot.child("date_wisata").getValue().toString());
                time_wisata.setText(dataSnapshot.child("time_wisata").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toHome = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(toHome);
            }
        });
    }
}
