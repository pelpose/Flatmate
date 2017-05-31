package com.example.pelpo.flatee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateRoom extends AppCompatActivity {
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        creatingChatRoom();
        startActivity(new Intent(this, Dashboard.class));
        progressDialog.dismiss();
    }

    private void creatingChatRoom() {
        String roomNum = "";
        //ChatNum chatNum = new ChatNum(roomNum);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child(user.getUid()).child("roomNum").setValue(user.getUid().toString()+"test");
    }
}
