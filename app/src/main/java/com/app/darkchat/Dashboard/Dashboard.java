package com.app.darkchat.Dashboard;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.darkchat.R;
import com.app.darkchat.UserAdapter;
import com.app.darkchat.search_class;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {
    FirebaseAuth auth;

RecyclerView  UserRecyclerView;
UserAdapter adapter;
FirebaseDatabase database;
ArrayList<search_class> userDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        auth = FirebaseAuth.getInstance();


        database = FirebaseDatabase.getInstance();
        DatabaseReference dbReference = database.getReference("JoinRoom").child(auth.getUid());
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){



                       search_class users = snapshot1.getValue(search_class.class);
                       userDataArrayList.add(users);

                    adapter.notifyDataSetChanged();

                }


            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
        UserRecyclerView = findViewById(R.id.rcv);
        UserRecyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
        userDataArrayList = new ArrayList<>();
        adapter =new UserAdapter(Dashboard.this ,userDataArrayList);
        UserRecyclerView.setAdapter(adapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dark_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.search:
                Intent intent = new Intent(Dashboard.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.request:
                Intent iRequest = new Intent(Dashboard.this , RequestActivity.class);
                startActivity(iRequest);
                break;
            case R.id.setting:
                Intent iSetting = new Intent(Dashboard.this , SettingActivity.class);
                startActivity(iSetting);
                break;
            case R.id.logout:
                FirebaseUser user = auth.getCurrentUser();
                if (user!=null){
                    auth.getInstance().signOut();
                    Intent iLogout = new Intent(Dashboard.this , MainActivity.class);
                    startActivity(iLogout);
                }



        }
        return super.onOptionsItemSelected(item);
    }
}