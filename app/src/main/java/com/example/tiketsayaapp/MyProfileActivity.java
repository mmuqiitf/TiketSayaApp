package com.example.tiketsayaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyProfileActivity extends AppCompatActivity {
    Button btn_edit_profile, btn_signout;
    LinearLayout item_my_ticket;
    TextView nama_lengkap, bio;
    ImageView photo_profile;
    DatabaseReference reference, reference2;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    RecyclerView myticket_place;
    ArrayList<MyTicket> list;
    TicketAdapter ticketAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        getUsernameLocal();
        btn_edit_profile = findViewById(R.id.btn_edit_profile);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        bio = findViewById(R.id.bio);
        photo_profile = findViewById(R.id.photo_profile);

        myticket_place = findViewById(R.id.myticket_place);
        myticket_place.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<MyTicket>();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_lengkap.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                bio.setText(dataSnapshot.child("bio").getValue().toString());
                Picasso.with(MyProfileActivity.this).load(dataSnapshot.child("uri_photo_profile").getValue().toString()).centerCrop().fit().into(photo_profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toEditProfile = new Intent(MyProfileActivity.this, EditProfileActivity.class);
                startActivity(toEditProfile);
                finish();
            }
        });
        reference2 = FirebaseDatabase.getInstance().getReference().child("Tickets").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    MyTicket p = dataSnapshot1.getValue(MyTicket.class);
                    list.add(p);
                }
                ticketAdapter = new TicketAdapter(MyProfileActivity.this, list);
                myticket_place.setAdapter(ticketAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_signout = findViewById(R.id.btn_signout);
        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //menghapus username local
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, null);
                editor.apply();

                //berpindah activity
                Intent toSignIn = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(toSignIn);
                finish();
            }
        });
    }
    public void getUsernameLocal()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
