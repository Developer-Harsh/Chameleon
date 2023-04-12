package com.sneproj.chameleon.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sneproj.chameleon.MessengerActivity;
import com.sneproj.chameleon.R;
import com.sneproj.chameleon.databinding.ItemChatsBinding;
import com.sneproj.chameleon.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.UsersViewHolder> {

    Context context;
    List<User> users;

    public ChatAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ChatAdapter.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_chats, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.UsersViewHolder holder, int position) {
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
            String senderId = FirebaseAuth.getInstance().getUid();
            String senderRoom = senderId + user.uid;

            binding.name.setText(user.name);

            Glide.with(context)
                    .load(user.profile)
                    .into(binding.profile);

            FirebaseDatabase.getInstance().getReference()
                    .child("chats")
                    .child(senderRoom)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                                long time = snapshot.child("lastMsgTime").getValue(Long.class);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                                binding.date.setText(dateFormat.format(new Date(time)));
                                binding.message.setText(lastMsg);
                            } else {
                                binding.message.setText("Tap to message");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context != null) {
                        Intent intent = new Intent(context, MessengerActivity.class);
                        intent.putExtra("user", user);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
