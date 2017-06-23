package com.example.pelpo.flatee;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.*;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private Button changeProfile;
    private Button changePassword;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editAddress;
    private EditText editPhone;
    private TextView editDob;

    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Initializing Firebase Auth object
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid().toString());

        //Initializing date format
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        setDateTimeField();

        //initializing views
        progressDialog = new ProgressDialog(this);
        changeProfile = (Button) findViewById(R.id.changeProfile);
        changePassword = (Button) findViewById(R.id.changePassword);
        editFirstName=(EditText)findViewById(R.id.editFirstName);
        editLastName=(EditText)findViewById(R.id.editLastName);
        editAddress=(EditText)findViewById(R.id.editAddress);
        editPhone=(EditText)findViewById(R.id.editPhone);
        editDob=(TextView)findViewById(R.id.editDob);
        editDob.requestFocus();

        //attaching listeners to buttons
        changeProfile.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        editDob.setOnClickListener(this);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInformation chat = dataSnapshot.getValue(UserInformation.class);
                String result = chat.getRoomNum();
                if(result.equals("12345")){
                    editDob.setText(result);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void saveUserInformation(){
        String fname = editFirstName.getText().toString().trim();
        String lname = editLastName.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String dob = editDob.getText().toString().trim();
        String roomNum="";
        int admin=0;

        UserInformation userInformation = new UserInformation(fname, lname, dob, phone, address, roomNum, admin);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(userInformation);

        Toast.makeText(this, "Information saved", Toast.LENGTH_LONG).show();
    }



    private void setDateTimeField(){
        java.util.Calendar newCalendar = java.util.Calendar.getInstance();

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar newDate = java.util.Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editDob.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(java.util.Calendar.YEAR), newCalendar.get(java.util.Calendar.MONTH), newCalendar.get(java.util.Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View v) {
        if(v == changeProfile){
            saveUserInformation();
        }
        if(v == changePassword){
            return;
        }
        if(v == editDob) {
            toDatePickerDialog.show();
        }
    }
}
