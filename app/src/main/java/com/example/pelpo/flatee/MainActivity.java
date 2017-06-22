package com.example.pelpo.flatee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnReg;
    private TextView signIn;
    private EditText editEmail;
    private EditText editPass;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editAddress;
    private EditText editPhone;
    private EditText editDob;


    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing Firebase Auth object
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        //Checking if the user exists
        if(firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
        }


        //initializing views
        progressDialog = new ProgressDialog(this);
        btnReg = (Button) findViewById(R.id.btnRegi);
        editEmail=(EditText) findViewById(R.id.editTextEmail);
        editPass=(EditText)findViewById(R.id.editPassword);
        signIn=(TextView)findViewById(R.id.textViewSignIn);
        editFirstName=(EditText)findViewById(R.id.editFirstName);
        editLastName=(EditText)findViewById(R.id.editLastName);
        editAddress=(EditText)findViewById(R.id.editAddress);
        editPhone=(EditText)findViewById(R.id.editPhone);
        editDob=(EditText)findViewById(R.id.editDob);

        //attaching listeners to buttons
        btnReg.setOnClickListener(this);
        signIn.setOnClickListener(this);
    }

    private void register() {
        String email= editEmail.getText().toString().trim();
            String password = editPass.getText().toString().trim();

            if(TextUtils.isEmpty(email)){
                //email is empty
                Toast.makeText(this, "Please enter Email",Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }
            if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Please enter Password",Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }
        //if validations are ok
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //user is Successfully Registered
                    saveUserInformation();
                    finish();
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));

                    //user registered successfully
                    Toast.makeText(MainActivity.this,"Registered Successfully", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                } else{
                    //user registration failed
                    Toast.makeText(MainActivity.this,"Could not register", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                }
            }
        });
    }


    private void saveUserInformation() {
        //Creating string for saving user information
        String fname = editFirstName.getText().toString().trim();
        String lname = editLastName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String dob = editDob.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String roomNum = "12345";
        int admin = 0;

        //Creating required objects
        ChatNum chatNum = new ChatNum(roomNum);
        UserInformation userInformation = new UserInformation(fname, lname, phone, dob, address, roomNum, admin);

        //Fetching current user's information
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //Inserting user information to the Firebase database
        //databaseReference.child(user.getUid()).setValue(chatNum);
        databaseReference.child(user.getUid()).setValue(userInformation);
    }

    @Override
    public void onClick(View v) {
        if(v == btnReg){
            register();
        }
        if(v == signIn){
            startActivity(new Intent(this, LogInActivity.class));
        }
    }
}
