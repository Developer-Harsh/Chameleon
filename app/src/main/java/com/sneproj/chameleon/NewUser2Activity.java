package com.sneproj.chameleon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sneproj.chameleon.adapter.LanguageAdapter;
import com.sneproj.chameleon.databinding.ActivityNewUser2Binding;
import com.sneproj.chameleon.model.LangModal;
import com.sneproj.chameleon.model.User;
import com.sneproj.chameleon.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class NewUser2Activity extends AppCompatActivity {
ActivityNewUser2Binding binding;
ArrayList<LangModal> list;
LanguageAdapter adapter;
LoadingDialog dialog = new LoadingDialog();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewUser2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(NewUser2Activity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(NewUser2Activity.this.getColor(R.color.bg_main));
        }
        list = new ArrayList<>();

        dialog.showdialog(this);

        FirebaseDatabase.getInstance().getReference().child("lang")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                            LangModal langModal = dataSnapshot.getValue(LangModal.class);
                            list.add(langModal);

                            dialog.dismissdialog();
                    }
                    binding.recyclerview.setHasFixedSize(true);
                    binding.recyclerview.setLayoutManager(new LinearLayoutManager(NewUser2Activity.this));
                    adapter = new LanguageAdapter( NewUser2Activity.this, list);
                    binding.recyclerview.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());

            }
        });

        binding.Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.getSelected() != null){
                    Toast.makeText(NewUser2Activity.this, adapter.getSelected().getName().toString(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(NewUser2Activity.this, "No Selection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void filter(String newText) {
        ArrayList<LangModal> filteredList = new ArrayList<>();
        for (LangModal singleNote : list){
            if (singleNote.getName().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(singleNote);
            }
        }
        adapter.filterList(filteredList);
    }
}