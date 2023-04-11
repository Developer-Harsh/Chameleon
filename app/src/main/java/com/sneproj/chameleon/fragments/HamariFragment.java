package com.sneproj.chameleon.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sneproj.chameleon.EditProfileActivity;
import com.sneproj.chameleon.LoadingDialog;
import com.sneproj.chameleon.NewUserActivity;
import com.sneproj.chameleon.R;
import com.sneproj.chameleon.SubscriptionActivity;
import com.sneproj.chameleon.databinding.FragmentHamariBinding;
import com.sneproj.chameleon.model.User;
import com.sneproj.chameleon.utils.Constants;

import java.util.Objects;

public class HamariFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    FragmentHamariBinding binding;
    private DatabaseReference reference, dbref;
    private FirebaseDatabase database;
    User usermodal;
    FirebaseAuth auth;
    FirebaseUser user;
    LoadingDialog loadingDialog = new LoadingDialog();
    NavController navController;
    private ActionBarDrawerToggle toggle;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHamariBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child(Constants.COLLECTION_USERS).child(user.getUid());

        getUserData();




        toggle = new ActionBarDrawerToggle((Activity) requireContext(), binding.drawerLayout,R.string.start, R.string.close);
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


       return binding.getRoot();

}

    private void getUserData() {
        loadingDialog.showdialog(requireContext());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    usermodal = snapshot.getValue(User.class);
                    binding.name.setText(usermodal.name);
                    binding.username.setText(usermodal.uname);
                    binding.countryName.setText(usermodal.location);
                    Glide.with(requireContext()).load(usermodal.profile).into(binding.profile);
                    loadingDialog.dismissdialog();
                } else {
                    Toast.makeText(requireContext(), "Not available", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            case R.id.edit_profile:
                startActivity(new Intent(requireContext(), EditProfileActivity.class));
                break;
            case R.id.subsctiption:
                startActivity(new Intent(requireContext(), SubscriptionActivity.class));
                break;

            case R.id.histroy:
                Toast.makeText(requireContext(), "Edit Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.refer:
                Toast.makeText(requireContext(), "Edit Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.wishlist:
                Toast.makeText(requireContext(), "Edit Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.themes:
                Toast.makeText(requireContext(), "Edit Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.help:
                Toast.makeText(requireContext(), "Edit Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share_profile:
                Toast.makeText(requireContext(), "Edit Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                Toast.makeText(requireContext(), "Edit Profile", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


}