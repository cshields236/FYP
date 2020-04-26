package com.example.fyp.App;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fyp.Entities.Journey;
import com.example.fyp.Entities.JourneyInformation;
import com.example.fyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

public class PastJourney extends AppCompatActivity {
    private static final String TAG = "Past";
    ArrayList<Journey> js = new ArrayList<>();
    TextView t;
    ArrayList<String> times = new ArrayList<>();
    ArrayList<String> blinks = new ArrayList<>();
    private ArrayList<JourneyInformation> journeys = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_journey);

        t = findViewById(R.id.tv);
        Intent i = getIntent();
        String j = i.getStringExtra("journ");
        t.setText(j);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("users").document(user.getUid()).collection("Journeys").document(j).collection("Journey");

// TODO View Specific Journey Information here
//        Show Length of journey, blinks per min, graph of blinks

        ref.orderBy("time").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Journey information = document.toObject(Journey.class);
                        js.add(information);
                    }
                    for (Journey j : js) {
                        times.add(j.getTime().split(" ")[1].split(":")[0] +":"+ j.getTime().split(" ")[1].split(":")[1] );

                    }

                    t.setText(times.toString());


            }

        }
    }).

    addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure (@NonNull Exception e){

        }
    });


}
}
