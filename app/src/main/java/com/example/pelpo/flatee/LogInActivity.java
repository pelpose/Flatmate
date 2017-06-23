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
    private TextView signUp;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

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
        signUp = (TextView) findViewById(R.id.textViewSignUp);

        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    private void userLogin() {
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
        progressDialog.setMessage("Loging in...");
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
