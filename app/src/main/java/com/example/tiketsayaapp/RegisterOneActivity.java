package com.example.tiketsayaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterOneActivity extends AppCompatActivity {
    LinearLayout btn_back;
    Button btn_continue;
    EditText username, password, email;
    DatabaseReference reference, reference_username;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);
        btn_back = findViewById(R.id.btn_back);
        btn_continue = findViewById(R.id.btn_continue);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference_username = FirebaseDatabase.getInstance().getReference().child("Users").child(username.getText().toString());
                reference_username.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            Toast.makeText(getApplicationContext(), "Username sudah tersedia!", Toast.LENGTH_SHORT).show();
                        }else{
                            if (username.getText().toString().isEmpty() || email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(), "Mohon isi semua form", Toast.LENGTH_SHORT).show();
                            }else {
                                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(username_key, username.getText().toString());
                                editor.apply();
                                //simpan ke firebase
                                reference = FirebaseDatabase.getInstance().getReference()
                                        .child("Users").child(username.getText().toString());
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        dataSnapshot.getRef().child("username").setValue(username.getText().toString());
                                        dataSnapshot.getRef().child("password").setValue(password.getText().toString());
                                        dataSnapshot.getRef().child("email").setValue(email.getText().toString());
                                        dataSnapshot.getRef().child("user_balance").setValue(600);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                //pindah activity
                                Intent toRegisterTwo = new Intent(RegisterOneActivity.this, RegisterTwoActivity.class);
                                startActivity(toRegisterTwo);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
