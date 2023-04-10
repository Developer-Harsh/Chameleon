package com.sneproj.chameleon.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sneproj.chameleon.R;
import com.sneproj.chameleon.adapter.CircularTeacherAdapter;
import com.sneproj.chameleon.adapter.RateTeacherAdapter;
import com.sneproj.chameleon.databinding.FragmentExploreBinding;
import com.sneproj.chameleon.model.LangModal;
import com.sneproj.chameleon.model.User;
import com.sneproj.chameleon.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExploreFragment extends Fragment {

    FragmentExploreBinding binding;
    List<User> circularList;
    CircularTeacherAdapter circularTeacherAdapter;
    List<User> bestTeachers;
    RateTeacherAdapter rateTeacherAdapter;
    List<User> filterList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(inflater, container, false);

        circularList = new ArrayList<>();
        circularTeacherAdapter = new CircularTeacherAdapter(circularList, getContext(), true);
        binding.circuteach.setHasFixedSize(false);
        binding.circuteach.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.circuteach.setAdapter(circularTeacherAdapter);

        bestTeachers = new ArrayList<>();
        rateTeacherAdapter = new RateTeacherAdapter(bestTeachers, getContext());
        binding.ratuTeach.setHasFixedSize(false);
        binding.ratuTeach.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.ratuTeach.setAdapter(rateTeacherAdapter);

        readTeachers();

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList.clear();
                for (int i = 0; i < bestTeachers.size(); i++) {
                    if (bestTeachers.get(i).name.contains(s.toString()) ||
                            bestTeachers.get(i).uname.contains(s.toString())
                    ) {
                        filterList.add(bestTeachers.get(i));
                    }

                    rateTeacherAdapter = new RateTeacherAdapter(filterList, getContext());
                    binding.ratuTeach.setAdapter(rateTeacherAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return binding.getRoot();
    }

    private void readTeachers() {
        FirebaseDatabase.getInstance().getReference().child(Constants.COLLECTION_USERS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        circularList.clear();
                        bestTeachers.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            User user = new User();
                            user.uid = dataSnapshot.child("uid").getValue(String.class);
                            user.profile = dataSnapshot.child("profile").getValue(String.class);
                            user.name = dataSnapshot.child("name").getValue(String.class);
                            user.uname = dataSnapshot.child("uname").getValue(String.class);
                            user.bio = dataSnapshot.child("bio").getValue(String.class);
                            user.number = dataSnapshot.child("number").getValue(String.class);
                            user.email = dataSnapshot.child("email").getValue(String.class);
                            user.location = dataSnapshot.child("location").getValue(String.class);
                            user.nativeLang = dataSnapshot.child("nativeLang").getValue(String.class);
                            user.gender = dataSnapshot.child("gender").getValue(String.class);
                            user.rank = dataSnapshot.child("rank").getValue(String.class);

                            if (Objects.equals(user.rank, "Teacher")) {
                                circularList.add(user);
                                bestTeachers.add(user);
                            }
                        }

                        if (circularList.size() > 0 && bestTeachers.size() > 0) {
                            rateTeacherAdapter.notifyDataSetChanged();
                            circularTeacherAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "There is no teacher!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}