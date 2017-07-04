package com.example.pelpo.flatee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Created by Reno on 25/06/2017.
 * Flat admin can send an invitation to a selected email in this activity
 */

public class InviteActivity extends AppCompatActivity implements View.OnClickListener {

    private Button  btnInvite;
    private Button    btnBack;
    private EditText etFlatee;

    private String roomName;

    private static final String EXTRA_MESSAGE = "Invite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        btnInvite= (Button) findViewById(R.id.btnInvite);
        btnBack= (Button) findViewById(R.id.btnBack);
        etFlatee=(EditText)findViewById(R.id.etFlatee);

        //attaching listeners to buttons
        btnInvite.setOnClickListener(this);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        roomName = intent.getStringExtra(ChatRoom.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(roomName);
    }

    private void sendInvite() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            //Inviter's Uid
            //roomName = Uid + test
            String inviterid = user.getUid();


            EditText invitedFlatee= (EditText) findViewById(R.id.etFlatee);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

            Query lookForUser = ref.child("User").orderByChild("email").equalTo(invitedFlatee.toString());

            /*
            lookForUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // dataSnapshot is the "User" node with all children with email equal to the invited
                        UserInformation userInfo = dataSnapshot.getValue(UserInformation.class);

                        TextView flatee = (TextView) findViewById(R.id.tvInvited);
                        flatee.setText("dataSnapshot");

                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            // do something with the individual "User"
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            */

            //Gets user reference for the database
            DatabaseReference usersRef = ref.child("User");

            //Searches and inputs datas into a directory, (directory/target, message)
            Map<String, Object> userUpdates = new HashMap<String, Object>();
            userUpdates.put("/roomNum", inviterid + "test");

            usersRef.updateChildren(userUpdates);

            //DatabaseReference dbr = FirebaseDatabase.getInstance().getReference(invitedFlatee.getText().toString());

            //String isInvited = "YES";
            //dbr.child().child("email").setValue(isInvited);
            //Intent intent = new Intent(this, ChatRoom.class);
            //String invitation = invitedFlatee.getText().toString();
            //intent.putExtra(roomName, invitation);
            //startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == btnInvite){
            sendInvite();
        }
        if(v == btnBack){
            finish();
        }
    }

}