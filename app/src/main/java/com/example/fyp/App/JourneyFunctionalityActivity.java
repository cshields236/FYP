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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.fyp.Entities.FaceInformation;
import com.example.fyp.Entities.Journey;
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

public class JourneyFunctionalityActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUESTS = 0;
    static String TAG = "AppFunct";
    private CameraSource cameraSource = null;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    CameraActivity activity;
    private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    ArrayList<FaceInformation> infor = new ArrayList<>();
    TextToSpeech toSpeech;
    private int prevBlink;
    private volatile FirebaseVisionFace firebaseVisionFace;
    private ArrayList<Journey> journeys = new ArrayList<>();
    long difference = 0;
    static JourneyFunctionalityActivity activityA;
    private int warnings;
    double lat, lat1;
    double lng, lng1;
    PrefsHelper prefsHelper;
    String phoneNo;
    double percentIncrease;
    String message;

    int mincounter = 0;
    int counter = 0;
    private ImageView cancel;

    ImageView start;

    boolean clicked = false;
    boolean msgSent = false;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    MediaPlayer mp;
    DocumentReference ref;
    CollectionReference ref1;
    private int blinks;
    int Tblinks;

    private HashMap<Integer, Integer> timeBlinks = new HashMap<Integer, Integer>();
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    public JourneyFunctionalityActivity() {


    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        prevBlink = 0;

        activityA = this;
        prefsHelper = new PrefsHelper(this);
        phoneNo = prefsHelper.getEmergencyContactNumber();

        prevBlink = 0;
        mp = MediaPlayer.create(this, R.raw.bleep);

        // Declare text to speech
        toSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    toSpeech.setLanguage(Locale.UK);
                }
            }
        });

        // Ensure phone does not enter sleep mode
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

        //find current location
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        FusedLocationProviderClient mFusedLocationClient = new FusedLocationProviderClient(JourneyFunctionalityActivity.this);
        if (ContextCompat.checkSelfPermission(JourneyFunctionalityActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
        prevBlink = 0;

        blinks = 0;
        journeys = new ArrayList<>();
        clicked = false;
        if (allPermissionsGranted()) {
            Date date = new Date();


            String time = sdf.format(date);

            // Declare both database references that will be used to write to DB,
            // Ref1 to declare that a journey has started and create the journey object
            // Ref to is where the specific face information from the journey is added to
            ref = db.collection("users").document(user.getUid()).collection("Journeys").document(time);


            ref1 = db.collection("users").document(user.getUid()).collection("Journeys").document(ref.getId()).collection("Journey");
            Journey j = new Journey(time);

            // Add to database to signify start of the journey
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


        // Get Location where journey finished
        FusedLocationProviderClient mFusedLocationClient = new FusedLocationProviderClient(JourneyFunctionalityActivity.this);
        if (ContextCompat.checkSelfPermission(JourneyFunctionalityActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                    (JourneyFunctionalityActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1
                    );
            Toast.makeText(JourneyFunctionalityActivity.this, "Coordinate", Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(JourneyFunctionalityActivity.this, JourneyRecap.class);


        // Send Journey Information to the journey recap activity
        intent.putExtra("Lat", String.valueOf(lat));
        intent.putExtra("Long", String.valueOf(lng));
        intent.putExtra("length", difference);
        intent.putExtra("endLat", String.valueOf(lat1));
        intent.putExtra("endLng", String.valueOf(lng1));
        intent.putExtra("startTime", ref.getId().split(" ")[1]);
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
        int recent =0;

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
                // Create face Object
                FaceInformation information = new FaceInformation(user.getEmail(), time, face.getLeftEyeOpenProbability(), face.getRightEyeOpenProbability(), Tblinks);
                infor.add(information);


                if (infor.size() > 2) {

                    if (infor.get(infor.size() - 1).getLeftEye() < .2 && infor.get(infor.size() - 2).getLeftEye() > .2) {
                        blinks++;
                        Tblinks++;
                    }
                }


                Log.d(TAG, "updateFace: " + difference);
                // check if journey has started
                if (difference > 0) {

                    Log.d(TAG, "updateFace: " + blinks);

                    // Every Time a minute passes add the total blinks for that minute to a hashmap
                    if ((((double) difference / (double) 60000 % 1) == 0.0) && blinks > 0) {

                        mincounter++;
                        timeBlinks.put(mincounter, blinks);
                        Log.d(TAG, "Blinky map: " + timeBlinks);
                        blinks = 0;
                        // no comparing in first minute
                        if (!timeBlinks.isEmpty()) {
                            int currentBlink = timeBlinks.get(timeBlinks.size());

                            // After first minute check if number of blinks in the second minute is more than the first
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
                            if (timeBlinks.size() > 2 ) {
                                // Get all previous blink numbers
                                for (int i : timeBlinks.keySet()) {
                                    recent =  timeBlinks.get(i);

//                                    Log.d(TAG, "prev: " + timeBlinks.get(i));
                                }prevBlink = prevBlink + recent;


                                int avg = prevBlink / timeBlinks.size();
                                Log.d(TAG, "Average: " + avg);
                                Log.d(TAG, "Size: " +  timeBlinks.size());

                                // if the current number of blinks is greater than the average alert the driver

                                    int diff = currentBlink - avg;
                                Log.d(TAG, "Differnce: " + diff);
                                    if (diff > 0 && avg > 0)
                                    {
                                         percentIncrease =  (diff / currentBlink ) * 100;
                                    }
                                Log.d(TAG, "percent differnce: " + percentIncrease);

                                if (percentIncrease > 20) {
                                    Toast.makeText(this, "More Blinks: " + avg + " " + timeBlinks.get(timeBlinks.size() - 1), Toast.LENGTH_SHORT).show();
                                    String toSpeak = "You Are Showing Signs Of Fatigue";
                                    toSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                    warnings++;
                                } else {
                                    Toast.makeText(this, "Less Blinks: " + avg + " " + timeBlinks.get(timeBlinks.size() - 1), Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                        // if warnings persist send message to emergency contact
                        if (warnings > 10 && msgSent == false) {
                            sendSMSMessage();
                            String toSpeak = "You have been showing continuous signs of fatigue, Please Consider Resting";
                            toSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
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
                        Toast.makeText(JourneyFunctionalityActivity.this, "Added To DB", Toast.LENGTH_SHORT);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Failed to write: ");
                    }
                });

            }


//            if (infor.size() > 5) {
//                if (infor.get(infor.size() - 1).getLeftEye() < .2 && infor.get(infor.size() - 2).getLeftEye() < .2 && infor.get(infor.size() - 3).getLeftEye() < .2 && infor.get(infor.size() - 4).getLeftEye() < .2 && infor.get(infor.size() - 5).getLeftEye() < .2) {
//                    mp.start();
//                }
//
//            }


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
                Toast.makeText(JourneyFunctionalityActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
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


    public static JourneyFunctionalityActivity getInstance() {
        return activityA;
    }
}