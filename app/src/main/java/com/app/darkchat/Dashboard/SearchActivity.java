package com.app.darkchat.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.darkchat.R;
import com.app.darkchat.SearchAdapter;
import com.app.darkchat.search_class;
import com.app.darkchat.ModelClass.userData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    EditText search_keyword;
    ImageView searchBtn;
    FirebaseAuth auth;
    FirebaseDatabase database;
    int positionOfUser ;
    RecyclerView UserRecyclerView;
    ArrayList<search_class> searchResult;
    SearchAdapter searchAdapter;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_keyword = findViewById(R.id.keyword);
        searchBtn = findViewById(R.id.searchBtn);
        Context context;
        progressDialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_keyword.getText().toString().isEmpty() || search_keyword.length()!=10) {

                    search_keyword.setError("please enter phone number !");

                }else {
                    progressDialog.setMessage("Please wait......");
                    progressDialog.show();
                    searchByEmail();
                }
            }
        });
    }

    private void searchByEmail() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference dbReference = database.getReference("Users");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()) {


                    userData users = snapshot1.getValue(userData.class);
                    String sKeyWord = search_keyword.getText().toString();
                    List<String> fKeyword = Collections.singletonList(users.getPhone());



                    for (int i = 0; i < fKeyword.size(); i++) {
                        if (sKeyWord.equals(fKeyword.get(i))) {

                            progressDialog.dismiss();
                            positionOfUser = i;
                            List<String> userName = Collections.singletonList(users.getName());
                            List<String> imgUrl = Collections.singletonList(users.getImageUri());
                            List<String> key = Collections.singletonList(users.getKey());
                            List<String> status = Collections.singletonList(users.getStatus());
                            search_class searchData = new search_class(userName.get(positionOfUser), imgUrl.get(positionOfUser),key.get(positionOfUser),status.get(positionOfUser));
                            searchResult.add(searchData);
                           // i = fKeyword.size() + 1;


                            break;

                        } else {

                            progressDialog.dismiss();
                            // search_keyword.setError("user not fond !");
                            //Toast.makeText(getApplicationContext(), "user not found", Toast.LENGTH_LONG).show();
                        }
                    }




                }
                searchAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
        UserRecyclerView = findViewById(R.id.s_rcv);
        UserRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
       searchResult = new ArrayList<>();
        searchAdapter =new SearchAdapter(SearchActivity.this , searchResult);
        UserRecyclerView.setAdapter(searchAdapter);
    }
}