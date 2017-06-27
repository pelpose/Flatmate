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

    /**
     * Brings user's information into firebase database
     * @param savedInstanceState
     */
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

    /**
     * Users can register by putting their email and password
     */
    private void register() {
        String email= editEmail.getText().toString().trim();
        String emailFunction = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; // email function in abcd@ format.
        String password = editPass.getText().toString().trim();
        String fName = editFirstName.getText().toString().trim();
        String fNameFunction = "[a-zA-z]+";
        String lName = editLastName.getText().toString().trim();
        String lNameFunction = "[a-zA-z]+";
        String address = editAddress.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String dob = editDob.getText().toString().trim();
        String dobPattern = "^([0-9]{2})-([0-9]{2})-([0-9]{4})$";

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter your Email",Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }
        if(!email.matches(emailFunction)){
            //Written in email format
            Toast.makeText(this, "Type your email address correctly",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Please enter your Password",Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        } else if(password.length() <6 ) {
            //password length should be longer than 5
            Toast.makeText(this, "Your password should be longer than 5",Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }
        if(TextUtils.isEmpty(fName)){
            //first name is empty
            Toast.makeText(this, "Please type your first name", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        } else if(!fName.matches(fNameFunction)){
            //Only alphabet letters are allowed
            Toast.makeText(this, "Your first name should be alphabet letters", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }

        if(TextUtils.isEmpty(lName)){
            //last name is empty
            Toast.makeText(this, "Please type your last name", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        } else if(!lName.matches(lNameFunction)){
            //Only alphabet letters are allowed
            Toast.makeText(this, "Your last name should be alphabet letters", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }
        if(TextUtils.isEmpty(address)){
            //Address is empty
            Toast.makeText(this, "Please type your address", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }
        if(TextUtils.isEmpty(phone)){
            //phone number is empty
            Toast.makeText(this, "Please type your phone number", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        } else if(phone.length() <4) {
            // phone number length should be longer than 4
            Toast.makeText(this, "Your phone number is too short", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        } else if(phone.length() >12) {
            //phone number length should be longer than 12
            Toast.makeText(this, "Your phone number is too long", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }
        if(TextUtils.isEmpty(dob)){
            //Date of birth is empty
            Toast.makeText(this, "Please type your Date of Birth", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        } else if(!dob.matches(dobPattern)){
            //Date of Birth should be in dd/mm/yyyy format
            Toast.makeText(this, "Write your Date of Birth in dd-mm-yyyy format", Toast.LENGTH_SHORT).show();
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

    /**
     * Saves user's first name, last name, phone number, and address
     */
    private void saveUserInformation() {
        //Creating string for saving user information
        String fname = editFirstName.getText().toString().trim();
        String lname = editLastName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String dob = editDob.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String roomNum = "12345";

        //Creating required objects
        ChatNum chatNum = new ChatNum(roomNum);
        UserInformation userInformation = new UserInformation(fname, lname, phone, dob, address, roomNum);

        //Fetching current user's information
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //Inserting user information to the Firebase database
        //databaseReference.child(user.getUid()).setValue(chatNum);
        databaseReference.child(user.getUid()).setValue(userInformation);
    }

    /**
     * Clicking the button let user to register and sign in.
     * @param v
     */
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
