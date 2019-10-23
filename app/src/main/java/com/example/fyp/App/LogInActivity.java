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

public class LogInActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    public FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);





    }

    public void signIn(View view){
        mAuth = FirebaseAuth.getInstance();


        mFirebaseAuth = FirebaseAuth.getInstance();
        final EditText emailtxt = findViewById(R.id.lEmail);
        final EditText passwordtxt = findViewById(R.id.lPassword);

        String email = emailtxt.getText().toString();
        String password = passwordtxt.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Log.d("", "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();

                    startActivity(new Intent(LogInActivity.this, MainActivity.class));
                }
                else{
                    Log.w("", "signInWithEmail:failure", task.getException());
                }
                Toast.makeText(LogInActivity.this, "Authentication failed." + task.getException().getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

}
