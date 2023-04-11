package com.sneproj.chameleon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.sneproj.chameleon.adapter.ViewPagerAdapter;
import com.sneproj.chameleon.databinding.ActivityTransactionBinding;

public class TransactionActivity extends AppCompatActivity {

    ActivityTransactionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(TransactionActivity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(TransactionActivity.this.getColor(R.color.bg_main));
        }

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        binding.viewpager.setAdapter(pagerAdapter);
        binding.tab.setupWithViewPager(binding.viewpager);
    }
}