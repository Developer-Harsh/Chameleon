package com.sneproj.chameleon.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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

import com.sneproj.chameleon.BuildConfig;
import com.sneproj.chameleon.EditProfileActivity;
import com.sneproj.chameleon.LoadingDialog;
import com.sneproj.chameleon.LoginActivity;
import com.sneproj.chameleon.MainActivity;
import com.sneproj.chameleon.NewUserActivity;
import com.sneproj.chameleon.R;
import com.sneproj.chameleon.ReferActivity;
import com.sneproj.chameleon.SubscriptionActivity;
import com.sneproj.chameleon.TransactionActivity;
import com.sneproj.chameleon.WishlistActivity;
import com.sneproj.chameleon.databinding.FragmentHamariBinding;
import com.sneproj.chameleon.databinding.LogoutdialogBinding;
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

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int checkedItem;
    private String selected;

    private final String CHECKITEM = "check_item";



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

        binding.schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), MainActivity.class));
            }
        });

        readRollowing();

           binding.navigationView.setNavigationItemSelectedListener(this);


        sharedPreferences= getActivity().getSharedPreferences("themes", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        switch (getCheckedItem()){
            case 0:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }

        return binding.getRoot();

}

private void readRollowing() {
        FirebaseDatabase.getInstance().getReference().child("follow")
                .child(FirebaseAuth.getInstance().getUid())
                .child("following")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            binding.followings.setText(snapshot.getChildrenCount() + "");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    FirebaseDatabase.getInstance().getReference().child("follow")
            .child(FirebaseAuth.getInstance().getUid())
            .child("followers")
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        binding.followers.setText(snapshot.getChildrenCount() + "");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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
                startActivity(new Intent(requireContext(), TransactionActivity.class));
                break;
            case R.id.refer:
                startActivity(new Intent(requireContext(), ReferActivity.class));
                break;
            case R.id.wishlist:
                startActivity(new Intent(requireContext(), WishlistActivity.class));
                break;
            case R.id.themes:
                ShowTheme();
                break;
            case R.id.help:
                String url = "https://support.google.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.share_profile:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.logout:
                        LogoutdialogBinding binding;
                        binding = LogoutdialogBinding.inflate(getLayoutInflater());

                        AlertDialog delete_dialog = new AlertDialog.Builder(requireContext())
                                .setCancelable(false)
                                .setView(binding.getRoot()).create();


                        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                delete_dialog.cancel();

                            }
                        });

                        binding.Logoutbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                auth.signOut();
                                Intent intent = new Intent(requireContext(), LoginActivity.class);
                                startActivity(intent);
                                  getActivity().finish();
                            }
                        });

                        delete_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        delete_dialog.show();

                break;
        }
        binding.drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    private int getCheckedItem(){
        return sharedPreferences.getInt(CHECKITEM,0);

    }
    private void setCheckedItem(int i){
        editor.putInt(CHECKITEM, i);
        editor.apply();
    }

    private void ShowTheme() {
        String[] themes= this.getResources().getStringArray(R.array.theme);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setTitle("Select Themes");
        builder.setSingleChoiceItems(R.array.theme, getCheckedItem(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selected = themes[i];
                checkedItem = i;
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (selected == null){
                    selected = themes[i];
                    checkedItem = i;
                }
                switch (selected){
                    case "System Default":
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                        break;
                    case "Dark":
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        break;
                    case "Light":
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        break;
                }
                setCheckedItem(checkedItem);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}