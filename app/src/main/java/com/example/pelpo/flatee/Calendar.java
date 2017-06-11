package com.example.pelpo.flatee;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

public class Calendar extends AppCompatActivity {

    private String roomNum;
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        setRoomNum();
        showCalender();
    }

    private void showCalender() {
        calendarView =(CalendarView) findViewById(R.id.calendar);
        calendarView.setShowWeekNumber(false);
        calendarView.setFirstDayOfWeek(2);
        calendarView.setBackgroundColor(getResources().getColor(R.color.green));
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(getApplicationContext(),dayOfMonth+"/"+month+"/"+year, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuforcalendar, menu);
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
