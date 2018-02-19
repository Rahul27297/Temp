package com.example.prodigy.travelate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private int option = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    private int PICK_IMAGE_REQUEST = 1;
    String mCurrentPhotoPath;
    private boolean isImageShown = false;
    public static final String upload_url = "http://127.0.1.1/Travelate/uploads/upload.php";
    public static final String images_url = "http://127.0.1.1/Travelate/uploads/getimage.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
    }


    ViewGroup layoutImage;
    ViewGroup layoutSwitch;
    Switch mainSwitch;

    Button cameraButton;
    Button galleryButton;

    ImageView displayImageView;

    private void setupViews(){

        displayImageView = findViewById(R.id.image);

        layoutImage = findViewById(R.id.layout_image);
        layoutSwitch = findViewById(R.id.layout_switch);
        mainSwitch = findViewById(R.id.mainswitch);
        // Call Kar
        mainSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Travelate", "Onclick");
                boolean b = ((Switch) view).isChecked();
                if (b) {
                    option = 2;
                    //Log.d("","signboard");
                } else {
                    option = 1;
                    Log.d("", "landmark");
                }
            }
        });

        cameraButton = findViewById(R.id.camera);
        galleryButton = findViewById(R.id.gallery);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open cameraButton
                takepicture();
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open galleryButton
                Intent gallery_intent = new Intent(Intent.ACTION_GET_CONTENT);
                gallery_intent.setType("image/*");
                //gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(gallery_intent,PICK_IMAGE_REQUEST);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
            displayImageView.setImageBitmap(image);
            Log.e("Travelate","Image shown");
            isImageShown = true;
            toggleViews();
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                displayImageView.setImageBitmap(bitmap);
                isImageShown = true;
                toggleViews();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void toggleViews() {
        if (isImageShown) {
            layoutImage.setVisibility(View.VISIBLE);
            layoutSwitch.setVisibility(View.GONE);
        } else {
            layoutImage.setVisibility(View.GONE);
            layoutSwitch.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (isImageShown) {
            isImageShown = false;
            toggleViews();
            return;
        }
        super.onBackPressed();
    }

    private void takepicture(){
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(camera_intent.resolveActivity(getPackageManager()) != null){
                startActivityForResult(camera_intent,REQUEST_IMAGE_CAPTURE);
            }
        }

}
