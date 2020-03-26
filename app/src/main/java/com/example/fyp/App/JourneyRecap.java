package com.example.fyp.App;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Entities.JourneyInformation;
import com.example.fyp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class JourneyRecap extends FragmentActivity implements OnMapReadyCallback {

    static String TAG = "JourneyRecap";
    private GoogleMap mMap;

    TextView length, startTime, endTime, blinktxt;

    ArrayList<JourneyInformation> infor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_recap);


        length = findViewById(R.id.lengthTxt);
        startTime = findViewById(R.id.starttimeTxt);
        endTime = findViewById(R.id.endtimeTxt);
        blinktxt = findViewById(R.id.blinkTxt);
        Intent intent = getIntent();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapV);
        mapFragment.getMapAsync(this);


        long jl = intent.getLongExtra("length", 0);


        startTime.setText(intent.getStringExtra("startTime"));
        endTime.setText(intent.getStringExtra("endTime"));

        length.setText(String.valueOf(jl / 1000));

        String b = intent.getStringExtra("blinks");
        double blinks = Double.parseDouble(b) / 6;
        blinktxt.setText(String.valueOf(blinks ));
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = getIntent();
                double lat = Double.parseDouble(intent.getStringExtra("Lat"));
                double lng = Double.parseDouble(intent.getStringExtra("Long"));
                double lat2 = Double.parseDouble(intent.getStringExtra("endLat"));
                double lng2 = Double.parseDouble(intent.getStringExtra("endLng"));

                LatLng currentLocation = new LatLng(lat, lng);
                LatLng endLocation = new LatLng(lat2, lng2);
                mMap = googleMap;

                mMap.setMaxZoomPreference(20.0f);
                mMap.setMinZoomPreference(10.0f);

                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Started Here"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                mMap.setMaxZoomPreference(100);


                mMap.addMarker(new MarkerOptions().position(endLocation).title("Finished Here"));

            }
        }, 2000);   //5 seconds


    }
}
