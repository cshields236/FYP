package com.example.fyp.App;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.fyp.Helper.CameraSourcePreview;
import com.example.fyp.Helper.GraphicOverlay;
import com.example.fyp.R;
import com.example.fyp.Helper.CameraSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppFunctionality extends AppCompatActivity {
    private static final int PERMISSION_REQUESTS = 0;
    static String TAG = "AppFunct";
    private CameraSource cameraSource = null;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    CameraActivity activity;

    MediaPlayer bleepMP;
    //    final MediaPlayer bleepMP = MediaPlayer.create(AppFunctionality.this, R.raw.bleep);
    private volatile FirebaseVisionFace firebaseVisionFace;

    public AppFunctionality() {

    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        bleepMP = MediaPlayer.create(AppFunctionality.this, R.raw.bleep);
        preview = findViewById(R.id.firePreview);
        graphicOverlay = findViewById(R.id.graphic_overlay);
        final CameraActivity activity = new CameraActivity();


        if (preview == null) {
            Log.d(TAG, "Preview is null");
        }
        graphicOverlay = findViewById(R.id.graphic_overlay);
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null");
        }
        final ImageView cancel = findViewById(R.id.btnFinish);
        cancel.setClickable(false);
        final ImageView start = findViewById(R.id.btn_detect);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel.setClickable(true);


                start.setClickable(false);
                if (allPermissionsGranted()) {
                    createCameraSource();
                    Log.d(TAG, "EYES_CLOSED: " + activity.isEyesClosed());



//                    if (activity.isEyesClosed() == true) {
//                        final MediaPlayer bleepMP = MediaPlayer.create(AppFunctionality.this, R.raw.bleep);
//                        bleepMP.start();
//                    }
                } else {
                    getRuntimePermissions();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraSource.stop();
                graphicOverlay.clear();

                start.setClickable(true);

            }
        });


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


}
