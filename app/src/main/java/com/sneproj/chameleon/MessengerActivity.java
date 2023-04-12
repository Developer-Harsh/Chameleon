package com.sneproj.chameleon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

public class MessengerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(MessengerActivity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(MessengerActivity.this.getColor(R.color.bg_main));
        }

    }
}