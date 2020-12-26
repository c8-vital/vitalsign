package com.example.vs1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private ArrayList<User> mUserList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View userView;
        TextView user_name;

        public ViewHolder(@NonNull View view) {
            super(view);
            userView = view;
            user_name = itemView.findViewById(R.id.user_name);

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
                Toast.makeText(v.getContext(), "111", Toast.LENGTH_LONG).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUserList.get(position);
        holder.user_name.setText("id:"+user.getId()+" tem:"+user.getTem()+" oxi:"+user.getOxi());
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }


}
