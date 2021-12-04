package com.app.darkchat.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.darkchat.R;
import com.app.darkchat.ModelClass.userData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {
EditText setting_name , setting_status;
Button setting_update;
CircleImageView setting_img;
FirebaseAuth auth;
FirebaseDatabase  database ;
FirebaseStorage storage;
Uri selected_imgUri;
String email , phone,password;
ProgressDialog progressDialog;
   // CropImageView cropImageView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();


        setting_img = findViewById(R.id.img);
        setting_name= findViewById(R.id.username);
        setting_status = findViewById(R.id.signup_pass);
        setting_update = findViewById(R.id.update);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();



        progressDialog = new ProgressDialog(this);
// user to  JoinRoom

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid());

        StorageReference storageReference = storage.getReference().child("image").child(auth.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {


                       email = snapshot.child("email").getValue().toString();
                    String username = snapshot.child("name").getValue().toString();
                    String status = snapshot.child("status").getValue().toString();
                    String profile_img = snapshot.child("imageUri").getValue().toString();
                      phone= snapshot.child("phone").getValue().toString();
                     password= snapshot.child("password").getValue().toString();

                    setting_name.setText(username);
                    setting_status.setText(status);
                    Picasso.get().load(profile_img).into(setting_img);





            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

        setting_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityIfNeeded(Intent.createChooser(intent, "Select Picture"), 1);



            }
        });

        setting_update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressDialog.show();
                progressDialog.setTitle("Updated");
                progressDialog.setMessage("Data is updating......");
                progressDialog.setCanceledOnTouchOutside(false);
               String name =  setting_name.getText().toString();
               String status = setting_status.getText().toString();
                if(selected_imgUri!=null){

                    storageReference.putFile(selected_imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull  Task<UploadTask.TaskSnapshot> task) {
                              storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                  @Override
                                  public void onSuccess(Uri uri) {
                                          String final_img_Uri = uri.toString();


                                         userData userData = new userData(name , email , phone,password,auth.getUid(),final_img_Uri,status);
                                         // search_class updateData = new search_class(name ,final_img_Uri,auth.getUid(),status);

                                          reference.setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                              @Override
                                              public void onComplete(@NonNull  Task<Void> task) {
                                                  if(task.isSuccessful()){
                                                      progressDialog.dismiss();
                                                      Toast.makeText(getApplicationContext(), "Data successfully uploaded", Toast.LENGTH_LONG).show();
                                                       Intent intent = new Intent(SettingActivity.this , Dashboard.class);
                                                       startActivity(intent);
                                                  }else{
                                                      Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                                                  }

                                              }
                                          });
                                  }
                              });
                        }
                    });
                }

                else{
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String final_img_Uri = uri.toString();
                            userData userData = new userData(name , email , phone,password,auth.getUid(),final_img_Uri,status);
                           // search_class updateData = new search_class(name ,final_img_Uri,auth.getUid(),status);

                            reference.setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull  Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressDialog.dismiss();

                                        Toast.makeText(getApplicationContext(), "Data successfully uploaded", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(SettingActivity.this , Dashboard.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                        }
                    });
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1){
            if(resultCode ==RESULT_OK) {
                if (data != null) {
                    selected_imgUri = data.getData();
                    setting_img.setImageURI(selected_imgUri);

                }
            }
        }

    }


}