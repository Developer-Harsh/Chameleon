package com.sneproj.chameleon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sneproj.chameleon.adapter.InterestAdapter;
import com.sneproj.chameleon.databinding.ActivityProfileBinding;
import com.sneproj.chameleon.databinding.LogoutdialogBinding;
import com.sneproj.chameleon.utils.Constants;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityProfileBinding binding;
    InterestAdapter adapter;
    ArrayList<ChipModal> list;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(ProfileActivity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(ProfileActivity.this.getColor(R.color.bg_main));
        }
        toggle = new ActionBarDrawerToggle(ProfileActivity.this, binding.drawerLayout,R.string.start, R.string.close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navigationView.setNavigationItemSelectedListener(this);

        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    binding.drawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });

        binding.navigationView.setNavigationItemSelectedListener(this);


        reference = FirebaseDatabase.getInstance().getReference().child(Constants.COLLECTION_USERS);
        auth = FirebaseAuth.getInstance();
        String name = getIntent().getStringExtra("name");
        String profile = getIntent().getStringExtra("profile");
        String uname = getIntent().getStringExtra("uname");
        String bio = getIntent().getStringExtra("bio");
        String email = getIntent().getStringExtra("email");
        String location = getIntent().getStringExtra("location");
        String uid = getIntent().getStringExtra("uid");
        String lang = getIntent().getStringExtra("lang");

        binding.profileName.setText(name);
        binding.profileBio.setText(bio);
        binding.profileCountryName.setText(location);
        binding.profileName.setText(name);
        binding.profileUsername.setText(uname);
        binding.language.setText(lang);
        Glide.with(ProfileActivity.this).load(profile).into(binding.profileImage);

        list= new ArrayList<>();
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        binding.interestRecyclerview.setLayoutManager(layoutManager);
        binding.interestRecyclerview.setHasFixedSize(true);
        adapter = new InterestAdapter( list, ProfileActivity.this);
        binding.interestRecyclerview.setAdapter(adapter);

        binding.levelTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.image_unselected), PorterDuff.Mode.SRC_ATOP);
        binding.levelItem.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("intrest")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                         ChipModal chipModal =dataSnapshot.getValue(ChipModal.class);
                            list.add(chipModal);
                    }
                    adapter.notifyDataSetChanged(); // notify adapter of data changes

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.levelTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.levelTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.black), PorterDuff.Mode.SRC_ATOP);
                binding.calendarTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.image_unselected), PorterDuff.Mode.SRC_ATOP);
                binding.videoTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.image_unselected), PorterDuff.Mode.SRC_ATOP);

                binding.levelItem.setVisibility(View.VISIBLE);
                binding.videoItem.setVisibility(View.GONE);
                binding.calendarItem.setVisibility(View.GONE);
            }
        });

        binding.calendarTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.levelTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.image_unselected), PorterDuff.Mode.SRC_ATOP);
                binding.calendarTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.black), PorterDuff.Mode.SRC_ATOP);
                binding.videoTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.image_unselected), PorterDuff.Mode.SRC_ATOP);
                binding.levelItem.setVisibility(View.GONE);
                binding.videoItem.setVisibility(View.GONE);
                binding.calendarItem.setVisibility(View.VISIBLE);
            }
        });

        binding.videoTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.levelTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.image_unselected), PorterDuff.Mode.SRC_ATOP);
                binding.calendarTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.image_unselected), PorterDuff.Mode.SRC_ATOP);
                binding.videoTab.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.black), PorterDuff.Mode.SRC_ATOP);
                binding.levelItem.setVisibility(View.GONE);
                binding.videoItem.setVisibility(View.VISIBLE);
                binding.calendarItem.setVisibility(View.GONE);
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        reference.child(auth.getCurrentUser().getUid()).child("Favourite")
                .child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            binding.favourite.setImageDrawable(getResources().getDrawable(R.drawable.favfill));
                        }else{
                            binding.favourite.setImageDrawable(getResources().getDrawable(R.drawable.fav));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child(auth.getCurrentUser().getUid()).child("Favourite")
                        .child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    binding.favourite.setImageDrawable(getResources().getDrawable(R.drawable.fav));
                                    snapshot.getRef().removeValue();
                                }else{
                                    snapshot.getRef().child("favname").setValue(uid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                binding.favourite.setImageDrawable(getResources().getDrawable(R.drawable.favfill));
                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



            }
        });




    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile_chat:
                startActivity(new Intent(ProfileActivity.this, MessengerActivity.class));
                break;
            case R.id.profile_block:
                Toast.makeText(this, "Block", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile_report:
                Toast.makeText(this, "Report", Toast.LENGTH_SHORT).show();

                break;

        }
        binding.drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

}