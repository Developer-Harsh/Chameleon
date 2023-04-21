package com.sneproj.chameleon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Build;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sneproj.chameleon.adapter.FavouriteAdapter;
import com.sneproj.chameleon.adapter.RateTeacherAdapter;
import com.sneproj.chameleon.databinding.ActivitySubscriptionBinding;
import com.sneproj.chameleon.databinding.ActivityWishlistBinding;
import com.sneproj.chameleon.model.FavModal;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {

    ActivityWishlistBinding binding;
    FavouriteAdapter adapter;
    ArrayList<FavModal> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWishlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(WishlistActivity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(WishlistActivity.this.getColor(R.color.bg_main));
        }

        list = new ArrayList<>();
        adapter = new FavouriteAdapter(WishlistActivity.this, list);
        binding.wishlistRecyclerview.setHasFixedSize(false);
        binding.wishlistRecyclerview.setLayoutManager(new LinearLayoutManager(WishlistActivity.this));
        binding.wishlistRecyclerview.setAdapter(adapter);


        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Favourite")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            FavModal modal = dataSnapshot.getValue(FavModal.class);
                            list.add(modal);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}