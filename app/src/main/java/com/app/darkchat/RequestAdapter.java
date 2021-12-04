package com.app.darkchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.darkchat.Dashboard.RequestActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.requestViewHolder>{
Context requestActivity;
ArrayList<search_class> arrayList;

    public RequestAdapter(RequestActivity requestActivity, ArrayList<search_class> arrayList) {
        this.requestActivity = requestActivity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public requestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(requestActivity).inflate(R.layout.request_layout, parent, false);
        return  new requestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull requestViewHolder holder, int position) {

        search_class data = arrayList.get(position);
        holder.username.setText(data.username);
        Picasso.get().load(data.imgUrl).into(holder.img);
        FirebaseAuth auth  = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();


        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference reference = database.getReference("JoinRoom").child(auth.getUid());
                reference.child(data.key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){


                            // data finish
                            DatabaseReference reference = database.getReference("RequestRoom").child(auth.getUid());
                            search_class finishData = new search_class(null,null,null,null);
                            reference.child(data.key).setValue(finishData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        Toast.makeText(requestActivity.getApplicationContext(),"User added !",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });



                        }
                    }
                });
            }
        });

       holder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference reference = database.getReference("RequestRoom").child(auth.getUid());
                search_class finishData = new search_class(null,null,null,null);
                reference.child(data.key).setValue(finishData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(requestActivity.getApplicationContext(),"User rejected !",Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class requestViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView username;
        Button acceptButton , rejectButton ;
        public requestViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            username = itemView.findViewById(R.id.user_namee);
            acceptButton = itemView.findViewById(R.id.accept_btn);
            rejectButton = itemView.findViewById(R.id.reject_btn);

        }
    }
}
