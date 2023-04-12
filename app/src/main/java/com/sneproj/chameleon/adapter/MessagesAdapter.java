package com.sneproj.chameleon.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sneproj.chameleon.R;
import com.sneproj.chameleon.databinding.ItemReceiveBinding;
import com.sneproj.chameleon.databinding.ItemSentBinding;
import com.sneproj.chameleon.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message> messages;
    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;
    String senderRoom;
    String receiverRoom;

    public MessagesAdapter(Context context, ArrayList<Message> messages, String senderRoom, String receiverRoom) {
        this.context = context;
        this.messages = messages;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_sent, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_receive, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (Objects.equals(FirebaseAuth.getInstance().getUid(), message.getSenderId())) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        if (holder.getClass() == SentViewHolder.class) {

            SentViewHolder viewHolder = (SentViewHolder) holder;

            //if (message.getMessage().contains("Photo")) {
            //    viewHolder.binding.image.setVisibility(View.VISIBLE);
            //    viewHolder.binding.message.setVisibility(View.VISIBLE);
            //    viewHolder.binding.image.setOnClickListener(v -> context.startActivity(new Intent(context, FullImageActivity.class).putExtra(Constants.KEY_PICTURE_URL, message.getImageUrl())));
            //    Glide.with(context).load(message.getImageUrl()).placeholder(R.drawable.photo).into(viewHolder.binding.image);
            //}

            viewHolder.binding.message.setText(message.getMessage());
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            viewHolder.binding.time.setText(dateFormat.format(new Date(message.getTimestamp())));

            viewHolder.binding.layoutRootSent.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Message");
                builder.setMessage("Do you want to delete this message from everyone?");
                builder.setPositiveButton("Delete", (dialog, which) -> {
                    FirebaseDatabase.getInstance().getReference()
                            .child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(message.getMessageId())
                            .removeValue();

                    FirebaseDatabase.getInstance().getReference()
                            .child("chats")
                            .child(receiverRoom)
                            .child("messages")
                            .child(message.getMessageId())
                            .removeValue();

                    dialog.dismiss();
                });
                builder.setNegativeButton("Dismiss", (dialog, which) -> dialog.dismiss());
                builder.create().show();
                return false;
            });
        } else {

            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;

            //if (message.getMessage().contains("Photo")) {
            //    viewHolder.binding.image.setVisibility(View.VISIBLE);
            //    viewHolder.binding.message.setVisibility(View.VISIBLE);
            //    viewHolder.binding.image.setOnClickListener(v -> context.startActivity(new Intent(context, FullImageActivity.class).putExtra(Constants.KEY_PICTURE_URL, message.getImageUrl())));
            //    Glide.with(context).load(message.getImageUrl()).placeholder(R.drawable.button).into(viewHolder.binding.image);
            //}

            viewHolder.binding.message.setText(message.getMessage());
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            viewHolder.binding.time.setText(dateFormat.format(new Date(message.getTimestamp())));

            viewHolder.binding.layoutRootReceive.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Message");
                builder.setMessage("Do you want to delete this message from everyone?");
                builder.setPositiveButton("Delete", (dialog, which) -> {
                    FirebaseDatabase.getInstance().getReference()
                            .child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(message.getMessageId())
                            .removeValue();

                    FirebaseDatabase.getInstance().getReference()
                            .child("chats")
                            .child(receiverRoom)
                            .child("messages")
                            .child(message.getMessageId())
                            .removeValue();

                    dialog.dismiss();
                });
                builder.setNegativeButton("Dismiss", (dialog, which) -> dialog.dismiss());
                builder.create().show();
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class SentViewHolder extends RecyclerView.ViewHolder {

        ItemSentBinding binding;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSentBinding.bind(itemView);
        }
    }

    public static class ReceiverViewHolder extends RecyclerView.ViewHolder {

        ItemReceiveBinding binding;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemReceiveBinding.bind(itemView);
        }
    }
}
