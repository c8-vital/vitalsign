package com.example.vs1;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private ArrayList<User> mUserList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View userView;
        TextView userTime;
        TextView userTem;
        TextView userOxi;
        TextView userPul;

        public ViewHolder(@NonNull View view) {
            super(view);
            userView = view;
            userTime = itemView.findViewById(R.id.user_time);
            userTem = itemView.findViewById(R.id.user_tem);
            userOxi = itemView.findViewById(R.id.user_oxi);
            userPul = itemView.findViewById(R.id.user_pul);

        }
    }

    public UserAdapter (ArrayList<User> userArrayList) {
        mUserList = userArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.userView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = mUserList.get(holder.getAdapterPosition());
//                Toast.makeText(v.getContext(), "111", Toast.LENGTH_LONG).show();
            }
        });
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUserList.get(position);
        holder.userTime.setText(user.getTime());
        holder.userTem.setText(user.getTem());
        holder.userOxi.setText(user.getOxi());
        holder.userPul.setText(user.getPul());
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

}
