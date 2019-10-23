package com.example.fyp.App;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    public FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        mFirebaseAuth = FirebaseAuth.getInstance();
        final EditText emailtxt = findViewById(R.id.lEmail);
        final EditText passwordtxt = findViewById(R.id.lPassword);
        Button registerbtn = findViewById(R.id.lButton);
        TextView tvSignIn = findViewById(R.id.lLink);

//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            FirebaseUser firebaseUser;
//
//            {
//                firebaseUser = mFirebaseAuth.getCur  rentUser();
//            }
//           // if(fire)
//
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//            }
//        //}
//    }
}}
