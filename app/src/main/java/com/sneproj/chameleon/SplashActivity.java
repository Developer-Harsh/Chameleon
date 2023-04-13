package com.sneproj.chameleon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.sneproj.chameleon.databinding.ActivitySplashBinding;

import java.util.MissingFormatArgumentException;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(SplashActivity.this.getColor(R.color.white));
            getWindow().setNavigationBarColor(SplashActivity.this.getColor(R.color.white));
        }


        Thread thread = new Thread() {

            public void run() {
                try {
                    sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();

                } finally {

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);

                    startActivity(intent);
                    finish();

                }
            }

        };
        thread.start();
    }

}
