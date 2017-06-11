package com.example.pelpo.flatee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Calendar extends AppCompatActivity {

    private String roomNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuforcalendar, menu);
        setRoomNum();
        return true;
    }


    public void setRoomNum() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            roomNum = extras.getString("key");
            //The key argument here must match that used in the other activity
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_chatroom) {
            Intent i = new Intent(getApplicationContext(), ChatRoom.class);

            i.putExtra("key",roomNum);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
