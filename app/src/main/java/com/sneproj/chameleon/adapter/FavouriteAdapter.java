package com.sneproj.chameleon.adapter;

import android.content.Context;
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
import com.sneproj.chameleon.R;
import com.sneproj.chameleon.databinding.ItemRatingTeacherBinding;
import com.sneproj.chameleon.model.FavModal;
import com.sneproj.chameleon.model.User;
import com.sneproj.chameleon.utils.Constants;

import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavHolder> {
    Context context;
    ArrayList<FavModal> list;
    User user;

    public FavouriteAdapter(Context context, ArrayList<FavModal> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FavHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavHolder(LayoutInflater.from(context).inflate(R.layout.item_rating_teacher, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavHolder holder, int position) {
        String uid = list.get(position).getFavname();


        FirebaseDatabase.getInstance().getReference().child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user = dataSnapshot.getValue(User.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

            Glide.with(context).load(user.profile).into(holder.binding.img);
        holder.binding.bio.setText(user.bio);
        holder.binding.name.setText(user.name);



            FirebaseDatabase.getInstance().getReference().child(Constants.COLLECTION_PRESENCE)
                    .child(user.uid)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                                holder.binding.presence.setVisibility(View.VISIBLE);
                            else
                                holder.binding.presence.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FavHolder extends RecyclerView.ViewHolder {
        ItemRatingTeacherBinding binding;

        public FavHolder(View itemView) {
            super(itemView);
            binding = ItemRatingTeacherBinding.bind(itemView);
        }


    }
}
