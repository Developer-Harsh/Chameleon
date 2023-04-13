package com.sneproj.chameleon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sneproj.chameleon.databinding.ActivityEditProfileBinding;
import com.sneproj.chameleon.model.User;
import com.sneproj.chameleon.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding binding;
    private static int SELECT_PICTURE = 1;
    private String name, username, bio, downloadurl, image;
    Bitmap bitmap = null;
    LoadingDialog loadingDialog = new LoadingDialog();
    private DatabaseReference reference, dbref;
    private FirebaseDatabase database;
    private StorageReference storagereference;
    private String uniquekey;
    FirebaseAuth auth;
    User usermodal;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(EditProfileActivity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(EditProfileActivity.this.getColor(R.color.bg_main));
        }

        loadingDialog.showdialog(EditProfileActivity.this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        storagereference = FirebaseStorage.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference().child(Constants.COLLECTION_USERS).child(user.getUid());
        readData();

        binding.editimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckValidation();
            }
        });
        binding.AboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
startActivity(new Intent(EditProfileActivity.this, AboutMeActivity.class));
            }
        });



    }
    private void readData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    usermodal = snapshot.getValue(User.class);
                    binding.editname.setText(usermodal.name);
                    binding.editusername.setText(usermodal.uname);
                    binding.editbio.setText(usermodal.bio);
                    binding.usergmail.setText(usermodal.email);

                    image  = usermodal.profile;
                    Glide.with(EditProfileActivity.this).load(image).into(binding.editimage);
                    loadingDialog.dismissdialog();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Not available", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void CheckValidation() {
        name = binding.editname.getText().toString();
        username = binding.editusername.getText().toString();
        bio = binding.editbio.getText().toString();
        if (name.isEmpty()) {
            binding.editname.setError("Required name");
        }
        if (username.isEmpty()) {
            binding.editusername.setError("Required username");
        }
        else if (bitmap == null) {
            UpdateData(image);
        } else {
            UploadData();
        }
    }

    private void UploadData() {
        loadingDialog.showdialog(EditProfileActivity.this);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();

        final StorageReference filepath;

        filepath = storagereference.child("Users").child(finalimg + "jpg");
        final UploadTask uploadTask = filepath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(EditProfileActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadurl = String.valueOf(uri);
                                    UpdateData(downloadurl);

                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(EditProfileActivity.this, "Not upload", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void OpenGallery() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    private void UpdateData(String downloadurl) {



        HashMap hp = new HashMap();
        hp.put("name", name);
        hp.put("uname", username);
        hp.put("bio", bio);
        hp.put("profile", downloadurl);


      reference.updateChildren(hp)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {

                        loadingDialog.dismissdialog();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    binding.editimage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
