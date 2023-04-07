package com.sneproj.chameleon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;

import com.google.firebase.auth.FirebaseAuth;
import com.sneproj.chameleon.databinding.ActivityOnboardBinding;

public class OnboardActivity extends AppCompatActivity {
    ActivityOnboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(OnboardActivity.this.getColor(R.color.white));
            getWindow().setNavigationBarColor(OnboardActivity.this.getColor(R.color.white));
        }

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(OnboardActivity.this, NewUserActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        binding.videoview.setVideoURI(Uri.parse("android.resource://"+ getPackageName() + "/" + R.raw.video));
        binding.videoview.requestFocus();
        binding.videoview.start();
        binding.videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.setLooping(true);

            }
        });

        binding.registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnboardActivity.this, RegisterActivity.class));
            }
        });
        binding.Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnboardActivity.this, LoginActivity.class));
            }
        });
    }
}