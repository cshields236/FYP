package com.example.fyp.App;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dmax.dialog.SpotsDialog;

public class LogInActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    public FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mAuth;
    EditText emailtxt;
    EditText passwordtxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Intent i = getIntent();

        if (i == null) {
            String e = i.getStringExtra("email");
            emailtxt.setText(e);

            TextView register = findViewById(R.id.lLink);


            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(LogInActivity.this, RegisterActivity.class);
                    startActivity(i);
                }
            });
        } else {


            TextView register = findViewById(R.id.lLink);


            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(LogInActivity.this, RegisterActivity.class);
                    startActivity(i);
                }
            });
        }

    }

    public void signIn(View view) {
        mAuth = FirebaseAuth.getInstance();


        mFirebaseAuth = FirebaseAuth.getInstance();
        emailtxt = findViewById(R.id.lEmail);
        passwordtxt = findViewById(R.id.lPassword);

        String email = emailtxt.getText().toString();
        String password = passwordtxt.getText().toString();
        // waiting = new SpotsDialog.Builder().setContext(this).setMessage("Logging In").setCancelable(false).build();
        if (email.isEmpty()) {

            emailtxt.setError("Please enter an email");
            emailtxt.requestFocus();
        } else if (password.isEmpty()) {
            passwordtxt.setError("Please enter password");
            passwordtxt.requestFocus();
        } else if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(LogInActivity.this, "Please Enter an Email and Password to Sign In", Toast.LENGTH_LONG).show();
        } else if (!((email.isEmpty() && password.isEmpty()))) {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        Log.d("", "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        startActivity(new Intent(LogInActivity.this, DashboardActivity.class));
                    } else {
                        Log.w("", "signInWithEmail:failure", task.getException());
                        Toast.makeText(LogInActivity.this, "Authentication failed." + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }

                }

            });
        }
    }

}
