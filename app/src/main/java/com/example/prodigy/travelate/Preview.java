package com.example.prodigy.travelate;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prodigy on 11/2/18.
 */

public class Preview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = Preview.class.getName();

    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private Camera.Parameters mParameters;
    Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback(){
        @Override
        public void onAutoFocus(boolean b, Camera camera) {
            if(b){
                mCamera.cancelAutoFocus();
            }
        }

    };

    public void doTouchFocus(final Rect tfocusRect) {
        try {
            List<Camera.Area> focusList = new ArrayList<Camera.Area>();
            Camera.Area focusArea = new Camera.Area(tfocusRect, 1000);
            focusList.add(focusArea);

            Camera.Parameters param = mCamera.getParameters();
            param.setFocusAreas(focusList);
            param.setMeteringAreas(focusList);
            mCamera.setParameters(param);

            mCamera.autoFocus(myAutoFocusCallback);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Unable to autofocus");
        }
    }


    public Preview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mCamera.setDisplayOrientation(90);
        setup();
        mParameters = mCamera.getParameters();
        mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        mCamera.setParameters(mParameters);
    }

    /**
     * Initiates the SurfaceHolder field and installs a SurfaceHolder.Callback
     */
    private void setup() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        if (mCamera == null) {
            Log.w(TAG, "surfaceCreated: Camera Null");
            return;
        }

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
            Log.v(TAG, "surfaceCreated: Camera preview enabled");
        } catch (IOException e) {
            Log.e(TAG, "Error setting camera preview: " + e.getMessage(), e);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mSurfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.e(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // Surface will be destroyed when we return, so stop the preview.
        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface.
            mCamera.stopPreview();
            mCamera.release();
            Log.d("Travelate","Camera released");
        }

    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

}
