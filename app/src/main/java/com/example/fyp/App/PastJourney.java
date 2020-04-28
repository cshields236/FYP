package com.example.fyp.App;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.fyp.Entities.Journey;
import com.example.fyp.Entities.JourneyInformation;
import com.example.fyp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class PastJourney extends AppCompatActivity implements OnMapReadyCallback{
    private static final String TAG = "Past";
    ArrayList<Journey> js = new ArrayList<>();
    TextView t, lt;
    ArrayList<String> times = new ArrayList<>();
    ArrayList<Integer> blinks = new ArrayList<>();

    private ArrayList<JourneyInformation> journeys = new ArrayList<>();
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_journey);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapV);
        mapFragment.getMapAsync( PastJourney.this);


        t = findViewById(R.id.lengthTxt);


        Intent i = getIntent();
        String j = i.getStringExtra("journ");



        // Getting Current user from Authenticator
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("users").document(user.getUid()).collection("Journeys").document(j).collection("Journey");



        ref.orderBy("time").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Journey information = document.toObject(Journey.class);
                        js.add(information);
                    }
                    for (Journey j : js) {
                        times.add(j.getTime().split(" ")[1].split(":")[0] + ":" + j.getTime().split(" ")[1].split(":")[1]);
                        journeys = (ArrayList<JourneyInformation>) j.getJourneyInformationss();
                    }
                    for (JourneyInformation inf : journeys) {
                        blinks.add(inf.getBlink());
                    }


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                mMap = googleMap;
                LatLng here =new LatLng (54.047340, -7.315570);
                LatLng there = new LatLng(54.074280, -7.077100);

                mMap.addMarker(new MarkerOptions().position(here).title("Started Here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(here));
                mMap.setMaxZoomPreference(100);

                mMap.addMarker(new MarkerOptions().position(there).title("Finished Here"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(here));
                mMap.setMaxZoomPreference(100);

                mMap.setMaxZoomPreference(20.0f);
                mMap.setMinZoomPreference(10.0f);



            }
        }, 2000);   //5 seconds

    }


}
