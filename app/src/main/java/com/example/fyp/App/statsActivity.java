package com.example.fyp.App;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fyp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class statsActivity extends AppCompatActivity {
    Button btn;
    TextView txt;
    private static final String TAG = "StatsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);




         btn = findViewById(R.id.btnData);
         txt = findViewById(R.id.resulttxt);

         btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                 FirebaseFirestore db = FirebaseFirestore.getInstance();
                 DocumentReference ref = db.collection("users").document(user.getUid()).collection("Journeys").document();


                 // Writing a face analysis to the database once every second
                 ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                     @Override
                     public void onSuccess(DocumentSnapshot documentSnapshot) {
                       txt.setText( documentSnapshot.toString());
                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {

                     }
                 });
             }
         });
    }
}
