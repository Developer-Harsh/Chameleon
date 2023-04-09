package com.sneproj.chameleon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.sneproj.chameleon.databinding.ActivityNewUserBinding;
import com.sneproj.chameleon.model.User;
import com.sneproj.chameleon.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class NewUserActivity extends AppCompatActivity {
    ActivityNewUserBinding binding;
    private static int SELECT_PICTURE = 1;
    private String name, image, country, lang, downloadurl;
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
        binding = ActivityNewUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(NewUserActivity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(NewUserActivity.this.getColor(R.color.bg_main));
        }

        loadingDialog.showdialog(NewUserActivity.this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        storagereference = FirebaseStorage.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference().child(Constants.COLLECTION_USERS).child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            usermodal = snapshot.getValue(User.class);
                            binding.name.setText(usermodal.name);
                            loadingDialog.dismissdialog();
                        } else {
                            Toast.makeText(NewUserActivity.this, "Not available", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        reference
                .child("image")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            image = snapshot.getValue(String.class);
                            Glide.with(NewUserActivity.this).load(image).into(binding.userimage);
                        } else {
                            Glide.with(NewUserActivity.this).load(R.drawable.logo).into(binding.userimage);
                        }
                        loadingDialog.dismissdialog();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });






        binding.userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });
        binding.Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckValidation();
            }
        });


        String[] languages = getResources().getStringArray(R.array.languages);
        binding.language.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, languages));

        binding.language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lang = binding.language.getSelectedItem()
                        .toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                country = selectedCountry.getName().toString();
            }
        });


    }


    private void CheckValidation() {
        name = binding.name.getText().toString();
        if (name.isEmpty()) {
            binding.name.setError("Required name");
        } else if (country == null) {
            Toast.makeText(this, "Select a country", Toast.LENGTH_SHORT).show();
        } else if (lang.equals("Select Language")) {
            Toast.makeText(this, "Required Language", Toast.LENGTH_SHORT).show();
        } else if (bitmap == null) {
            if (image != null){
                UpdateData(image);
            }else{
                Toast.makeText(this, "Image Required", Toast.LENGTH_SHORT).show();
            }
        } else {
            UploadData();
        }
    }

    private void UploadData() {
        loadingDialog.showdialog(NewUserActivity.this);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();

        final StorageReference filepath;

        filepath = storagereference.child("Users").child(finalimg + "jpg");
        final UploadTask uploadTask = filepath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(NewUserActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                    Toast.makeText(NewUserActivity.this, "Not upload", Toast.LENGTH_SHORT).show();
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
        hp.put("location", country);
        hp.put("nativeLang", lang);
        hp.put("profile", downloadurl);


        FirebaseDatabase.getInstance().getReference().child(Constants.COLLECTION_USERS)
                .child(user.getUid()).updateChildren(hp)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {

                        startActivity(new Intent(NewUserActivity.this, NewUser2Activity.class));
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
                    binding.userimage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}