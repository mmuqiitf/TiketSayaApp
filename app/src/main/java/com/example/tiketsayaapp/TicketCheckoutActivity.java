package com.example.tiketsayaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class TicketCheckoutActivity extends AppCompatActivity {
    Button btn_buy_ticket, btn_min, btn_plus;
    TextView text_ticket_total, text_ticket_cost, text_my_balance,nama_wisata,lokasi,ketentuan;
    LinearLayout btn_back;
    ImageView notice;
    Integer value_ticket_total = 1;
    Integer my_balance = 117;
    Integer ticket_cost = 0;
    Integer per_ticket = 0;
    Integer sisa_balance = 0;
    DatabaseReference reference,reference2,reference3,reference4;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    String date_wisata = "";
    String time_wisata = "";
    Integer no_transaksi = new Random().nextInt();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);
        getUsernameLocal();
        //mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

        btn_buy_ticket = findViewById(R.id.btn_buy_ticket);
        btn_min = findViewById(R.id.btn_min);
        btn_plus = findViewById(R.id.btn_plus);
        text_ticket_total = findViewById(R.id.ticket_total);
        text_my_balance = findViewById(R.id.text_my_balance);
        text_ticket_cost = findViewById(R.id.text_ticket_cost);
        notice = findViewById(R.id.notice);

        nama_wisata = findViewById(R.id.nama_wisata);
        lokasi = findViewById(R.id.lokasi);
        ketentuan = findViewById(R.id.ketentuan);

        text_ticket_total.setText(value_ticket_total.toString());
        //setting
        text_my_balance.setText("US$ " + my_balance + "");
        ticket_cost = per_ticket * value_ticket_total;
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //hide btn min
        btn_min.animate().alpha(0).setDuration(300).start();
        btn_min.setEnabled(false);
        notice.setVisibility(View.GONE);
        btn_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_ticket_total -= 1;
                text_ticket_total.setText(value_ticket_total.toString());
                if (value_ticket_total < 2){
                    btn_min.animate().alpha(0).setDuration(300).start();
                    btn_min.setEnabled(false);
                }
                ticket_cost = per_ticket * value_ticket_total;
                text_ticket_cost.setText("US$ " + ticket_cost + "");
                if (ticket_cost < my_balance){
                    btn_buy_ticket.animate().translationY(0).alpha(1).setDuration(350).start();
                    btn_buy_ticket.setEnabled(true);
                    text_my_balance.setTextColor(Color.parseColor("#203DD1"));
                    notice.setVisibility(View.GONE);
                }
            }
        });
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_ticket_total += 1;
                text_ticket_total.setText(value_ticket_total.toString());
                if (value_ticket_total > 1){
                    btn_min.animate().alpha(1).setDuration(300).start();
                    btn_min.setEnabled(true);
                }
                ticket_cost = per_ticket * value_ticket_total;
                text_ticket_cost.setText("US$ " + ticket_cost + "");
                if (ticket_cost > my_balance){
                    btn_buy_ticket.animate().translationY(250).alpha(0).setDuration(350).start();
                    btn_buy_ticket.setEnabled(false);
                    text_my_balance.setTextColor(Color.parseColor("#D1206B"));
                    notice.setVisibility(View.VISIBLE);
                }
            }
        });
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        Log.e("Reference", reference.toString());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                ketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());
                date_wisata = dataSnapshot.child("date_wisata").getValue().toString();
                time_wisata = dataSnapshot.child("time_wisata").getValue().toString();
                per_ticket =  Integer.valueOf(dataSnapshot.child("harga_tiket").getValue().toString());

                ticket_cost = per_ticket * value_ticket_total;
                text_ticket_cost.setText("US$ " + ticket_cost.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                my_balance = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                text_my_balance.setText("US$ " + my_balance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_buy_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //menyimpan data user kepada firebase dan membuat tabel baru "Tickets"
                reference3 = FirebaseDatabase.getInstance().getReference().child("Tickets").child(username_key_new).child(nama_wisata.getText().toString() + no_transaksi);
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reference3.getRef().child("id_ticket").setValue(nama_wisata.getText().toString() + no_transaksi);
                        reference3.getRef().child("nama_wisata").setValue(nama_wisata.getText().toString());
                        reference3.getRef().child("lokasi").setValue(lokasi.getText().toString());
                        reference3.getRef().child("ketentuan").setValue(ketentuan.getText().toString());
                        reference3.getRef().child("jumlah_tiket").setValue(value_ticket_total.toString());
                        reference3.getRef().child("date_wisata").setValue(date_wisata);
                        reference3.getRef().child("time_wisata").setValue(time_wisata);
                        Intent toSuccessBuy = new Intent(TicketCheckoutActivity.this,SuccessBuyTicketActivity.class);
                        startActivity(toSuccessBuy);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                reference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        sisa_balance = my_balance - ticket_cost;
                        reference4.getRef().child("user_balance").setValue(sisa_balance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
    public void getUsernameLocal()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
