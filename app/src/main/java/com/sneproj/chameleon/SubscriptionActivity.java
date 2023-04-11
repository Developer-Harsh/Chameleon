package com.sneproj.chameleon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.sneproj.chameleon.databinding.ActivitySubscriptionBinding;

public class SubscriptionActivity extends AppCompatActivity {
    ActivitySubscriptionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(SubscriptionActivity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(SubscriptionActivity.this.getColor(R.color.bg_main));
        }

        binding.subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
startActivity(new Intent(SubscriptionActivity.this, SubscribeActivity.class));
            }
        });
    }
}