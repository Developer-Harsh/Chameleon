package com.sneproj.chameleon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sneproj.chameleon.R;
import com.sneproj.chameleon.databinding.ItemLanguageBinding;
import com.sneproj.chameleon.model.LangModal;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LangHolder>{
    Context context;
    ArrayList<LangModal> list;
    private int checkedPosition = 0;

    public LanguageAdapter(Context context, ArrayList<LangModal> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LangHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_language_, parent, false);
        return new LangHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LangHolder holder, int position) {
        holder.bind(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LangHolder extends RecyclerView.ViewHolder {
        ItemLanguageBinding binding;
        public LangHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemLanguageBinding.bind(itemView);
        }
        void bind(final LangModal langModal){
            if (checkedPosition == -1){
                binding.check.setVisibility(View.GONE);
            }else{
                if (checkedPosition == getAdapterPosition()){
                    binding.check.setVisibility(View.VISIBLE);
                }
                else {
                    binding.check.setVisibility(View.GONE);
                }
            }

            binding.name.setText(langModal.getName());
            Glide.with(context).load(langModal.getImage()).into(binding.flag);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.check.setVisibility(View.VISIBLE);
                    if (checkedPosition != getAdapterPosition()){
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                }
            });
        }
    }
    public void filterList(ArrayList<LangModal> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }

    public LangModal getSelected(){
        if (checkedPosition != -1){
            return list.get(checkedPosition);
        }else{
            return null;
        }
    }


}
