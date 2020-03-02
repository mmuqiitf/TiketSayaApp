package com.example.tiketsayaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {
    LinearLayout btn_back;
    DatabaseReference reference;
    StorageReference storage;
    Uri photo_location;
    Integer photo_max = 1;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    ImageView photo_edit_profile;
    EditText nama_lengkap, bio, email, password, username;
    Button btn_edit_profile, btn_add_photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getUsernameLocal();
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        photo_edit_profile = findViewById(R.id.photo_edit_profile);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        bio = findViewById(R.id.bio);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        btn_edit_profile = findViewById(R.id.btn_edit_profile);
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        storage = FirebaseStorage.getInstance().getReference()
                .child("Photousers").child(username_key_new);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_lengkap.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                bio.setText(dataSnapshot.child("bio").getValue().toString());
                email.setText(dataSnapshot.child("email").getValue().toString());
                password.setText(dataSnapshot.child("password").getValue().toString());
                username.setText(dataSnapshot.child("username").getValue().toString());
                Picasso.with(EditProfileActivity.this).load(dataSnapshot.child("uri_photo_profile").getValue().toString())
                        .centerCrop().fit().into(photo_edit_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_add_photo = findViewById(R.id.btn_add_photo);
        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });
        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nama_lengkap.getText().toString().isEmpty() || bio.getText().toString().isEmpty() ||
                        email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || username.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Form tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }else{
                btn_edit_profile.setText("Loading...");
                btn_edit_profile.setEnabled(false);
                //validasi untuk file
                if (photo_location != null) {
                    final StorageReference storageReference1 = storage.child(System.currentTimeMillis() + "." + getFileExtension(photo_location));
                    UploadTask uploadTask = storageReference1.putFile(photo_location);
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return storageReference1.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                String downloadURL = downloadUri.toString();
                                reference.getRef().child("uri_photo_profile").setValue(downloadURL);
                                reference.getRef().child("nama_lengkap").setValue(nama_lengkap.getText().toString());
                                reference.getRef().child("bio").setValue(bio.getText().toString());
                                reference.getRef().child("username").setValue(username.getText().toString());
                                reference.getRef().child("password").setValue(password.getText().toString());
                                reference.getRef().child("email").setValue(email.getText().toString());
                                Intent toMyProfile = new Intent(EditProfileActivity.this, MyProfileActivity.class);
                                startActivity(toMyProfile);
                            } else {
                                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                }
            }
        });
    }
    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public void findPhoto(){
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null){
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(photo_edit_profile);
        }
    }
    public void getUsernameLocal()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
