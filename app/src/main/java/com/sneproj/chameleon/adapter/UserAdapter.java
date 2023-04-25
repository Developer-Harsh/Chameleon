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
import com.sneproj.chameleon.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UsersViewHolder> {

    Context context;
    List<User> users;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserAdapter.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_chats, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UsersViewHolder holder, int position) {
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

        public void setData(User user) {
            binding.name.setText(user.name);
            binding.message.setText(user.uname);
            binding.date.setVisibility(View.GONE);

            Glide.with(context)
                    .load(user.profile)
                    .into(binding.profile);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context != null) {
                        Intent intent = new Intent(context, MessengerActivity.class);
                        intent.putExtra("user", user);
                        intent.putExtra("name", user.name);
                        intent.putExtra("profile", user.profile);
                        intent.putExtra("uname", user.uname);
                        intent.putExtra("bio", user.bio);
                        intent.putExtra("email", user.email);
                        intent.putExtra("location", user.location);
                        intent.putExtra("uid", user.uid);
                        intent.putExtra("lang", user.nativeLang);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
