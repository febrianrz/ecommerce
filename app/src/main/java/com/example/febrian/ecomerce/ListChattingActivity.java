package com.example.febrian.ecomerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.febrian.ecomerce.Adapter.UsersAdapter;
import com.example.febrian.ecomerce.Response.Produk;
import com.example.febrian.ecomerce.Response.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListChattingActivity extends AppCompatActivity {
    private RecyclerView rvUsers;
    private RecyclerView.Adapter recylerAdapter;
    private RecyclerView.LayoutManager recylerLayoutManager;
    List<UserModel> arrData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chatting);
        rvUsers     = (RecyclerView) findViewById(R.id.rvUsers);
        setList();
    }

    private void setList(){
        loadData();
        recylerLayoutManager =  new LinearLayoutManager(this);
        rvUsers.setHasFixedSize(true);
        rvUsers.setLayoutManager(recylerLayoutManager);
        recylerAdapter =  new UsersAdapter(this,arrData);
        rvUsers.setAdapter(recylerAdapter);
    }

    private void loadData(){
        DatabaseReference listUser = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("users");
        listUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(ListChattingActivity.this,"execute",Toast.LENGTH_SHORT).show();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = (String) ds.getKey();
                    DatabaseReference keyReference = FirebaseDatabase.getInstance()
                            .getReference().child("users")
                            .child(key);
                    keyReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String email =  dataSnapshot.child("email").getValue(String.class);
                            String guid = dataSnapshot.child("guid").getValue(String.class);
                            if(!FirebaseAuth.getInstance().getUid().equals(guid)) {
                                UserModel userModel = new UserModel();
                                userModel.setEmail(email);
                                userModel.setGuid(guid);
                                arrData.add(userModel);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
