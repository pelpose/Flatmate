package com.example.pelpo.flatee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.support.design.widget.FloatingActionButton;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.view.Menu;
import android.view.MenuItem;

public class ChatRoom extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference UserDB;
    private FirebaseListAdapter<ChatMessage> adapter;

    RelativeLayout chat_room;
    FloatingActionButton fab;

    private String roomNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        chat_room =(RelativeLayout)findViewById(R.id.activity_main);

        setRoomNum();

        fab =(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference().child("ChatRoom").child(roomNum).push().setValue(new ChatMessage(input.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input.setText("");
            }

        });

        UserDB = FirebaseDatabase.getInstance().getReference("User");
        firebaseAuth = FirebaseAuth.getInstance();

        displayChatMessage();
    }



    public void setRoomNum() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            roomNum = extras.getString("key");
            //The key argument here must match that used in the other activity
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuforchatroom, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_changeProfile) {
            startActivity(new Intent(this,Profile.class));
        }

        if (id == R.id.action_calendar) {
            Intent i = new Intent(getApplicationContext(), Calendar.class);

            i.putExtra("key",roomNum);
            startActivity(i);
        }

        if (id == R.id.action_logout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,LogInActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    private void displayChatMessage() {
        ListView listOfMessage = (ListView)findViewById(R.id.list_of_message);
        adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.list_item,FirebaseDatabase.getInstance().getReference().child("ChatRoom").child(roomNum)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                //Get Referernce to the views of list_item.xml
                TextView messageText,messageUser,messageTime;
                messageText =(TextView)v.findViewById(R.id.message_text);
                messageTime =(TextView)v.findViewById(R.id.message_time);
                messageUser =(TextView)v.findViewById(R.id.message_user);

                messageText.setText(model.getMessageText());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getMessageTime()));
                messageUser.setText(model.getMessageUser());
            }
        };
        listOfMessage.setAdapter(adapter);
    }
}
