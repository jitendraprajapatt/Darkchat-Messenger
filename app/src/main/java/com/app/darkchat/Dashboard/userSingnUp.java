package com.app.darkchat.Dashboard;


import android.app.ProgressDialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.app.darkchat.R;
import com.app.darkchat.search_class;
import com.app.darkchat.ModelClass.userData;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import de.hdodenhof.circleimageview.CircleImageView;


public class userSingnUp extends AppCompatActivity {

    EditText username , email, number , password,cPassword ;
    Button signUp , singIn;

    ProgressDialog progressDialog;
    private static final String TAG = "userSingnUp";
    FirebaseAuth auth ;
   FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseStorage storage;


    String Name , EmailId ,phone,pass ,key ,imageUrl;
    String status = "hey I'm using Darkchat";
    String EmailOfUser;
    String passwordOfUser;

    CircleImageView img;

    Uri imgUri ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_singn_up);
        getSupportActionBar().hide();
        img = findViewById(R.id.img);
        username = findViewById(R.id.sigup_name);
        email = findViewById(R.id.signup_email);
        number =findViewById(R.id.signup_phone);
        password= findViewById(R.id.signup_pass);
        cPassword = findViewById(R.id.signup_cpass);
        signUp = findViewById(R.id.signup_btn);
        singIn = findViewById(R.id.already_signin);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        storage = FirebaseStorage.getInstance();
        singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userSingnUp.this, MainActivity.class);
                startActivity(intent);
            }
        });


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        progressDialog = new ProgressDialog(this);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRegistration();


            }
        });
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityIfNeeded(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1){
            if(resultCode ==RESULT_OK) {
                if (data != null) {
                    imgUri = data.getData();
                  img.setImageURI(imgUri);
                }
            }
        }

    }

    private void UserRegistration() {
        EmailOfUser =  email.getText().toString();
        passwordOfUser =  password.getText().toString();
        String cPasswordOfUser = cPassword.getText().toString();
        if (TextUtils.isEmpty(EmailOfUser)) {
            email.setError("fill email !");

        }
        else if(TextUtils.isEmpty(passwordOfUser)){
            password.setError("Fill password");
        }
        else if(TextUtils.isEmpty(cPasswordOfUser)){
            cPassword.setError("Fill password");
        }
        else if (!passwordOfUser.equals(cPasswordOfUser)){
            cPassword.setError("Check password !");
        }
        else if(passwordOfUser.length() <6 ){
            password.setError("Password length must contain at least 6 !");
        }
        else if(!isValidEmail(EmailOfUser)){
            email.setError("inValid Email");
            return;
        }

        progressDialog.setTitle("SignUp");
        progressDialog.setMessage("please wait......");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(EmailOfUser, passwordOfUser)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            key = auth.getUid();
                            Name = username.getText().toString();
                            EmailId = email.getText().toString();
                            phone = number.getText().toString();
                            pass = password.getText().toString();



                            reference = database.getReference("Users").child(key);
                                StorageReference storageReference = storage.getReference("image/" ).child(key);


                            if(imgUri!=null) {

                                storageReference.putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull  Task<UploadTask.TaskSnapshot> task) {
                                        if(task.isSuccessful()) {
                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                   imageUrl= uri.toString();
                                                    userData userObj = new userData(Name ,EmailId,phone,pass,key,imageUrl,status);
                                                    reference.setValue(userObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull  Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                progressDialog.dismiss();
                                                                reference = database.getReference("JoinRoom").child(auth.getUid()).child(auth.getUid());
                                                                search_class addUser = new search_class(Name ,imageUrl,auth.getUid(),status);
                                                                reference.setValue(addUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Intent intent= new Intent(userSingnUp.this,MainActivity.class);
                                                                            startActivity(intent);
                                                                        }
                                                                    }
                                                                });


                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }

                                    }
                                });
                            }else{
                                status = "hey I'm using Darkchat";
                                imageUrl= "https://firebasestorage.googleapis.com/v0/b/darkchat-46eb1.appspot.com/o/user.png?alt=media&token=ca4f56f1-91d3-4e03-b1b9-3eadf23a7944";
                                userData userObj = new userData(Name ,EmailId,phone,pass,key,imageUrl,status);
                                reference.setValue(userObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull  Task<Void> task) {
                                        if (task.isSuccessful()){
                                            progressDialog.dismiss();
                                            reference = database.getReference("JoinRoom").child(auth.getUid()).child(auth.getUid());
                                            search_class addUser = new search_class(Name ,imageUrl,auth.getUid(),status);
                                            reference.setValue(addUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Intent intent= new Intent(userSingnUp.this,MainActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }

                            Log.d(TAG, "Successfully signed in with email link!");
                            AuthResult result = task.getResult();



                        } else {
                            Log.e(TAG, "Error signing in with email link", task.getException());
                            updateUI(null);
                        }

                    }
                });



    }




    private void updateUI(FirebaseUser user) {

    }

    private boolean isValidEmail(CharSequence target) {

        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}