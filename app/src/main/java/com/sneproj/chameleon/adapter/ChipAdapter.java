package com.sneproj.chameleon.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sneproj.chameleon.ChipModal;
import com.sneproj.chameleon.R;

import java.util.ArrayList;

public class ChipAdapter extends RecyclerView.Adapter<ChipAdapter.ChipViewHolder> {

    private ArrayList<ChipModal> itemList;
    private ArrayList<String> selectedItemsList = new ArrayList<String>();
    private DatabaseReference mDatabase;
    Context context;

    public ChipAdapter(ArrayList<ChipModal> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @NonNull
    @Override
    public ChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.intrest_item_holder, parent, false);
        return new ChipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChipViewHolder holder, int position) {
        String item = String.valueOf(itemList.get(position));
        String itemname = itemList.get(position).getChiptext();
        holder.chip.setText(itemList.get(position).getChiptext());
        holder.chip.setTextColor(Color.BLACK);
        holder.chip.setChipBackgroundColorResource(R.color.chip_bg);

        mDatabase.child("intrest").child(itemname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    selectedItemsList.add(item);
                    holder.chip.setChipBackgroundColorResource(R.color.mainColor);
                    holder.chip.setTextColor(Color.WHITE);
                } else {
                    selectedItemsList.remove(item);
                    holder.chip.setChipBackgroundColorResource(R.color.chip_bg);
                    holder.chip.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(v -> {



            if (selectedItemsList.contains(item)) {
                selectedItemsList.remove(item);
                holder.chip.setChipBackgroundColorResource(R.color.chip_bg);
                holder.chip.setTextColor(Color.BLACK);
                mDatabase.child("intrest").child(itemname).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

            } else {
                selectedItemsList.add(item);
                holder.chip.setChipBackgroundColorResource(R.color.mainColor);
                holder.chip.setTextColor(Color.WHITE);
                mDatabase.child("intrest").child(itemname).setValue(itemname).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ChipViewHolder extends RecyclerView.ViewHolder {

        Chip chip;

        public ChipViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.chip);

        }
    }
}

