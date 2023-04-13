package com.sneproj.chameleon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sneproj.chameleon.adapter.ChatAdapter;
import com.sneproj.chameleon.adapter.NotificationsAdapter;
import com.sneproj.chameleon.adapter.RateTeacherAdapter;
import com.sneproj.chameleon.databinding.ActivityMessagingBoardBinding;
import com.sneproj.chameleon.model.AppNotification;
import com.sneproj.chameleon.model.User;
import com.sneproj.chameleon.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MessagingBoardActivity extends AppCompatActivity {

    ActivityMessagingBoardBinding binding;
    List<String> idList;
    List<User> userList;
    List<User> filterList;
    List<AppNotification> notificationList;
    NotificationsAdapter notificationsAdapter;
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessagingBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageView4.setOnClickListener(v -> finish());

        binding.chats.setOnClickListener(v -> {
            binding.chats.setBackgroundResource(R.drawable.message_board_bottom_line);
            binding.notifications.setBackgroundResource(android.R.color.transparent);

            binding.notificationList.setVisibility(View.GONE);
            binding.chatList.setVisibility(View.VISIBLE);
            binding.editTextTextPersonName.setVisibility(View.VISIBLE);
        });

        binding.notifications.setOnClickListener(v -> {
            binding.notifications.setBackgroundResource(R.drawable.message_board_bottom_line);
            binding.chats.setBackgroundResource(android.R.color.transparent);

            binding.chatList.setVisibility(View.GONE);
            binding.editTextTextPersonName.setVisibility(View.GONE);
            binding.notificationList.setVisibility(View.VISIBLE);
        });

        idList = new ArrayList<>();
        userList = new ArrayList<>();
        notificationList = new ArrayList<>();

        binding.chatList.setHasFixedSize(false);
        binding.chatList.setLayoutManager(new LinearLayoutManager(this));
        binding.chatList.setAdapter(chatAdapter);

        binding.notificationList.setHasFixedSize(false);
        binding.notificationList.setLayoutManager(new LinearLayoutManager(this));
        binding.notificationList.setAdapter(notificationsAdapter);

        readNotifications();

        FirebaseDatabase.getInstance().getReference().child(Constants.COLLECTION_CHATS)
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            idList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                idList.add(dataSnapshot.getKey());
                            }

                            readUsers();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.editTextTextPersonName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList.clear();
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).name.contains(s.toString()) ||
                            userList.get(i).uname.contains(s.toString())
                    ) {
                        filterList.add(userList.get(i));
                    }

                    chatAdapter = new ChatAdapter(MessagingBoardActivity.this, filterList);
                    binding.chatList.setAdapter(chatAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void readNotifications() {
        FirebaseDatabase.getInstance().getReference().child(Constants.COLLECTION_NOTIFICATIONS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() || snapshot.hasChildren()) {
                            notificationList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                AppNotification notification = dataSnapshot.getValue(AppNotification.class);
                                notificationList.add(notification);
                            }

                            notificationsAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void readUsers() {
        FirebaseDatabase.getInstance().getReference().child(Constants.COLLECTION_USERS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            User user = dataSnapshot.getValue(User.class);
                            for (String id : idList){
                                try {
                                    if (user != null && id.equals(user.uid)) {
                                        userList.add(user);
                                    }
                                } catch (Exception ignored) {

                                }
                            }
                        }

                        if (userList.size() > 0) {
                            chatAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MessagingBoardActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}