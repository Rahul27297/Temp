package com.example.prodigy.travelate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

//import android.hardware.camera2;

@SuppressLint("LogNotTimber")
public class ImageActivity extends AppCompatActivity {

    private static final String TAG = "ImageActivity";

    private Camera mCamera;
    private Preview mPreview;

    //private CameraPreview mPreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        if (!checkCameraHardware(this)) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            return;
        }

        // TODO Check for Camera Permission !!!

        mCamera = getCameraInstance();
        if (mCamera != null) {
            mPreview = findViewById(R.id.camera_preview);
            mPreview.setCamera(mCamera);
        }
    }

    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(0); // attempt to get a Camera instance
            Log.e(TAG, "Camera Opened");
        } catch (Exception e) {
            Log.e(TAG, "failed to open Camera");
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
}
