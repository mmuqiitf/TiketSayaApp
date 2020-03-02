package com.example.tiketsayaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.OnSuccessListener;
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

public class RegisterTwoActivity extends AppCompatActivity {

    Button btn_continue,btn_upload_photo;
    LinearLayout btn_back;
    ImageView user_photo;
    Uri photo_location;
    Integer photo_max = 1;
    EditText bio, nama_lengkap;
    DatabaseReference reference;
    StorageReference storage;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);
        getUsernameLocal();
        btn_back = findViewById(R.id.btn_back);
        btn_continue = findViewById(R.id.btn_continue);
        btn_upload_photo = findViewById(R.id.btn_upload_photo);
        user_photo = findViewById(R.id.user_photo);
        bio = findViewById(R.id.bio);
        nama_lengkap = findViewById(R.id.nama_lengkap);

        btn_upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //menyimpan ke firebase
                reference = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(username_key_new);
                storage = FirebaseStorage.getInstance().getReference()
                        .child("Photousers").child(username_key_new);
                //validasi untuk file
                if (nama_lengkap.getText().toString().isEmpty() || bio.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Mohon isi semua form!", Toast.LENGTH_SHORT).show();
                }else{
                if (photo_location != null){
                    //ubah state menjadi loading
                    btn_continue.setEnabled(false);
                    btn_continue.setText("Loading...");
                    final StorageReference storageReference1 = storage.child(System.currentTimeMillis() + "." + getFileExtension(photo_location));
                    UploadTask uploadTask = storageReference1.putFile(photo_location);
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw task.getException();
                            }
                            return storageReference1.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                Uri downloadUri = task.getResult();
                                String downloadURL = downloadUri.toString();
                                reference.getRef().child("uri_photo_profile").setValue(downloadURL);
                                reference.getRef().child("nama_lengkap").setValue(nama_lengkap.getText().toString());
                                reference.getRef().child("bio").setValue(bio.getText().toString());
                                Intent toSuccessRegister = new Intent(RegisterTwoActivity.this, SuccessRegisterActivity.class);
                                startActivity(toSuccessRegister);
                            }else{
                                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
//                    storageReference1.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            String s = taskSnapshot.getStorage().getDownloadUrl().toString();
//                            reference.getRef().child("uri_photo_profile").setValue(s);
//                            reference.getRef().child("nama_lengkap").setValue(nama_lengkap.getText().toString());
//                            reference.getRef().child("bio").setValue(bio.getText().toString());
//                        }
//                    })
//                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                            Intent toSuccessRegister = new Intent(RegisterTwoActivity.this, SuccessRegisterActivity.class);
//                            startActivity(toSuccessRegister);
//                            finish();
//                        }
//                    });
                }

                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
            Picasso.with(this).load(photo_location).centerCrop().fit().into(user_photo);
        }
    }

    public void getUsernameLocal()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
