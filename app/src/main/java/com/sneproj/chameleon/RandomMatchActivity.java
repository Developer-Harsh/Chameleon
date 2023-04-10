package com.sneproj.chameleon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sneproj.chameleon.databinding.ActivityRandomMatchBinding;
import com.sneproj.chameleon.model.User;
import com.sneproj.chameleon.utils.Constants;

import java.util.HashMap;
import java.util.Objects;

public class RandomMatchActivity extends AppCompatActivity {

    ActivityRandomMatchBinding binding;
    boolean isOkay = false;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRandomMatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        username = FirebaseAuth.getInstance().getUid();

        FirebaseDatabase.getInstance().getReference()
                .child(Constants.COLLECTION_USERS)
                .child(username)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = new User();
                        user.uid = snapshot.child("uid").getValue(String.class);
                        user.profile = snapshot.child("profile").getValue(String.class);
                        user.name = snapshot.child("name").getValue(String.class);
                        user.uname = snapshot.child("uname").getValue(String.class);
                        user.bio = snapshot.child("bio").getValue(String.class);
                        user.number = snapshot.child("number").getValue(String.class);
                        user.email = snapshot.child("email").getValue(String.class);
                        user.location = snapshot.child("location").getValue(String.class);
                        user.nativeLang = snapshot.child("nativeLang").getValue(String.class);
                        user.gender = snapshot.child("gender").getValue(String.class);
                        user.rank = snapshot.child("rank").getValue(String.class);

                        Glide.with(RandomMatchActivity.this).load(user.profile).into(binding.personMy);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("connection")
                .orderByChild("status")
                .equalTo(0).limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() > 0) {
                            isOkay = true;
                            for (DataSnapshot childSnap : snapshot.getChildren()) {
                                FirebaseDatabase.getInstance().getReference()
                                        .child("connection")
                                        .child(Objects.requireNonNull(childSnap.getKey()))
                                        .child("incoming")
                                        .setValue(username);

                                FirebaseDatabase.getInstance().getReference()
                                        .child("connection")
                                        .child(childSnap.getKey())
                                        .child("status")
                                        .setValue(1);

                                FirebaseDatabase.getInstance().getReference()
                                        .child(Constants.COLLECTION_USERS)
                                        .child(childSnap.child("incoming").getValue(String.class))
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                User user = new User();
                                                user.uid = snapshot.child("uid").getValue(String.class);
                                                user.profile = snapshot.child("profile").getValue(String.class);
                                                user.name = snapshot.child("name").getValue(String.class);
                                                user.uname = snapshot.child("uname").getValue(String.class);
                                                user.bio = snapshot.child("bio").getValue(String.class);
                                                user.number = snapshot.child("number").getValue(String.class);
                                                user.email = snapshot.child("email").getValue(String.class);
                                                user.location = snapshot.child("location").getValue(String.class);
                                                user.nativeLang = snapshot.child("nativeLang").getValue(String.class);
                                                user.gender = snapshot.child("gender").getValue(String.class);
                                                user.rank = snapshot.child("rank").getValue(String.class);

                                                Glide.with(RandomMatchActivity.this).load(user.profile).into(binding.personOther);
                                                binding.textView5.setText("Matched : " + user.name);


                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        callAgoraApi();
                                                        Toast.makeText(RandomMatchActivity.this, "Redirecting you to Call!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }, 10000);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                        } else {
                            HashMap<String, Object> room = new HashMap<>();
                            room.put("incoming", username);
                            room.put("createdBy", username);
                            room.put("isAvailable", true);
                            room.put("status", 0);

                            FirebaseDatabase.getInstance().getReference()
                                    .child("connection")
                                    .child(username)
                                    .setValue(room).addOnSuccessListener(unused -> FirebaseDatabase.getInstance().getReference()
                                            .child("connection")
                                            .child(username).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot12) {
                                                    if (snapshot12.child("status").exists()) {
                                                        if (snapshot12.child("status").getValue(Integer.class) == 1) {
                                                            if (isOkay)
                                                                return;

                                                            isOkay = true;

                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child(Constants.COLLECTION_USERS)
                                                                    .child(snapshot12.child("incoming").getValue(String.class))
                                                                    .addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            User user = new User();
                                                                            user.uid = snapshot.child("uid").getValue(String.class);
                                                                            user.profile = snapshot.child("profile").getValue(String.class);
                                                                            user.name = snapshot.child("name").getValue(String.class);
                                                                            user.uname = snapshot.child("uname").getValue(String.class);
                                                                            user.bio = snapshot.child("bio").getValue(String.class);
                                                                            user.number = snapshot.child("number").getValue(String.class);
                                                                            user.email = snapshot.child("email").getValue(String.class);
                                                                            user.location = snapshot.child("location").getValue(String.class);
                                                                            user.nativeLang = snapshot.child("nativeLang").getValue(String.class);
                                                                            user.gender = snapshot.child("gender").getValue(String.class);
                                                                            user.rank = snapshot.child("rank").getValue(String.class);

                                                                            Glide.with(RandomMatchActivity.this).load(user.profile).into(binding.personOther);
                                                                            binding.textView5.setText("Matched : " + user.name);

                                                                            new Handler().postDelayed(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    callAgoraApi();
                                                                                    Toast.makeText(RandomMatchActivity.this, "Redirecting you to Call!", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }, 10000);
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            }));

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void callAgoraApi() {
        Toast.makeText(this, "we need to add agora sdk in this project!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseDatabase.getInstance().getReference()
                .child("connection")
                .child(username).removeValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseDatabase.getInstance().getReference()
                .child("connection")
                .child(username).removeValue();
    }
}