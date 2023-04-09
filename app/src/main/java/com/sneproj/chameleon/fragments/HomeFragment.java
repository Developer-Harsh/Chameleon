package com.sneproj.chameleon.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.sneproj.chameleon.MessagingBoardActivity;
import com.sneproj.chameleon.R;
import com.sneproj.chameleon.RandomMatchActivity;
import com.sneproj.chameleon.StrikesActivity;
import com.sneproj.chameleon.adapter.CircularTeacherAdapter;
import com.sneproj.chameleon.adapter.SliderAdapter;
import com.sneproj.chameleon.databinding.FragmentHomeBinding;
import com.sneproj.chameleon.model.User;
import com.sneproj.chameleon.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    int[] images = {R.drawable.refer, R.drawable.bookless, R.drawable.promo};
    CircularTeacherAdapter adapter;
    List<User> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container,false);

        setSliderView();

        userList = new ArrayList<>();
        adapter = new CircularTeacherAdapter(userList, getContext(), false);
        binding.teachers.setHasFixedSize(false);
        binding.teachers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.teachers.setAdapter(adapter);

        readAdhyapak();
        readLanguage();

        binding.instaLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.instaLesson.setBackgroundResource(R.drawable.selector_bg);
                binding.instaMatch.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                binding.start.setText("Start Lesson");
            }
        });

        binding.instaMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.instaMatch.setBackgroundResource(R.drawable.selector_bg);
                binding.instaLesson.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                binding.start.setText("Start Match");
            }
        });

        binding.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.start.getText().equals("Start Match"))
                    startActivity(new Intent(getContext(), RandomMatchActivity.class));
                else
                    Toast.makeText(getContext(), "Bottom sheet in progress", Toast.LENGTH_SHORT).show();
            }
        });

        binding.strikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), StrikesActivity.class));
            }
        });

        binding.messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MessagingBoardActivity.class));
            }
        });

        return  binding.getRoot();
    }

    private void readAdhyapak() {
        FirebaseDatabase.getInstance().getReference().child(Constants.COLLECTION_USERS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
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

                            if (Objects.equals(user.rank, "Teacher"))
                                userList.add(user);
                        }

                        if (userList.size() > 0) {
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "There is no teacher!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void setSliderView() {
        SliderAdapter adapter = new SliderAdapter(images);
        binding.imageSlider.setSliderAdapter(adapter);
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSlider.setAutoCycle(true);
    }
}