package com.sneproj.chameleon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.sneproj.chameleon.databinding.ActivitySubscriptionBinding;
import com.sneproj.chameleon.databinding.ActivityWishlistBinding;

public class WishlistActivity extends AppCompatActivity {

    ActivityWishlistBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWishlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(WishlistActivity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(WishlistActivity.this.getColor(R.color.bg_main));
        }
    }
}