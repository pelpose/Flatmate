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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity implements TextView.OnClickListener{

    private Button signIn;
    private EditText editEmail;
    private EditText editPass;
    private EditText editEmailFunction;
    private EditText editFirstName;
    private TextView signUp;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    /**
     * Creates signIn, editEmail, editPassword, signUp
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
        }

        progressDialog = new ProgressDialog(this);

        signIn = (Button) findViewById(R.id.btnLogIn);
        editEmail = (EditText) findViewById(R.id.editTextEmail);
        editPass= (EditText) findViewById(R.id.editPassword);
        editFirstName = (EditText) findViewById(R.id.editFirstName);
        signUp = (TextView) findViewById(R.id.textViewSignUp);

        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    /**
     * Let users to log in by providing their email and password
     */
    private void userLogin() {
        String email= editEmail.getText().toString().trim();
        String password = editPass.getText().toString().trim();
        String emailFunction = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; // email function in abcd@ format.
        if(TextUtils.isEmpty(email)) {
            //email is empty
            Toast.makeText(this, "Please enter your Email", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }
        if(!email.matches(emailFunction)){
            //Written in email format
            Toast.makeText(this, "Type your email address correctly",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(emailFunction)){
            //Email is empty
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT);
            //stopping the function execution further
            return;
        }
        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Please enter your Password",Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        } else if(password.length() <6 ) {
            //password length should be longer than 5
            Toast.makeText(this, "Your password is too short",Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }

        //if validations are ok
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if(task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                }
            }
        });
    }

    /**
     * Clicking the button let users to sign in and sign up.
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(v == signIn){
            userLogin();
        }
        if(v == signUp){
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
