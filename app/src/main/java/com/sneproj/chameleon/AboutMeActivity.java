package com.sneproj.chameleon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.sneproj.chameleon.adapter.ChipAdapter;
import com.sneproj.chameleon.adapter.LanguageAdapter;
import com.sneproj.chameleon.databinding.ActivityAboutMeBinding;
import com.sneproj.chameleon.model.User;
import com.sneproj.chameleon.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;

public class AboutMeActivity extends AppCompatActivity {
    ActivityAboutMeBinding binding;
    private static int SELECT_PICTURE = 1;
    private String name, image, country,lang, downloadurl, countrycode, updatecc, updategender;
    Bitmap bitmap = null;
    LoadingDialog loadingDialog = new LoadingDialog();
    private DatabaseReference reference, dbref;
    private FirebaseDatabase database;
    private StorageReference storagereference;
    private String uniquekey;
    FirebaseAuth auth;
    User usermodal;
    int selectedId;
    FirebaseUser user;
    ChipAdapter adapter;
    ArrayList<ChipModal> chiplist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutMeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(AboutMeActivity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(AboutMeActivity.this.getColor(R.color.bg_main));
        }

        loadingDialog.showdialog(AboutMeActivity.this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        storagereference = FirebaseStorage.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference().child(Constants.COLLECTION_USERS).child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    usermodal = snapshot.getValue(User.class);
                    countrycode = usermodal.countrycode;
                    binding.ccp.setDefaultCountryUsingNameCodeAndApply(countrycode);
                    loadingDialog.dismissdialog();



                } else {
                    Toast.makeText(AboutMeActivity.this, "Not available", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("gender").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String gen = snapshot.getValue(String.class);

                    if (gen.contains("Male")){
                        binding.male.setChecked(true);
                    }else{
                        binding.female.setChecked(true);
                    }
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


        String[] languages = getResources().getStringArray(R.array.languages);


        reference.child("nativeLang").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String defaultLanguage = dataSnapshot.getValue(String.class);
                int defaultIndex = -1;
                for (int i = 0; i < languages.length; i++) {
                    if (languages[i].equals(defaultLanguage)) {
                        defaultIndex = i;
                        break;
                    }
                }
                if (defaultIndex >= 0) {
                    binding.language.setSelection(defaultIndex);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });
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
                updatecc = selectedCountry.getIso();
            }
        });

        binding.groupradio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                updategender = radioButton.getText().toString();
            }
        });
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedId = binding.groupradio.getCheckedRadioButtonId();


             RadioButton radioButton = (RadioButton) binding.groupradio.findViewById(selectedId);

                if(radioButton != null) {
                    updategender = radioButton.getText().toString();
                    // Do something with the text
                }else{
                    updategender = null;
                }
                    CheckValidation();
            }
        });

        getChipsData();


    }

    private void getChipsData() {
        chiplist = new ArrayList<>();
        chiplist.add(new ChipModal("Grammar"));
        chiplist.add(new ChipModal("StudyAbroad"));
        chiplist.add(new ChipModal("Reading"));
        chiplist.add(new ChipModal("Listening"));
        chiplist.add(new ChipModal("Writing"));
        chiplist.add(new ChipModal("Speaking"));
        chiplist.add(new ChipModal("Music"));
        chiplist.add(new ChipModal("Movies"));
        chiplist.add(new ChipModal("Games"));
        chiplist.add(new ChipModal("Art"));
        chiplist.add(new ChipModal("Sports"));
        chiplist.add(new ChipModal("Language"));
        chiplist.add(new ChipModal("Conversational Skills"));
        chiplist.add(new ChipModal("Vocabulary"));
        chiplist.add(new ChipModal("Pronunciation"));
        chiplist.add(new ChipModal("Accent"));
        chiplist.add(new ChipModal("American"));
        chiplist.add(new ChipModal("British"));
        chiplist.add(new ChipModal("Engineering"));
        chiplist.add(new ChipModal("Science"));
        chiplist.add(new ChipModal("Coding"));
        chiplist.add(new ChipModal("Photography"));
        chiplist.add(new ChipModal("Painting"));
        chiplist.add(new ChipModal("Love"));
        chiplist.add(new ChipModal("Family"));
        chiplist.add(new ChipModal("Religion"));
        chiplist.add(new ChipModal("Fashion"));
        chiplist.add(new ChipModal("Animals"));
        chiplist.add(new ChipModal("School"));
        chiplist.add(new ChipModal("Work"));
        chiplist.add(new ChipModal("Travel"));
        chiplist.add(new ChipModal("Culture"));
        chiplist.add(new ChipModal("Food"));
        chiplist.add(new ChipModal("Politics"));
        chiplist.add(new ChipModal("History"));
        chiplist.add(new ChipModal("Hiking"));
        chiplist.add(new ChipModal("Running"));
        chiplist.add(new ChipModal("Cooking"));
        chiplist.add(new ChipModal("Dancing"));
        chiplist.add(new ChipModal("Gardening"));
        chiplist.add(new ChipModal("Sleeping"));
        chiplist.add(new ChipModal("Swimming"));
        chiplist.add(new ChipModal("MaterialArts"));


        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        binding.recyclerview.setLayoutManager(layoutManager);
        binding.recyclerview.setHasFixedSize(true);
        adapter = new ChipAdapter( chiplist, AboutMeActivity.this);
        binding.recyclerview.setAdapter(adapter);
    }

    private void CheckValidation() {

        if (country == null) {
            Toast.makeText(this, "Select a country", Toast.LENGTH_SHORT).show();
        } else if (lang.equals("Select Language")) {
            Toast.makeText(this, "Required Language", Toast.LENGTH_SHORT).show();
        }
        else {
            UpdateData();
        }
    }

    private void UpdateData() {
        HashMap hp = new HashMap();
        hp.put("nativeLang", lang);
        hp.put("location", country);
        hp.put("gender",updategender );
        hp.put("countrycode",updatecc );

 reference.updateChildren(hp)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(AboutMeActivity.this, "Update data", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismissdialog();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}