package com.portfolio_app.mvvm_sample.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.portfolio_app.R;
import com.portfolio_app.mvvm_sample.service.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stefan Wyszynski
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    List<UserInfo> userList;
    private Context context;

    public UserListAdapter(Context context) {
        this.context = context;
        userList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mvvm_user_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserInfo user = userList.get(position);
        holder.firstName.setText(user.first_name);
        holder.lastName.setText(user.last_name);
        Glide.with(context).load(user.avatar).into(holder.avatar);
    }

    public void setUserList(List<UserInfo> users) {
        if (users != null) {
            userList.clear();
            userList.addAll(users);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (userList != null) {
            return userList.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView avatar;
        private final TextView firstName;
        private final TextView lastName;

        public ViewHolder(View group) {
            super(group);
            avatar = group.findViewById(R.id.avatar);
            firstName = group.findViewById(R.id.last_name);
            lastName = group.findViewById(R.id.first_name);
        }
    }
}
