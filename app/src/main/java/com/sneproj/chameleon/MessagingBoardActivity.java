package com.sneproj.chameleon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

public class MessagingBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_board);

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(MessagingBoardActivity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(MessagingBoardActivity.this.getColor(R.color.bg_main));
        }

    }
}