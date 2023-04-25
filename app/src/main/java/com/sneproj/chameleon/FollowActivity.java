package com.sneproj.chameleon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sneproj.chameleon.databinding.ActivityFollowBinding;

public class FollowActivity extends AppCompatActivity {

    ActivityFollowBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFollowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageView4.setOnClickListener(v -> finish());

        if (getIntent().hasExtra("title"))
            binding.textView10.setText(getIntent().getStringExtra("title"));
    }
}