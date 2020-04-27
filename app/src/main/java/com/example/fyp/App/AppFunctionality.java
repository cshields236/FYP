package com.example.fyp.App;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.fyp.Entities.Journey;
import com.example.fyp.Entities.JourneyInformation;
import com.example.fyp.Entities.User;
import com.example.fyp.Helper.CameraSource;
import com.example.fyp.Helper.CameraSourcePreview;
import com.example.fyp.Helper.GraphicOverlay;
import com.example.fyp.Helper.PrefsHelper;
import com.example.fyp.R;
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
import com.google.firebase.firestore.DocumentSnapshot;
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
    TextToSpeech toSpeech;
    private int prevBlink;
    private volatile FirebaseVisionFace firebaseVisionFace;
    private ArrayList<Journey> journeys = new ArrayList<>();
    long difference = 0;
    static AppFunctionality activityA;
    private int warnings;
    double lat, lat1;
    double lng, lng1;
    PrefsHelper prefsHelper;
    String phoneNo;
    String message;

    int mincounter = 0;
    int counter = 0;
    private ImageView cancel;

    ImageView start;

    boolean clicked = false;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    MediaPlayer mp;
    DocumentReference ref;
    CollectionReference ref1;
    private int blinks;
    int Tblinks;

    private HashMap<Integer, Integer> timeBlinks = new HashMap<Integer, Integer>();
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    public AppFunctionality() {


    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        activityA = this;
        prefsHelper = new PrefsHelper(this);
        phoneNo = prefsHelper.getEmergencyContactNumber();

        prevBlink = 0;
        mp = MediaPlayer.create(this, R.raw.bleep);

        toSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    toSpeech.setLanguage(Locale.UK);
                }
            }
        });


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


            ref1 = db.collection("users").document(user.getUid()).collection("Journeys").document(ref.getId()).collection("Journey");
            Journey j = new Journey(time);
            ref.set(j);
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

        clicked = true;

        //Get Journey Length
        String[] t1, t2;
        t1 = ref.getId().split(" ");
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
        intent.putExtra("blinks", String.valueOf(Tblinks));
        intent.putExtra("warnings", String.valueOf(warnings));

        startActivity(intent);


        finish();
    }

    public void updateFace(FirebaseVisionFace face) {

        counter++;
        firebaseVisionFace = face;
        Journey journey;


        String[] t1, t2;
        if (!infor.isEmpty()) {
            t1 = ref.getId().split(" ");
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

        if (face != null) {
            if (counter < 50) {
                Date date = new Date();
                String time = sdf.format(date);


                if (face.getLeftEyeOpenProbability() < .2 && face.getLeftEyeOpenProbability() < .2 && face.getLeftEyeOpenProbability() > 0 && face.getLeftEyeOpenProbability() > 0) {

                }
                JourneyInformation information = new JourneyInformation(user.getEmail(), time, face.getLeftEyeOpenProbability(), face.getRightEyeOpenProbability(), Tblinks);
                infor.add(information);

                journey = new Journey(infor);

                if (infor.size() > 4) {

                    if (infor.get(infor.size() - 1).getLeftEye() < .2 && infor.get(infor.size() - 2).getLeftEye() > .2) {
                        blinks++;
                        Tblinks++;
                    }
                }


                Log.d(TAG, "updateFace: " + difference);
                if (difference > 0) {

                    Log.d(TAG, "updateFace: " + blinks);

                    if ((((double) difference / (double) 10000 % 1) == 0.0) && blinks > 0) {

                        mincounter++;
                        timeBlinks.put(mincounter, blinks);
                        Log.d(TAG, "Blinky Hashmap: " + timeBlinks);
                        blinks = 0;

                        if (!timeBlinks.isEmpty()) {
                            int currentBlink = timeBlinks.get(timeBlinks.size());
                            if (timeBlinks.size() == 2) {
                                if (timeBlinks.get(1) < currentBlink) {
                                    Toast.makeText(this, "More Blinks: " + timeBlinks.get(1) + " " + timeBlinks.get(2), Toast.LENGTH_SHORT).show();
                                    String toSpeak = "Blinks Increased";
                                    warnings++;
                                    toSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                } else {
                                    Toast.makeText(this, "Less Blinks: " + timeBlinks.get(1) + " " + timeBlinks.get(2), Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (timeBlinks.size() > 2) {
                                for (int i : timeBlinks.keySet()) {
                                    prevBlink += timeBlinks.get(i);
                                }
                                int avg = prevBlink / timeBlinks.size();

                                if (currentBlink > avg) {
                                    Toast.makeText(this, "More Blinks: " + timeBlinks.get(1) + " " + timeBlinks.get(2), Toast.LENGTH_SHORT).show();
                                    String toSpeak = "Blinks Increased";
                                    toSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                    warnings++;
                                } else {
                                    Toast.makeText(this, "Less Blinks: " + timeBlinks.get(1) + " " + timeBlinks.get(2), Toast.LENGTH_SHORT).show();

                                }
                            }
                        }

                        if (warnings > 10){
                            sendSMSMessage();
                        }
                    }


                }
                // Posting information to the database in batches of 50
            } else if (counter == 50 || clicked == true) {
                Date date = new Date();
                String time = sdf.format(date);

                journey = new Journey(infor, time);
                ref1.add(journey).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        infor.clear();
                        counter = 0;
                        Toast.makeText(AppFunctionality.this, "Added To DB", Toast.LENGTH_SHORT);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "FAIIIL: ");
                    }
                });

            }


            if (infor.size() > 5) {
                if (infor.get(infor.size() - 1).getLeftEye() < .2 && infor.get(infor.size() - 2).getLeftEye() < .2 && infor.get(infor.size() - 3).getLeftEye() < .2 && infor.get(infor.size() - 4).getLeftEye() < .2 && infor.get(infor.size() - 5).getLeftEye() < .2) {
                    mp.start();
                }

            }

//            Log.d(TAG, "Size: " + infor.size());
        }


    }


    protected void sendSMSMessage() {
        DocumentReference ref = db.collection("users").document(user.getUid());
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                message = "This is an automated warning message from Stay Awake, Drive Safe application. " + user.getFname() + " has been showing signs of fatigue,check in on him.";

                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage(phoneNo, null, message, null, null);
                Toast.makeText(AppFunctionality.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
            }


        });

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