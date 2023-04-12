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
import com.sneproj.chameleon.MessengerActivity;
import com.sneproj.chameleon.R;
import com.sneproj.chameleon.databinding.ItemChatsBinding;
import com.sneproj.chameleon.model.AppNotification;
import com.sneproj.chameleon.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.UsersViewHolder> {

    Context context;
    List<AppNotification> users;

    public NotificationsAdapter(Context context, List<AppNotification> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public NotificationsAdapter.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_chats, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.UsersViewHolder holder, int position) {
        ((UsersViewHolder) holder).setData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        ItemChatsBinding binding;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemChatsBinding.bind(itemView);
        }

        public void setData(AppNotification user) {
            binding.name.setText(user.name);
            binding.message.setText(user.message);
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            binding.date.setText(dateFormat.format(new Date(user.getDate())));
            Glide.with(context)
                    .load(user.img)
                    .into(binding.profile);
        }
    }
}
