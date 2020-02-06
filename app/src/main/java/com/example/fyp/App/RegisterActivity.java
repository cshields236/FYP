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

import com.example.fyp.Entities.User;
import com.example.fyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;
    EditText emailtxt;
    EditText passwordtxt;
    EditText fNametxt;
    EditText lNametxt;

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


                Intent i = new Intent(RegisterActivity.this, LogInActivity.class);
                startActivity(i);


            }
        });

    }

    public void register(View view) {

        emailtxt = findViewById(R.id.rEmail);
        passwordtxt = findViewById(R.id.rPassword);
        fNametxt = findViewById(R.id.fName);
        lNametxt = findViewById(R.id.lName);

        final String email = emailtxt.getText().toString();
        String pw = passwordtxt.getText().toString();
        final String fname = fNametxt.getText().toString();
        final String lname = fNametxt.getText().toString();
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


                                Intent i = new Intent(RegisterActivity.this, AppFunctionality.class);
                                startActivity(i);


                                // Create a new user with a first and last name

                                FirebaseFirestore db = FirebaseFirestore.getInstance();


                                Log.d("", "createUserWithEmail:success");
                                FirebaseUser u = mAuth.getCurrentUser();


                                User u1 = new User();

                                u1.setId(u.getUid());
                                u1.setFname(fname);
                                u1.setLastName(lname);
                                u1.setEmail(email);


                                // Add a new document with a generated ID
                                db.collection("users").document(u.getUid())
                                        .set(u1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot added with ID: ");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });


                                Log.d(TAG, "onComplete: " + "created");

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
