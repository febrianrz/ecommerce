package com.example.febrian.ecomerce;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.febrian.ecomerce.Libraries.Auth;
import com.example.febrian.ecomerce.Response.ChatModel;
import com.example.febrian.ecomerce.Response.UserModel;
import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.util.ChatBot;
import com.github.bassaer.chatmessageview.view.ChatView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class ChattingActivity extends AppCompatActivity {
    private ChatView mChatView;
    private String myId, friendId, myName, friendEmail;
    private final String FIREBASE_CHAT_OBJECT = "chat_user";
    private ArrayList<String> idPesan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        Intent i = getIntent();
        friendEmail  = i.getStringExtra("email_friend");
        friendId     = i.getStringExtra("guid_friend");
        getSupportActionBar().setTitle(friendEmail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mChatView = (ChatView) findViewById(R.id.chat_view);
        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500));
        mChatView.setLeftBubbleColor(Color.WHITE);
        mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.blueGray500));
//        mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.cyan500));
        mChatView.setSendIcon(R.drawable.ic_action_send);
        mChatView.setRightMessageTextColor(Color.WHITE);
        mChatView.setLeftMessageTextColor(Color.BLACK);
        mChatView.setUsernameTextColor(Color.WHITE);
        mChatView.setSendTimeTextColor(Color.WHITE);
        mChatView.setDateSeparatorColor(Color.WHITE);
        mChatView.setInputTextHint("new message...");
        mChatView.setMessageMarginTop(5);
        mChatView.setMessageMarginBottom(5);

        //Click Send Button
        mChatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToFirebase(mChatView.getInputText().toString());
                mChatView.setInputText("");
            }
        });
        getDataFromFirebase();
    }

    private void getDataFromFirebase(){
        final UserModel me = new UserModel();
        DatabaseReference chatUser = FirebaseDatabase.getInstance().getReference().child(FIREBASE_CHAT_OBJECT);
        chatUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                  String key = (String) ds.getKey();
                  if(!idPesan.contains(key)){
                      idPesan.add(key);
                      DatabaseReference keyReference = FirebaseDatabase.getInstance()
                              .getReference().child(FIREBASE_CHAT_OBJECT)
                              .child(key);
                      keyReference.addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(DataSnapshot dataSnapshot) {
                              String pesan = dataSnapshot.child("messageText").getValue(String.class);
                              String userFrom = dataSnapshot.child("messageUserId").getValue(String.class);
                              long time = dataSnapshot.child("messageTime").getValue(Long.class);
                              Calendar calendar = Calendar.getInstance();
                              calendar.setTimeInMillis(time);
                              Message message = new Message.Builder()
                                      .setSendTime(calendar)
                                      .setText(pesan)
                                      .hideIcon(true)
                                      .build();
                              if(FirebaseAuth.getInstance().getUid().equals(userFrom)){
                                  UserModel userModel = new UserModel();
                                  userModel.setEmail(dataSnapshot.child("messageUser").getValue(String.class));
                                  message.setUser(userModel);
                                  message.setRight(true);
                              } else {
                                  UserModel userModel = new UserModel();
                                  userModel.setEmail(dataSnapshot.child("messageUserFriend").getValue(String.class));
                                  message.setUser(userModel);
                                  message.setRight(false);
                              }
                              mChatView.send(message);
                          }

                          @Override
                          public void onCancelled(DatabaseError databaseError) {

                          }
                      });
                  }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendToFirebase(String pesan){
        Toast.makeText(this,pesan,Toast.LENGTH_SHORT).show();
        FirebaseDatabase.getInstance()
                .getReference()
                .child(FIREBASE_CHAT_OBJECT)
                .push()
                .setValue(new ChatModel(pesan,
                        FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                        FirebaseAuth.getInstance().getUid(),
                        0,friendId,friendEmail));
    }
}
