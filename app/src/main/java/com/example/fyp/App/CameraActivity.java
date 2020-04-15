package com.example.fyp.App;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.fyp.Entities.Journey;
import com.example.fyp.Entities.JourneyInformation;
import com.example.fyp.Helper.FaceGraphic;
import com.example.fyp.Helper.FrameMetadata;
import com.example.fyp.Helper.GraphicOverlay;
import com.example.fyp.Helper.VisionProcessorBase;
import com.example.fyp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CameraActivity extends VisionProcessorBase<List<FirebaseVisionFace>> {

    private static final String TAG = "FaceDetectionProcessor";

    private final FirebaseVisionFaceDetector detector;

    public FirebaseVisionFace face;


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public CameraActivity() {
        FirebaseVisionFaceDetectorOptions options = new FirebaseVisionFaceDetectorOptions.Builder()
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .enableTracking()
                .build();

        detector = FirebaseVision.getInstance().getVisionFaceDetector(options);

    }

    public CameraActivity(FirebaseVisionFaceDetector detector) {
        this.detector = detector;
    }

    @Override
    public void stop() {
        try {
            detector.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception thrown while trying to close Face Detector: " + e);
        }
    }

    @Override
    protected Task<List<FirebaseVisionFace>> detectInImage(FirebaseVisionImage image) {

        return detector.detectInImage(image);
    }


    @Override
    protected void onSuccess(
            List<FirebaseVisionFace> faces,
            FrameMetadata frameMetadata,
            GraphicOverlay graphicOverlay) {
        graphicOverlay.clear();

        for (int i = 0; i < faces.size(); ++i) {


            face = faces.get(i);
            // Declaring and adding the graphic overlay to the screen
            FaceGraphic faceGraphic = new FaceGraphic(graphicOverlay);
            graphicOverlay.add(faceGraphic);
            //updating the graphic overlay every time a face is detected in order to track the face in real time
            faceGraphic.updateFace(face, frameMetadata.getCameraFacing());




                    AppFunctionality.getInstance().updateFace(face);

                }




    }


    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.e(TAG, "Face detection failed " + e);
    }

}

