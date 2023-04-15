package com.sneproj.chameleon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sneproj.chameleon.adapter.InterestAdapter;
import com.sneproj.chameleon.databinding.ActivityProfileBinding;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    InterestAdapter adapter;
    ArrayList<ChipModal> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        String name = getIntent().getStringExtra("name");
        String profile = getIntent().getStringExtra("profile");
        String uname = getIntent().getStringExtra("uname");
        String bio = getIntent().getStringExtra("bio");
        String email = getIntent().getStringExtra("email");
        String location = getIntent().getStringExtra("location");
        String uid = getIntent().getStringExtra("uid");
        String lang = getIntent().getStringExtra("lang");

        binding.profileName.setText(name);
        binding.profileBio.setText(bio);
        binding.profileCountryName.setText(location);
        binding.profileName.setText(name);
        binding.profileUsername.setText(uname);
        binding.language.setText(lang);
        Glide.with(ProfileActivity.this).load(profile).into(binding.profileImage);

        list= new ArrayList<>();
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        binding.interestRecyclerview.setLayoutManager(layoutManager);
        binding.interestRecyclerview.setHasFixedSize(true);
        adapter = new InterestAdapter( list, ProfileActivity.this);
        binding.interestRecyclerview.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(ProfileActivity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(ProfileActivity.this.getColor(R.color.bg_main));
        }
        binding.levelTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.image_unselected), PorterDuff.Mode.SRC_ATOP);
        binding.levelItem.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("intrest")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                         ChipModal chipModal =dataSnapshot.getValue(ChipModal.class);
                            list.add(chipModal);
                    }
                    adapter.notifyDataSetChanged(); // notify adapter of data changes

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.levelTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.levelTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.black), PorterDuff.Mode.SRC_ATOP);
                binding.calendarTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.image_unselected), PorterDuff.Mode.SRC_ATOP);
                binding.videoTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.image_unselected), PorterDuff.Mode.SRC_ATOP);

                binding.levelItem.setVisibility(View.VISIBLE);
                binding.videoItem.setVisibility(View.GONE);
                binding.calendarItem.setVisibility(View.GONE);
            }
        });

        binding.calendarTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.levelTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.image_unselected), PorterDuff.Mode.SRC_ATOP);
                binding.calendarTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.black), PorterDuff.Mode.SRC_ATOP);
                binding.videoTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.image_unselected), PorterDuff.Mode.SRC_ATOP);
                binding.levelItem.setVisibility(View.GONE);
                binding.videoItem.setVisibility(View.GONE);
                binding.calendarItem.setVisibility(View.VISIBLE);
            }
        });

        binding.videoTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.levelTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.image_unselected), PorterDuff.Mode.SRC_ATOP);
                binding.calendarTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.image_unselected), PorterDuff.Mode.SRC_ATOP);
                binding.videoTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.black), PorterDuff.Mode.SRC_ATOP);
                binding.levelItem.setVisibility(View.GONE);
                binding.videoItem.setVisibility(View.VISIBLE);
                binding.calendarItem.setVisibility(View.GONE);
            }
        });




    }
}