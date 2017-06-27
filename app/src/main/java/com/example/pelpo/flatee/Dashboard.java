package com.example.pelpo.flatee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity implements View.OnClickListener{

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private Button btnLogOut;
    private Button btnCreate;
    private TextView displayName;

    private String roomNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid().toString());

        btnCreate = (Button) findViewById(R.id.btnCreateRoom);
        //btnLogOut =(Button) findViewById(R.id.btnLogOut);
        displayName =(TextView) findViewById(R.id.name);

        btnCreate.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        displayName.setOnClickListener(this);

        progressDialog.setMessage("Loading...");
        progressDialog.show();



        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ChatNum chatNum = dataSnapshot.getValue(ChatNum.class);
                String result = chatNum.getRoomNum();
                if(result.equals("12345")){
                    displayName.setText(result);
                    progressDialog.dismiss();
                }else if (!result.equals("12345")) {
                    setRoomNum(result);
                    navigate();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void setRoomNum(String nNum) {this.roomNum =nNum;}

    public void navigate() {
        Intent i = new Intent(getApplicationContext(), ChatRoom.class);

        i.putExtra("key",roomNum);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_logout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,LogInActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v == btnCreate){
            startActivity(new Intent(this,CreateRoom.class));
        }
        if(v == btnLogOut){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,LogInActivity.class));
        }
    }
}
