package com.sneproj.chameleon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sneproj.chameleon.databinding.ActivityEmailLoginBinding;
import com.sneproj.chameleon.model.User;
import com.sneproj.chameleon.utils.Constants;
import com.sneva.easyprefs.EasyPrefs;

public class EmailLoginActivity extends AppCompatActivity {

    ActivityEmailLoginBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String userId;
    LoadingDialog dialog = new LoadingDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(EmailLoginActivity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(EmailLoginActivity.this.getColor(R.color.bg_main));
        }


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        binding.forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
startActivity(new Intent(EmailLoginActivity.this, ForgetPassActivitty.class));
            }
        });
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.emailBox.getText().length() == 0) {
                    binding.emailBox.setError("Required Email");
                } else if (binding.passwordBox.getText().length() == 0) {
                    binding.passwordBox.setError("Required Password");
                } else {
                    String email, pass;

                    email = binding.emailBox.getText().toString();
                    pass = binding.passwordBox.getText().toString();

                    dialog.showdialog(EmailLoginActivity.this);
                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                userId = auth.getCurrentUser().getUid();
                                FirebaseUser user = auth.getCurrentUser();
                                if (!user.isEmailVerified()) {
                                    // if user email is not verified - goto com.myapplication.ConfirmEmail
                                    startActivity(new Intent(getApplicationContext(), ConfirmEmail.class));

                                } else {
                                    dialog.dismissdialog();
                                    EasyPrefs.use().getBoolean("isNew", true);
                                    Intent intent = new Intent(EmailLoginActivity.this, NewUserActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }

                            } else {
                                dialog.dismissdialog();
                                Toast.makeText(EmailLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }

                    });


                }
            }

        });
    }


}
