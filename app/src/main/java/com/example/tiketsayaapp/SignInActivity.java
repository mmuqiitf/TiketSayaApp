package com.example.tiketsayaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
    TextView btn_new_account;
    Button btn_signin;
    EditText username, password;
    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        btn_new_account = findViewById(R.id.btn_new_account);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


        btn_signin = findViewById(R.id.btn_signin);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = username.getText().toString();
                final String pass = password.getText().toString();
                if (name.isEmpty()){
                    btn_signin.setEnabled(true);
                    btn_signin.setText(R.string.sign_in);
                    Toast.makeText(getApplicationContext(), "Username dan Password wajib diisi", Toast.LENGTH_SHORT).show();
                }else{
                    if (pass.isEmpty()){
                        btn_signin.setEnabled(true);
                        btn_signin.setText(R.string.sign_in);
                        Toast.makeText(getApplicationContext(), "Username dan Password wajib diisi", Toast.LENGTH_SHORT).show();
                    }else {
                        btn_signin.setEnabled(false);
                        btn_signin.setText("Loading...");
                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(name);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    //ambil data password dari db
                                    String passFromDB = dataSnapshot.child("password").getValue().toString();
                                    //validasi password
                                    if (pass.equals(passFromDB)) {
                                        Intent goHome = new Intent(SignInActivity.this, HomeActivity.class);
                                        startActivity(goHome);
                                        //pindah username(key) kepada local
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key, username.getText().toString());
                                        editor.apply();
                                    } else {
                                        btn_signin.setEnabled(true);
                                        btn_signin.setText(R.string.sign_in);
                                        Toast.makeText(getApplicationContext(), "Password salah!", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    btn_signin.setEnabled(true);
                                    btn_signin.setText(R.string.sign_in);
                                    Toast.makeText(getApplicationContext(), "Username tidak ada", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goRegisterOne = new Intent(SignInActivity.this, RegisterOneActivity.class);
                startActivity(goRegisterOne);
                finish();

            }
        });
    }
}
