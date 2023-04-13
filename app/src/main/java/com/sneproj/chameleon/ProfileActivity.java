package com.sneproj.chameleon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.sneproj.chameleon.adapter.ProfileViewPagerAdapter;
import com.sneproj.chameleon.adapter.ViewPagerAdapter;
import com.sneproj.chameleon.databinding.ActivityProfileBinding;
import com.sneproj.chameleon.databinding.ActivityTransactionBinding;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;

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

        binding.profileName.setText(name);
        binding.profileBio.setText(bio);
        binding.profileCountryName.setText(location);
        binding.profileName.setText(name);
        binding.profileUsername.setText(uname);
        Glide.with(ProfileActivity.this).load(profile).into(binding.profileImage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(ProfileActivity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(ProfileActivity.this.getColor(R.color.bg_main));
        }

        ProfileViewPagerAdapter pagerAdapter = new ProfileViewPagerAdapter(getSupportFragmentManager());
        binding.viewpager.setAdapter(pagerAdapter);
        binding.tab.setupWithViewPager(binding.viewpager);
    }
}