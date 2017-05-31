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
    private EditText editEmail;
    private EditText editPass;
    private TextView signIn;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        if(firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
        }

        btnReg = (Button) findViewById(R.id.btnRegi);
        editEmail=(EditText) findViewById(R.id.editTextEmail);
        editPass=(EditText)findViewById(R.id.editPassword);
        signIn=(TextView)findViewById(R.id.textViewSignIn);

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
        String roomNum = "12345";
        ChatNum chatNum = new ChatNum(roomNum);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(chatNum);
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
