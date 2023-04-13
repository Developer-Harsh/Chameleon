package com.sneproj.chameleon.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sneproj.chameleon.ProfileActivity;
import com.sneproj.chameleon.R;
import com.sneproj.chameleon.databinding.ItemRatingTeacherBinding;
import com.sneproj.chameleon.databinding.ItemTeachersCircularBinding;
import com.sneproj.chameleon.model.User;
import com.sneproj.chameleon.utils.Constants;

import java.util.List;

public class RateTeacherAdapter extends RecyclerView.Adapter<RateTeacherAdapter.ViewHolder> {

    public List<User> userList;
    public Context context;

    public RateTeacherAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @Override
    public RateTeacherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rating_teacher, parent, false));
    }

    @Override
    public void onBindViewHolder(RateTeacherAdapter.ViewHolder holder, int position) {
        holder.set(userList.get(position));
        User user = userList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("name", user.name);
                intent.putExtra("profile", user.profile);
                intent.putExtra("uname", user.uname);
                intent.putExtra("bio", user.bio);
                intent.putExtra("email", user.email);
                intent.putExtra("location", user.location);
           context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemRatingTeacherBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = ItemRatingTeacherBinding.bind(itemView);
        }

        private void set(User user) {
            Glide.with(context).load(user.profile).into(binding.img);
            binding.bio.setText(user.bio);
            binding.name.setText(user.name);

            FirebaseDatabase.getInstance().getReference().child(Constants.COLLECTION_PRESENCE)
                    .child(user.uid)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                                binding.presence.setVisibility(View.VISIBLE);
                            else
                                binding.presence.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            FirebaseDatabase.getInstance().getReference()
                    .child(Constants.COLLECTION_RATES)
                    .child(user.uid)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                if (snapshot.hasChild(FirebaseAuth.getInstance().getUid())) {
                                    binding.rate.setTag("rated");
                                } else {
                                    binding.rate.setTag("rate");
                                }

                                if (snapshot.getChildrenCount() > 0)
                                    binding.ratings.setText(snapshot.getChildrenCount() + "");
                                else
                                    binding.ratings.setText("0");
                            } else {
                                binding.ratings.setText("0");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            binding.rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (binding.rate.getTag().equals("rated")) {
                        FirebaseDatabase.getInstance().getReference()
                                .child(Constants.COLLECTION_RATES)
                                .child(user.uid)
                                .child(FirebaseAuth.getInstance().getUid())
                                .setValue(true);
                    } else {
                        FirebaseDatabase.getInstance().getReference()
                                .child(Constants.COLLECTION_RATES)
                                .child(user.uid)
                                .child(FirebaseAuth.getInstance().getUid())
                                .removeValue();
                    }
                }
            });
        }
    }
}
