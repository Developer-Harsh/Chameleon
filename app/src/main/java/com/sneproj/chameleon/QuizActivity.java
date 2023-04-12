package com.sneproj.chameleon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.sneproj.chameleon.databinding.ActivityQuizBinding;

public class QuizActivity extends AppCompatActivity {
    ActivityQuizBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(QuizActivity.this.getColor(R.color.white));
            getWindow().setNavigationBarColor(QuizActivity.this.getColor(R.color.white));
        }
        binding.Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuizActivity.this, MainActivity.class));
            }
        });
        binding.skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
startActivity(new Intent(QuizActivity.this, MainActivity.class));
            }
        });


    }
}