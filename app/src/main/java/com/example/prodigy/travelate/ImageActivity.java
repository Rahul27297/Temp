package com.example.prodigy.travelate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.View.X;

//import android.hardware.camera2;

@SuppressLint("LogNotTimber")
public class ImageActivity extends AppCompatActivity {

    private static final String TAG = "ImageActivity";

    private Camera mCamera;
    private Preview mPreview;
    private float x,y;
    private boolean drawingViewSet,listenerSet;
    private DrawingView drawingView;
    private Bitmap bmp;

    //private CameraPreview mPreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        new OpenCamera().execute();
        final Context context = getApplicationContext();
        Button button_capture = (Button)findViewById(R.id.button_capture);
        button_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.takePicture(null,null,mPicture);
                //setContentView(R.layout.image_display);
                //ImageView displaycapturedimage = (ImageView)findViewById(R.id.image);
                //displaycapturedimage.setImageBitmap(Bitmap.createScaledBitmap(bmp,displaycapturedimage.getWidth(),displaycapturedimage.getHeight(),false));
                Log.e(getString(R.string.app_name), "Image clicked");
            }
        });

        if (!checkCameraHardware(this)) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            return;
        }

        // TODO Check for Camera Permission !!!

    }

    /*private class StoreImage extends AsyncTask<PictureCallback, Void, >{

        @Override
        protected Picture doInBackground(Void... voids) {

        }
    }*/


    //opening camera on background async thread

    private class OpenCamera extends AsyncTask<Void, Void, Camera>{

        @Override
        protected Camera doInBackground(Void... voids) {
            mCamera = getCameraInstance();
            Log.d("","Camera Instance obtained");
            return mCamera;
        }

        @Override
        protected void onPostExecute(Camera camera){
            mCamera = camera;
            if (mCamera != null) {
                mPreview = new Preview(getApplicationContext(),mCamera);
                final FrameLayout camera_view = (FrameLayout)findViewById(R.id.camera_view);
                camera_view.addView(mPreview);
                camera_view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        x = motionEvent.getX();
                        y = motionEvent.getY();

                        final Rect touchRect = new Rect(
                                (int)(x - 100),
                                (int)(y - 100),
                                (int)(x + 100),
                                (int)(y + 100));


                        final Rect targetFocusRect = new Rect(
                                touchRect.left * 2000/camera_view.getWidth() - 1000,
                                touchRect.top * 2000/camera_view.getHeight() - 1000,
                                touchRect.right * 2000/camera_view.getWidth() - 1000,
                                touchRect.bottom * 2000/camera_view.getHeight() - 1000);

                        mPreview.doTouchFocus(targetFocusRect);
                        drawingView = new DrawingView(getApplicationContext(),touchRect);
                        setDrawingView(drawingView);
                        if (drawingViewSet) {
                            drawingView.setHaveTouch(true, touchRect);
                            drawingView.invalidate();

                            // Remove the square indicator after 1000 msec
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    drawingView.setHaveTouch(true,touchRect);
                                    drawingView.invalidate();
                                }
                            }, 1000);
                        }


                        String str = "x: " + String.valueOf(x) + " y: " + String.valueOf(y);
                        Log.d("Travelate",str);
                        //setContentView(new FocusArea(getApplicationContext(),X,Y));
                        return true;
                    }
                });
            }
        }
    }



    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCamera != null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;Log.d("Travelate", "in destrpy");
        }
    }*/


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

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            bmp = BitmapFactory.decodeByteArray(data,0,data.length);
            Log.d("","You can make a function call here");
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }
    };

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    /**
     * set DrawingView instance for touch focus indication.
     */
    public void setDrawingView(DrawingView dView) {
        drawingView = dView;
        drawingViewSet = true;
    }

}
