package com.example.fyp.App;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.fyp.Entities.User;
import com.example.fyp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashboardActivity extends AppCompatActivity {
    private static final String TAG = "Dashboard";
    double lat;
    double lng;
    TextView lbl;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        Intent i = getIntent();

        lbl = findViewById(R.id.dashboardLbl);
        lbl.setText("Hello, " + i.getStringExtra("name") );


        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        FusedLocationProviderClient mFusedLocationClient = new FusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult l) {
                    super.onLocationResult(l);
                    lat = l.getLastLocation().getLatitude();
                    lng = l.getLastLocation().getLongitude();


                }
            }, getMainLooper());

        } else {


            ActivityCompat.requestPermissions
                    (DashboardActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1
                    );
            Toast.makeText(DashboardActivity.this, "Coordinate", Toast.LENGTH_LONG).show();
        }


       CardView mapsBtn = findViewById(R.id.imageView4);

        mapsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MapsActivity.class);
                intent.putExtra("Lat", String.valueOf(lat));
                intent.putExtra("Long", String.valueOf(lng));
                startActivity(intent);


            }
        });


    }


    public void launchStats(View v) {
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }

    public void launchFaceTrcker(View view) {
        Intent i = new Intent(this, AppFunctionality.class);
        startActivity(i);
    }

    public void ViewPastJourneys(View view) {
        Intent i = new Intent(this, ViewJourneys.class);
        startActivity(i);
    }

}
