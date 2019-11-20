package com.example.fyp.App;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fyp.Helper.FaceGraphic;
import com.example.fyp.Helper.FrameMetadata;
import com.example.fyp.Helper.GraphicOverlay;
import com.example.fyp.Helper.VisionProcessorBase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.io.IOException;
import java.util.List;
public class MainActivity extends VisionProcessorBase<List<FirebaseVisionFace>>    {

    private static final String TAG = "FaceDetectionProcessor";

    private final FirebaseVisionFaceDetector detector;

    public MainActivity() {
        FirebaseVisionFaceDetectorOptions options = new FirebaseVisionFaceDetectorOptions.Builder()
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .enableTracking()
                .build();

        detector = FirebaseVision.getInstance().getVisionFaceDetector(options);

    }

    public MainActivity(FirebaseVisionFaceDetector detector) {
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
            FirebaseVisionFace face = faces.get(i);
            FaceGraphic faceGraphic = new FaceGraphic(graphicOverlay);
            graphicOverlay.add(faceGraphic);
            faceGraphic.updateFace(face, frameMetadata.getCameraFacing());
            Log.d(TAG, "onSuccess: " + face.toString());
        }
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.e(TAG, "Face detection failed " + e);
    }
}