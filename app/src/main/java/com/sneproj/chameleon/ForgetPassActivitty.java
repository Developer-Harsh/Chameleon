package com.sneproj.chameleon;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.sneproj.chameleon.databinding.ActivityForgetPassActivittyBinding;

public class ForgetPassActivitty extends AppCompatActivity {

    ActivityForgetPassActivittyBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPassActivittyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(ForgetPassActivitty.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(ForgetPassActivitty.this.getColor(R.color.bg_main));
        }

        auth = FirebaseAuth.getInstance();

        binding.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


        String recovery_email = binding.email.getText().toString();


        if (TextUtils.isEmpty(recovery_email)) {
            binding.email.setError("Invalid Email");
            Toast.makeText(ForgetPassActivitty.this, "Invalid Email", Toast.LENGTH_LONG).show();
            return;
        } else {

            auth.sendPasswordResetEmail(recovery_email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    Toast.makeText(ForgetPassActivitty.this, "Reset Password Email Sent!", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ForgetPassActivitty.this, "Password Reset Failure - Invalid Email", Toast.LENGTH_LONG).show();
                }
            });
        }

        binding.signing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgetPassActivitty.this, EmailLoginActivity.class));
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
            }
        });

    }
}