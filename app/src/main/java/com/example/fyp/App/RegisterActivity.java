package com.example.fyp.App;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        final EditText emailtxt = findViewById(R.id.rEmail);
        final EditText passwordtxt = findViewById(R.id.rPassword);
        Button registerbtn = findViewById(R.id.rButton);
        TextView tvSignIn = findViewById(R.id.rLink);



        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailtxt.getText().toString();
                String pw = passwordtxt.getText().toString();

                if (email.isEmpty()) {

                    emailtxt.setError("Please enter an email");
                    emailtxt.requestFocus();
                } else if (pw.isEmpty()) {
                    passwordtxt.setError("Please enter password");
                    passwordtxt.requestFocus();
                } else if (email.isEmpty() && pw.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please Enter an Email and Password to Register", Toast.LENGTH_LONG).show();
                } else if (!((email.isEmpty() && pw.isEmpty()))) {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Registration Unsuccessful, Please Try Again.", Toast.LENGTH_LONG).show();
                            } else {
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Error Occured", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (RegisterActivity.this, LogInActivity.class);
            }
        });

    }


}
