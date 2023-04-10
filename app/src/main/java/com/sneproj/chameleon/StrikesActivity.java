package com.sneproj.chameleon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sneproj.chameleon.databinding.ActivityStrikesBinding;

public class StrikesActivity extends AppCompatActivity {
    ActivityStrikesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStrikesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}