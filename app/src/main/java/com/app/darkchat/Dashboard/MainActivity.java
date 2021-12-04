package com.app.darkchat.Dashboard;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.darkchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button Login;
    TextView newUserRegistration;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    EditText email ,password;

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        auth  = FirebaseAuth.getInstance();
       email= findViewById(R.id.user_email);
       password = findViewById(R.id.signup_pass);
        progressDialog = new ProgressDialog(this);
        Login = findViewById(R.id.signup_btn);
        newUserRegistration = findViewById(R.id.already_signin);
        newUserRegistration.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                registerNow();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(email.getText().toString())) {
                    email.setError("Fill email address");
                }
               else if (TextUtils.isEmpty(password.getText().toString())){
                    password.setError("Fill password");
                }
                else{
                    userLogin();
                }
            }
        });
    }

    private void userLogin() {
        String EmailOfUser =  email.getText().toString();
        String passwordOfUser =  password.getText().toString();




         if(passwordOfUser.length() <6 ){
            password.setError("Password length must contain at least 6 !");
        }
        else if(!isValidEmail(EmailOfUser)){
            email.setError("inValid Email");
            return;
        }


        progressDialog.setMessage("logging......");
        progressDialog.show();

            auth.signInWithEmailAndPassword(EmailOfUser, passwordOfUser)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser user = auth.getCurrentUser();
                                updateUI(user);
                                Log.d(TAG, "Successfully signIn....");
                                AuthResult result = task.getResult();
                                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                                startActivity(intent);
                                finish();

                            } else {

                                Toast.makeText(getApplicationContext(), "failed logged in..", Toast.LENGTH_LONG).show();
                                updateUI(null);
                            }
                            progressDialog.dismiss();
                        }
                    });


    }

    private void updateUI(FirebaseUser user) {

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
           reload();
        }
    }

    private void reload() {
        Intent intent  = new Intent(MainActivity.this, Dashboard.class);
        startActivity(intent);
    }


    private boolean isValidEmail(CharSequence target) {

        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    private void registerNow() {
        Intent intent = new Intent(MainActivity.this, userSingnUp.class);
        startActivity(intent);
    }
}