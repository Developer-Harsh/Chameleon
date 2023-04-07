package com.sneproj.chameleon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.sneproj.chameleon.databinding.ActivityNewUserBinding;

public class NewUserActivity extends AppCompatActivity {
    ActivityNewUserBinding binding;

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

    }
}