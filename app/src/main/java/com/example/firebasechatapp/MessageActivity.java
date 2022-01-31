package com.example.firebasechatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import adapter.Chat;
import adapter.MessageAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import model.User;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    RecyclerView recyclerView;

    FirebaseUser mUser;
    DocumentReference reference;

    MessageAdapter adapter;
    List<Chat> mChat;

    ImageButton imageButton;
    EditText editText;

    Intent intent;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MessageActivity.this, MainPart.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.the_username);
        imageButton = findViewById(R.id.btn_send);
        recyclerView = findViewById(R.id.recycler_view);
        editText = findViewById(R.id.message_send);

        intent = getIntent();
         userId = intent.getStringExtra("userId");
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);




        mUser = FirebaseAuth.getInstance().getCurrentUser();
        assert userId != null;
        reference = FirebaseFirestore.getInstance().collection("User").document(userId);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    User user = task.getResult().toObject(User.class);
                    assert user != null;
                    username.setText(user.getUsername());
                    if (user.getImageURL().equals("default")){
                        profile_image.setImageResource(R.mipmap.ic_launcher);
                    }else{
                        Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                    }

                    readMessage(mUser.getUid(), userId, user.getImageURL());
                }else{
                    Toast.makeText(MessageActivity.this, "Error opening activity", Toast.LENGTH_SHORT).show();
                }
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = editText.getText().toString();
                if (!msg.equals("")){
                    sendMessage(mUser.getUid(), userId, msg);
                }else {
                    Toast.makeText(MessageActivity.this, "You cant send empty message", Toast.LENGTH_SHORT).show();
                }
                editText.setText("");
            }
        });

        seenMessage(userId);
    }

    private void sendMessage(String sender, String receiver, String message){
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Chat").document();

        Map<String, Object> map = new HashMap<>();
        map.put("sender", sender);
        map.put("receiver", receiver);
        map.put("message", message);
        map.put("isseen", false);

        documentReference.set(map);

        final DocumentReference doc1 = FirebaseFirestore.getInstance().collection("Chatlist").document();
        final DocumentReference doc2 = FirebaseFirestore.getInstance().collection("Chatlist").document();
        Map<String, Object> maps1 = new HashMap<>();
        maps1.put("id", mUser.getUid());
                  doc1.set(maps1);


        Map<String, Object> maps2 = new HashMap<>();
        maps2.put("id", userId);
        doc2.set(maps2);
}

    private void seenMessage(final String userId){
        final CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Chat");
        collectionReference.get().addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                    Chat chat = documentSnapshot.toObject(Chat.class);

                    if (chat.getReceiver().equals(mUser.getUid()) && chat.getSender().equals(userId)){
                        reference.update("isseen", true);
                    }
                }
            }
        });
    }

    private void readMessage(final String myid, final String userid, final String imageUrl){
        mChat = new ArrayList<>();

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Chat");

        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                mChat.clear();
                assert queryDocumentSnapshots != null;

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Chat chat = documentSnapshot.toObject(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mChat.add(chat);
                    }
                }
                adapter = new MessageAdapter(MessageActivity.this, mChat, imageUrl);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}