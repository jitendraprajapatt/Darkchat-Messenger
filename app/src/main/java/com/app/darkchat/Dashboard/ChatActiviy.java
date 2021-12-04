package com.app.darkchat.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.darkchat.MessagesAdapter;
import com.app.darkchat.ModelClass.Msg;
import com.app.darkchat.R;
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
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActiviy extends AppCompatActivity {

    String ReceiverName ,ReceiverImage , ReceiverUid;
    TextView Name;
    CircleImageView UserImage;
    FirebaseAuth auth;
    FirebaseDatabase database;
    public static String sImage;
    public static String rImage;
    EditText Emessage;
    ImageView send;
    RecyclerView recyclerview;
    String sUid;
    String senderRoom , receiverRoom;
    RecyclerView messageAdapter;
    ArrayList<Msg> messagesArraylistt;
    MessagesAdapter Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_activiy);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Name = findViewById(R.id.Name);
        UserImage = findViewById(R.id.img);
        Emessage = findViewById(R.id.edit_message);
        send = findViewById(R.id.sent_msg);


        recyclerview = findViewById(R.id.rcv_msg);



        ReceiverName = getIntent().getStringExtra("Name");
        ReceiverImage = getIntent().getStringExtra("ReceiverImage");

        ReceiverUid = getIntent().getStringExtra("Uid");
        sUid = auth.getUid();
        senderRoom =  sUid + ReceiverUid;
        receiverRoom = ReceiverUid + sUid;
        messagesArraylistt  = new ArrayList<>();

        Adapter = new MessagesAdapter(ChatActiviy.this , messagesArraylistt);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setStackFromEnd(true);
        recyclerview.setLayoutManager(linearLayout);
        recyclerview.setAdapter(Adapter);



        Name.setText(ReceiverName);
        Picasso.get().load(ReceiverImage).into(UserImage);

        DatabaseReference reference = database.getReference().child("Users").child(auth.getUid());
        DatabaseReference chatsReference = database.getReference().child("chats").child(senderRoom).child("message");
        chatsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                messagesArraylistt.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                    Msg messages = dataSnapshot.getValue(Msg.class);
                    messagesArraylistt.add(messages);
                    recyclerview.smoothScrollToPosition(messagesArraylistt.toArray().length-1);
                }
                Adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sImage = snapshot.child("imageUri").getValue().toString();
                rImage = ReceiverImage;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = Emessage.getText().toString();
                if(message.isEmpty()){
                    Toast.makeText(getApplicationContext(),"please write something...",Toast.LENGTH_LONG).show();
                return ;
                }
                else{
                    Emessage.setText("");
                    Date date = new Date();
                    Msg messageObj = new  Msg(message,sUid,date.getTime());
                    database.getReference().child("chats")
                            .child(senderRoom)
                            .child("message")
                            .push()
                            .setValue(messageObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull  Task<Void> task) {

                            database.getReference().child("chats")
                                    .child(receiverRoom)
                                    .child("message")
                                    .push()
                                    .setValue(messageObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull  Task<Void> task) {

                                }
                            });
                        }
                    });

                }
            }
        });
    }
}