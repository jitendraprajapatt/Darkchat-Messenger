package com.app.darkchat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.darkchat.Dashboard.ChatActiviy;
import com.app.darkchat.Dashboard.Dashboard;
import com.app.darkchat.Dashboard.SettingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.userViewHolder> {
    Context dashboard;
    ArrayList<search_class> userDataArrayList;
    public UserAdapter(Dashboard dashboard, ArrayList<search_class> userDataArrayList) {

        this.dashboard = dashboard;
        this.userDataArrayList = userDataArrayList;
    }

    @NonNull

    @Override
    public userViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(dashboard).inflate(R.layout.item_user_row,parent,false);
        return new userViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  UserAdapter.userViewHolder holder, int position) {

        search_class users =userDataArrayList.get(position);
        holder.userName.setText(users.username);
        holder.userStatus.setText(users.status);
        Picasso.get().load(users.imgUrl).into(holder.userImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String signIn_key = auth.getUid();
                String holderKey = users.getKey();
                if(signIn_key.equals(holderKey) ){
                    Intent intent = new Intent(dashboard, SettingActivity.class);
                    dashboard.startActivity(intent);
                }else {
                    Intent intent = new Intent(dashboard, ChatActiviy.class);
                    intent.putExtra("Name", users.getUsername());
                    intent.putExtra("ReceiverImage", users.getImgUrl());
                    intent.putExtra("Uid", users.getKey());
                    dashboard.startActivity(intent);
                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return userDataArrayList.size();
    }

    class userViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userImage ;
        TextView userName ;
        TextView userStatus ;
        public userViewHolder(@NonNull  View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.img);
            userName =itemView.findViewById(R.id.user_name);
            userStatus = itemView.findViewById(R.id.user_status);
        }
    }
}
