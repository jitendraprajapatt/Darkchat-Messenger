package com.app.darkchat.Dashboard;

import android.os.Bundle;

import com.app.darkchat.R;
import com.app.darkchat.RequestAdapter;
import com.app.darkchat.search_class;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestActivity extends AppCompatActivity {
RecyclerView rcv;
FirebaseAuth auth;
FirebaseDatabase database;
ArrayList<search_class> arrayList;
    private com.app.darkchat.RequestAdapter RequestAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        rcv = findViewById(R.id.rcv);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("RequestRoom").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    search_class fetchRequestData =  snapshot1.getValue(search_class.class);
                    arrayList.add(fetchRequestData);


                }
                RequestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rcv.setLayoutManager(new LinearLayoutManager(RequestActivity.this));
        arrayList = new ArrayList<>();
        RequestAdapter= new RequestAdapter(RequestActivity.this , arrayList);
        rcv.setAdapter(RequestAdapter);
    }

}