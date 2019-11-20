package com.example.fyp.Helper;


import android.graphics.Bitmap;
import android.media.Image;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import java.nio.ByteBuffer;



public abstract class VisionProcessorBase<T> implements VisionImageProcessor {



    public VisionProcessorBase() {
    }

    @Override
    public void process(
            ByteBuffer data, final FrameMetadata frameMetadata, final GraphicOverlay
            graphicOverlay) {

        FirebaseVisionImageMetadata metadata =
                new FirebaseVisionImageMetadata.Builder()
                        .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                        .setWidth(frameMetadata.getWidth())
                        .setHeight(frameMetadata.getHeight())
                        .setRotation(frameMetadata.getRotation())
                        .build();

        detectInVisionImage(
                FirebaseVisionImage.fromByteBuffer(data, metadata), frameMetadata, graphicOverlay);
    }

    // Bitmap version
    @Override
    public void process(Bitmap bitmap, final GraphicOverlay
            graphicOverlay) {

        detectInVisionImage(FirebaseVisionImage.fromBitmap(bitmap), null, graphicOverlay);
    }

    /**
     * Detects feature from given media.Image
     *
     * @return created FirebaseVisionImage
     */
    @Override
    public void process(Image image, int rotation, final GraphicOverlay graphicOverlay) {

        // This is for overlay display's usage
        FrameMetadata frameMetadata =
                new FrameMetadata.Builder().setWidth(image.getWidth()).setHeight(image.getHeight
                        ()).build();
        FirebaseVisionImage fbVisionImage =
                FirebaseVisionImage.fromMediaImage(image, rotation);
        detectInVisionImage(fbVisionImage, frameMetadata, graphicOverlay);
    }

    private void detectInVisionImage(
            FirebaseVisionImage image,
            final FrameMetadata metadata,
            final GraphicOverlay graphicOverlay) {
        detectInImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<T>() {
                            @Override
                            public void onSuccess(T results) {
                                VisionProcessorBase.this.onSuccess(results, metadata,
                                        graphicOverlay);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure( Exception e) {
                                VisionProcessorBase.this.onFailure(e);
                            }
                        });

    }

    @Override
    public void stop() {
    }

    protected abstract Task<T> detectInImage(FirebaseVisionImage image);

    protected abstract void onSuccess(
          T results,
             FrameMetadata frameMetadata,
             GraphicOverlay graphicOverlay);

    protected abstract void onFailure( Exception e);
}
