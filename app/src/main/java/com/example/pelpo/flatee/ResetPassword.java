package com.example.pelpo.flatee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private Button btnReset;
    private TextView textLogin;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        progressDialog = new ProgressDialog(this);

        //Initializing Firebase Auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        btnReset = (Button)findViewById(R.id.btnReset);
        textLogin = (TextView)findViewById(R.id.textViewLogin);

        //attaching listeners
        btnReset.setOnClickListener(this);
        textLogin.setOnClickListener(this);
    }

    public void resetPassword(){
        String email= editTextEmail.getText().toString().trim();

        progressDialog.setMessage("Sending email...");
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassword.this,"Email sent", Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        } else {
                            Toast.makeText(ResetPassword.this,"Failed to send reset email. Contact our support team please", Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v == btnReset){
            resetPassword();
            finish();
            startActivity(new Intent(this, LogInActivity.class));
        }
        if(v == textLogin){
            finish();
            startActivity(new Intent(this, LogInActivity.class));
        }
    }
}
