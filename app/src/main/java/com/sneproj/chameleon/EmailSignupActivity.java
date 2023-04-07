package com.sneproj.chameleon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sneproj.chameleon.databinding.ActivityEmailSignupBinding;
import com.sneproj.chameleon.model.User;
import com.sneproj.chameleon.utils.Constants;

public class EmailSignupActivity extends AppCompatActivity {

    ActivityEmailSignupBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    LoadingDialog dialog = new LoadingDialog();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(EmailSignupActivity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(EmailSignupActivity.this.getColor(R.color.bg_main));
        }



        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();



        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

         if (binding.emailBox.getText().length() ==0){
                    binding.emailBox.setError("Required Email");
                }else if (binding.passwordBox.getText().length() ==0){
                    binding.passwordBox.setError("Required Password");
                }else {
                    String email, pass, name, referCode;

                    email = binding.emailBox.getText().toString();
                    pass = binding.passwordBox.getText().toString();

                    dialog.showdialog(EmailSignupActivity.this);
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser fuser = auth.getCurrentUser();
                                if (fuser != null) {
                                    fuser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(EmailSignupActivity.this, "Verification email sent", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(EmailSignupActivity.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                User updateUser = new User();
                                updateUser.name = task.getResult().getUser().getDisplayName();
                                updateUser.uname = "null";
                                updateUser.bio = "";
                                updateUser.email = task.getResult().getUser().getEmail();
                                updateUser.gender = "NA";
                                updateUser.uid = task.getResult().getUser().getUid();
                                updateUser.profile = String.valueOf(task.getResult().getUser().getPhotoUrl());
                                updateUser.number = task.getResult().getUser().getPhoneNumber();
                                updateUser.nativeLang = "";
                                updateUser.location = "";

//                                    String uid = task.getResult().getUser().getp;

                                database.getReference()
                                        .child(Constants.COLLECTION_USERS)
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    dialog.dismissdialog();
                                                    Toast.makeText(EmailSignupActivity.this, "This Account Already exist", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    database.getReference().child(Constants.COLLECTION_USERS)
                                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                            .setValue(updateUser)
                                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                dialog.dismissdialog();
                                                            }
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                startActivity(new Intent(EmailSignupActivity.this, ConfirmEmail.class));
                            }
                        }

                    });



        }
         }

    });
        }


}
