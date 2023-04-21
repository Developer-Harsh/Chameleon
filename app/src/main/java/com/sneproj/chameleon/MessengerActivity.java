package com.sneproj.chameleon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.sneproj.chameleon.adapter.MessagesAdapter;
import com.sneproj.chameleon.databinding.ActivityMessengerBinding;
import com.sneproj.chameleon.model.Message;
import com.sneproj.chameleon.model.User;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.sneproj.chameleon.databinding.ActivityMessengerBinding;
import com.sneproj.chameleon.model.User;
import com.sneproj.chameleon.utils.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class MessengerActivity extends AppCompatActivity {

    ActivityMessengerBinding binding;
    MessagesAdapter adapter;
    ArrayList<Message> messages;
    String senderRoom, receiverRoom;
    FirebaseDatabase database;
    FirebaseStorage storage;
//    User user;
    String senderUid;
    String receiverUid;
    String uid, name, uname, profile, bio, email, location, lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessengerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(MessengerActivity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(MessengerActivity.this.getColor(R.color.bg_main));
        }
        name = getIntent().getStringExtra("name");
        profile = getIntent().getStringExtra("profile");
        uname = getIntent().getStringExtra("uname");
        bio = getIntent().getStringExtra("bio");
        email = getIntent().getStringExtra("email");
        location = getIntent().getStringExtra("location");
        uid = getIntent().getStringExtra("uid");
        lang = getIntent().getStringExtra("lang");

//        user = (User) getIntent().getSerializableExtra("user");
        uid = getIntent().getStringExtra("uid");
        database = FirebaseDatabase.getInstance();
        database.getReference().keepSynced(true);
        storage = FirebaseStorage.getInstance();
        receiverUid = uid;
        senderUid = FirebaseAuth.getInstance().getUid();
        messages = new ArrayList<>();
        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;
        adapter = new MessagesAdapter(this, messages, senderRoom, receiverRoom);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.chats.setLayoutManager(linearLayoutManager);
        binding.chats.setAdapter(adapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.chats.scrollToPosition(messages.size() - 1);
            }
        }, 1000);

        binding.name.setText(name);

        Glide.with(MessengerActivity.this)
                .load(profile)
                .into(binding.profile);

        database.getReference().keepSynced(true);
        database.getReference()
                .child(Constants.KEY_COLLECTION_CHATS)
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Message message = snapshot1.getValue(Message.class);
                            if (message != null) {
                                message.setMessageId(snapshot1.getKey());
                            }
                            messages.add(message);
                        }

                        int count = messages.size();
                        if (count == 0) {
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter.notifyItemRangeRemoved(messages.size(), messages.size());
                            adapter.notifyItemRangeInserted(messages.size(), messages.size());
                            binding.chats.smoothScrollToPosition(messages.size() - 1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.bar.setOnClickListener(v -> {
            Intent intent = new Intent(MessengerActivity.this, ProfileActivity.class);
            intent.putExtra("uid", uid);
            intent.putExtra("name", name);
            intent.putExtra("profile", profile);
            intent.putExtra("uname", uname);
            intent.putExtra("bio", bio);
            intent.putExtra("email", email);
            intent.putExtra("location", location);
            intent.putExtra("uid", uid);
            intent.putExtra("lang", lang);
            startActivity(intent);
        });

        binding.send.setOnClickListener(v -> {
            String messageTxt = Objects.requireNonNull(binding.message.getText()).toString();
            if (messageTxt.isEmpty()) {
                Snackbar.make(
                        findViewById(android.R.id.content),
                        "Empty message cannot be sent!",
                        Snackbar.LENGTH_SHORT
                ).show();
            } else {
                Date date = new Date();
                Message message = new Message(messageTxt, senderUid, date.getTime());
                binding.message.setText("");

                String randomKey = database.getReference().push().getKey();

                HashMap<String, Object> lastMsgObj = new HashMap<>();
                lastMsgObj.put("lastMsg", message.getMessage());
                lastMsgObj.put("lastMsgTime", date.getTime());
                lastMsgObj.put("read", true);

                HashMap<String, Object> lastMsgObj2 = new HashMap<>();
                lastMsgObj2.put("lastMsg", message.getMessage());
                lastMsgObj2.put("lastMsgTime", date.getTime());
                lastMsgObj2.put("read", false);

                database.getReference().child(Constants.COLLECTION_CHATS).child(receiverUid).child(senderUid).setValue(true);
                database.getReference().child(Constants.COLLECTION_CHATS).child(senderUid).child(receiverUid).setValue(true);
                database.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj2);
                database.getReference().child("chats").child(receiverRoom).updateChildren(lastMsgObj);

                if (randomKey != null) {
                    database.getReference().child("chats")
                            .child(receiverRoom)
                            .child("messages")
                            .child(randomKey)
                            .setValue(message);

                    database.getReference().child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(randomKey)
                            .setValue(message);
                }
            }
        });
    }
}