package com.example.fyp.App;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Locale;
import java.util.Timer;
import java.util.UUID;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.fyp.Entities.Journey;
import com.example.fyp.Entities.JourneyInformation;
import com.example.fyp.Helper.CameraSourcePreview;
import com.example.fyp.Helper.GraphicOverlay;
import com.example.fyp.R;
import com.example.fyp.Helper.CameraSource;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppFunctionality extends AppCompatActivity {
    private static final int PERMISSION_REQUESTS = 0;
    static String TAG = "AppFunct";
    private CameraSource cameraSource = null;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    CameraActivity activity;
    private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    ArrayList<JourneyInformation> infor = new ArrayList<>();

    private volatile FirebaseVisionFace firebaseVisionFace;
    private ArrayList<Journey> journeys = new ArrayList<>();
    long difference = 0;
    static AppFunctionality activityA;

    double lat, lat1;
    double lng, lng1;

    private ImageView cancel;

    ImageView start;

    boolean clicked = false;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    MediaPlayer mp;
    DocumentReference ref;
    private int blinks;

    public AppFunctionality() {


    }

    protected void onCreate(Bundle savedInstanceState) {
        activityA = this;

        mp = MediaPlayer.create(this, R.raw.bleep);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        preview = findViewById(R.id.firePreview);
        graphicOverlay = findViewById(R.id.graphic_overlay);


        if (preview == null) {
            Log.d(TAG, "Preview is null");
        }
        graphicOverlay = findViewById(R.id.graphic_overlay);
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null");
        }
        cancel = findViewById(R.id.btnFinish);

        cancel.setClickable(false);
        start = findViewById(R.id.btn_detect);
        clicked = false;


        blinks = 0;
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        FusedLocationProviderClient mFusedLocationClient = new FusedLocationProviderClient(AppFunctionality.this);
        if (ContextCompat.checkSelfPermission(AppFunctionality.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult l) {
                    super.onLocationResult(l);
                    lat = l.getLastLocation().getLatitude();
                    lng = l.getLastLocation().getLongitude();


                }
            }, getMainLooper());

        }


    }

    public void StartJourney(View view) {

        blinks = 0;
        journeys = new ArrayList<>();
        clicked = false;
        if (allPermissionsGranted()) {
            Date date = new Date();



            String time = sdf.format(date);
            ref = db.collection("users").document(user.getUid()).collection("Journeys").document(time);



            createCameraSource();
            cancel.setClickable(true);


            start.setClickable(false);


        }

    }


    public void EndJourney(View view) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        cameraSource.stop();
        graphicOverlay.clear();

        clicked = true;
        start.setClickable(true);
        cancel.setClickable(false);


        for (int q = 0; q < infor.size(); q++) {
            if (infor.get(q).getLeftEye() < .2 && infor.get(q).getRightEye() < .2) {
                Log.d(TAG, "EndJourney: " + infor.get(q));
            }

        }


        FusedLocationProviderClient mFusedLocationClient = new FusedLocationProviderClient(AppFunctionality.this);
        if (ContextCompat.checkSelfPermission(AppFunctionality.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult l) {
                    super.onLocationResult(l);
                    lat1 = l.getLastLocation().getLatitude();
                    lng1 = l.getLastLocation().getLongitude();


                }
            }, getMainLooper());


        } else {


            ActivityCompat.requestPermissions
                    (AppFunctionality.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1
                    );
            Toast.makeText(AppFunctionality.this, "Coordinate", Toast.LENGTH_LONG).show();
        }


        //Get Journey Length

        String[] t1, t2;
        t1 = infor.get(0).getTime().split(" ");
        t2 = infor.get(infor.size() - 1).getTime().split(" ");

        String startingTime = t1[1];
        String endTIme = t2[1];


        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = null;
        try {
            date1 = format.parse(startingTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = format.parse(endTIme);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long difference = date2.getTime() - date1.getTime();

        Log.d(TAG, "EndJourney: " + difference);
        Intent intent = new Intent(AppFunctionality.this, JourneyRecap.class);


        intent.putExtra("Lat", String.valueOf(lat));
        intent.putExtra("Long", String.valueOf(lng));
        intent.putExtra("length", difference);
        intent.putExtra("endLat", String.valueOf(lat1));
        intent.putExtra("endLng", String.valueOf(lng1));
        intent.putExtra("startTime", infor.get(0).getTime().split(" ")[1]);
        intent.putExtra("endTime", infor.get(infor.size() - 1).getTime().split(" ")[1]);
        intent.putExtra("blinks", String.valueOf(blinks));

        startActivity(intent);

        Log.d(TAG, "Blink: " + blinks);

        finish();
    }


    public void updateFace(FirebaseVisionFace face) {

        firebaseVisionFace = face;


        if (face != null) {
            Date date = new Date();
            String time = sdf.format(date);

            //Get Journey Length
            if (infor.size() > 3) {
                String[] t1, t2;
                t1 = infor.get(0).getTime().split(" ");
                t2 = infor.get(infor.size() - 1).getTime().split(" ");

                String startingTime = t1[1];
                String endTIme = t2[1];


                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                Date date1 = null;
                try {
                    date1 = format.parse(startingTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date2 = null;
                try {
                    date2 = format.parse(endTIme);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                difference = date2.getTime() - date1.getTime();

            }

            if (face.getLeftEyeOpenProbability() < .2 && face.getLeftEyeOpenProbability() < .2 && face.getLeftEyeOpenProbability() > 0 && face.getLeftEyeOpenProbability() > 0) {
                blinks++;
            }
            JourneyInformation information = new JourneyInformation(user.getEmail(), time, face.getLeftEyeOpenProbability(), face.getRightEyeOpenProbability(), blinks);
            infor.add(information);

            Journey journey = new Journey(infor);
            if (infor.size() == 50) {
                ref.set(journey);
                infor.clear();
            }



            if (infor.size() > 5) {
                if (infor.get(infor.size() - 1).getLeftEye() < .2 && infor.get(infor.size() - 2).getLeftEye() < .2 && infor.get(infor.size() - 3).getLeftEye() < .2 && infor.get(infor.size() - 4).getLeftEye() < .2 && infor.get(infor.size() - 5).getLeftEye() < .2) {
                    mp.start();
                }

            }

            Log.d(TAG, "Size: " + infor.size());
        }


    }


    public void AddToList(JourneyInformation journeyInformation) {


    }


    public void createCameraSource() {

        cameraSource = new CameraSource(this, graphicOverlay);

        activity = new CameraActivity();
        cameraSource.setMachineLearningFrameProcessor(activity);
        cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);
        startCameraSource();

    }


    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null");
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null");
                }
                preview.start(cameraSource);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    public void flipCamera(View view) {
        if (cameraSource.getCameraFacing() == CameraSource.CAMERA_FACING_FRONT) {

            cameraSource.stop();
            graphicOverlay.clear();

            cameraSource = new CameraSource(this, graphicOverlay);


            cameraSource.setMachineLearningFrameProcessor(new CameraActivity());
            cameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);

            startCameraSource();
        } else {

            cameraSource.stop();
            graphicOverlay.clear();
            cameraSource = new CameraSource(this, graphicOverlay);


            cameraSource.setMachineLearningFrameProcessor(new CameraActivity());
            cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);

            startCameraSource();
        }
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }


    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        Log.i(TAG, "Permission granted!");
        if (allPermissionsGranted()) {
            createCameraSource();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }


    public static AppFunctionality getInstance() {
        return activityA;
    }
}
