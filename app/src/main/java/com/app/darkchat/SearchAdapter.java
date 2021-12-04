package com.app.darkchat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.darkchat.Dashboard.Dashboard;
import com.app.darkchat.Dashboard.SearchActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.searchViewHolder> {
    Context searchActivity;
    // get data of founded user
    ArrayList<search_class> searchResult;

    public SearchAdapter(SearchActivity searchActivity, ArrayList<search_class> searchResult) {
        this.searchActivity = searchActivity;
        this.searchResult = searchResult;
    }

    @NonNull

    @Override
    public searchViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(searchActivity).inflate(R.layout.search_layout,parent,false);
        return new SearchAdapter.searchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  SearchAdapter.searchViewHolder holder, int position) {
        search_class result = searchResult.get(position);
        holder.userName.setText(result.username);
        Picasso.get().load(result.imgUrl).into(holder.userImage);


// add data in join room
        holder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog progressDialog = new ProgressDialog(searchActivity);
                progressDialog.setMessage("adding.......");
                progressDialog.show();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getInstance().getReference("JoinRoom");
                reference.child(auth.getUid()).child(result.key).setValue(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull  Task<Void> task) {
                        if (task.isSuccessful()){
// Request sent with your data
                            DatabaseReference reference = database.getInstance().getReference("Users");
                            reference.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String username = snapshot.child("name").getValue().toString();
                                    String imgUrl = snapshot.child("imageUri").getValue().toString();
                                    String key = auth.getUid();
                                    String status = snapshot.child("status").getValue().toString();
                                    search_class req_data = new search_class(username,imgUrl,key,status);
                                    DatabaseReference reference = database.getInstance().getReference("RequestRoom");
                                    reference.child(result.key).child(auth.getUid()).setValue(req_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                progressDialog.dismiss();
                                                Toast.makeText(searchActivity.getApplicationContext(), "Request sent !",Toast.LENGTH_LONG).show();
                                                Intent i = new Intent(searchActivity , Dashboard.class);
                                                searchActivity.startActivity(i);
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                        }else{
                            Toast.makeText(searchActivity.getApplicationContext(), "user not save",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }




    @Override
    public int getItemCount() {
        return searchResult.size();
    }

    class searchViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userImage ;
        TextView userName ;
       Button join;



        public searchViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.img);
            userName =itemView.findViewById(R.id.user_namee);
            join = itemView.findViewById(R.id.join_btn);


        }
    }
}
