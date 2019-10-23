package com.example.fyp.App;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText emailtxt;
    EditText passwordtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        emailtxt = findViewById(R.id.rEmail);
        final String email = emailtxt.getText().toString();
        mAuth = FirebaseAuth.getInstance();

        TextView tvSignIn = findViewById(R.id.rLink);
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!email.isEmpty()) {
                    Intent i = new Intent(RegisterActivity.this, LogInActivity.class);
                    startActivity(i);

                    i.putExtra("email", email);

                } else {
                    Intent i = new Intent(RegisterActivity.this, LogInActivity.class);
                    startActivity(i);
                }


            }
        });

    }

    public void register(View view) {

        emailtxt = findViewById(R.id.rEmail);
        passwordtxt = findViewById(R.id.rPassword);


        String email = emailtxt.getText().toString();
        String pw = passwordtxt.getText().toString();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (email.isEmpty()) {

            emailtxt.setError("Please enter an email");
            emailtxt.requestFocus();
        } else if (pw.isEmpty()) {
            passwordtxt.setError("Please enter password");
            passwordtxt.requestFocus();
        } else if (email.isEmpty() && pw.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please Enter an Email and Password to Register", Toast.LENGTH_LONG).show();
        } else if (!((email.isEmpty() && pw.isEmpty()))) {
            Log.d("", "createAccount:" + email);
            mAuth = FirebaseAuth.getInstance();

            // [START create_user_with_email]
            mAuth.createUserWithEmailAndPassword(email, pw)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Log.d("", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(i);

                            } else {
                                Log.w("", "createUserWithEmail:failure", task.getException());

                                Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();

                            }


                        }
                    });

        }
    }


}


