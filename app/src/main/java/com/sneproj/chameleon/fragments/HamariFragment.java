package com.sneproj.chameleon.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sneproj.chameleon.LoadingDialog;
import com.sneproj.chameleon.NewUserActivity;
import com.sneproj.chameleon.R;
import com.sneproj.chameleon.databinding.FragmentHamariBinding;
import com.sneproj.chameleon.model.User;
import com.sneproj.chameleon.utils.Constants;

public class HamariFragment extends Fragment {

    FragmentHamariBinding binding;
    private DatabaseReference reference, dbref;
    private FirebaseDatabase database;
    User usermodal;
    FirebaseAuth auth;
    FirebaseUser user;
    LoadingDialog loadingDialog = new LoadingDialog();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHamariBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child(Constants.COLLECTION_USERS).child(user.getUid());

        getUserData();
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
}