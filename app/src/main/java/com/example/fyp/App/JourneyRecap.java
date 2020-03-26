package com.example.fyp.App;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.fyp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class JourneyRecap extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_recap);


        Intent intent = getIntent();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapV);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

//        Intent intent = getIntent();
//        double lat = Double.parseDouble(intent.getStringExtra("Lat"));
//        double lng = Double.parseDouble(intent.getStringExtra("Long"));

//        LatLng currentLocation = new LatLng(lat, lng);
        mMap = googleMap;

        mMap.setMaxZoomPreference(20.0f);
        mMap.setMinZoomPreference(10.0f);

//        mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are currently here"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
//        mMap.setMaxZoomPreference(100);
//

    }
}
