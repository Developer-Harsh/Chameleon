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
import com.sneproj.chameleon.databinding.ItemTeachersCircularBinding;
import com.sneproj.chameleon.model.LangModal;
import com.sneproj.chameleon.model.User;
import com.sneproj.chameleon.utils.Constants;

import java.util.List;

public class CircularTeacherAdapter extends RecyclerView.Adapter<CircularTeacherAdapter.ViewHolder> {

    public List<User> userList;
    public Context context;
    public Boolean isCalledFromExplore;

    public CircularTeacherAdapter(List<User> userList, Context context, Boolean isCalledFromExplore) {
        this.userList = userList;
        this.context = context;
        this.isCalledFromExplore = isCalledFromExplore;
    }

    @Override
    public CircularTeacherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_teachers_circular, parent, false));
    }

    @Override
    public void onBindViewHolder(CircularTeacherAdapter.ViewHolder holder, int position) {
        holder.set(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemTeachersCircularBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = ItemTeachersCircularBinding.bind(itemView);
        }

        private void set(User user) {
            binding.presence.setVisibility(View.GONE);
            binding.name.setText(user.name);

            if (isCalledFromExplore)
                binding.country.setVisibility(View.GONE);
            else
                binding.country.setVisibility(View.VISIBLE);

            // needs to add country logic here to show Country Image

            Glide.with(context).load(user.profile).placeholder(R.drawable.indian_flag).into(binding.img);
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

        }
    }
}
