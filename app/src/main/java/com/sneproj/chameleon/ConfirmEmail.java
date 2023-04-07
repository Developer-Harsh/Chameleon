package com.sneproj.chameleon;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sneproj.chameleon.databinding.ActivityConfirmEmailBinding;
import com.sneva.easyprefs.EasyPrefs;


public class ConfirmEmail extends AppCompatActivity {
    ActivityConfirmEmailBinding binding;

    FirebaseAuth fAuth;
    FirebaseUser user;
    LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(ConfirmEmail.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(ConfirmEmail.this.getColor(R.color.bg_main));
        }

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        dialog = new LoadingDialog();

        // Button signIn
        binding.signing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EmailLoginActivity.class));
            }
        });
        binding.resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmailVerificationLink();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user != null) {
            user.reload().addOnSuccessListener(aVoid -> {
                if (user.isEmailVerified()) {
                    EasyPrefs.use().getBoolean("isNew", true);
                    Intent intent = new Intent(ConfirmEmail.this, NewUserActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }).addOnFailureListener(e -> {
            });
        } else {
            fAuth.signOut();
        }
    }

    private void sendEmailVerificationLink() {
        if (user != null && !user.isEmailVerified())
            user.sendEmailVerification()
                    .addOnSuccessListener(aVoid -> {
                        dialog.showdialog(ConfirmEmail.this);
                        Toast.makeText(this, "Email verification email sent to your email address!", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        dialog.showdialog(ConfirmEmail.this);
                        Toast.makeText(this, "We are not able to send verification email, please try again later!", Toast.LENGTH_SHORT).show();
                    });
    }
}