package com.sneproj.chameleon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.sneproj.chameleon.databinding.ActivityQuizResultBinding;

public class QuizResultActivity extends AppCompatActivity {
    ActivityQuizResultBinding binding;
    int correct, total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(QuizResultActivity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(QuizResultActivity.this.getColor(R.color.bg_main));
        }
        correct = getIntent().getIntExtra("correct",0);
        total = getIntent().getIntExtra("total", 0);


    }
}