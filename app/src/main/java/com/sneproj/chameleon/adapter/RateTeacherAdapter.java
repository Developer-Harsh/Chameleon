package com.sneproj.chameleon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.sneproj.chameleon.R;
import com.sneproj.chameleon.databinding.ItemRatingTeacherBinding;
import com.sneproj.chameleon.databinding.ItemTeachersCircularBinding;
import com.sneproj.chameleon.model.User;

import java.util.List;

public class RateTeacherAdapter extends RecyclerView.Adapter<RateTeacherAdapter.ViewHolder> {

    public List<User> userList;
    public Context context;

    public RateTeacherAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @Override
    public RateTeacherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rating_teacher, parent, false));
    }

    @Override
    public void onBindViewHolder(RateTeacherAdapter.ViewHolder holder, int position) {
        holder.set(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemRatingTeacherBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = ItemRatingTeacherBinding.bind(itemView);
        }

        private void set(User user) {
            // we need to set user data into TeacherCircularItem
        }
    }
}
