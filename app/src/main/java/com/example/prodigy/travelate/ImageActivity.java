package com.example.prodigy.travelate;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
//import android.hardware.camera2;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class ImageActivity extends AppCompatActivity {
    public static final int MEDIA_TYPE_IMAGE = 1;

    private Camera mCamera;
    private Preview mPreview;
    //private CameraPreview mPreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Context context = getApplicationContext();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        if(!checkCameraHardware(context)){
            Log.e(getString(R.string.app_name), "failed to open Camera1111");
        }
        mCamera = getCameraInstance();
        Log.e(getString(R.string.app_name), String.valueOf(Camera.getNumberOfCameras()));
        mPreview = new Preview(context,mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        Log.e("Travelate", "All done");
        Bundle extras = getIntent().getExtras();

    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(0); // attempt to get a Camera instance
            Log.e("Travelate", "Camera Opened 2222");
        }
        catch (Exception e){
            Log.e("Travelate", "failed to open Camera2222");
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }


}
