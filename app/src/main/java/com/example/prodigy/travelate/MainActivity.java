package com.example.prodigy.travelate;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private int option = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    private int PICK_IMAGE_REQUEST = 1;
    private boolean isImageShown = false;
    private int PIC_CROP = 3;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
    }


    ViewGroup layoutImage;
    ViewGroup layoutSwitch;
    Switch mainSwitch;

    Button uploadButton;
    Button cameraButton;
    Button galleryButton;

    Bitmap bitmap = null;

    ImageView displayImageView;

    private void setupViews() {

        displayImageView = findViewById(R.id.image);

        layoutImage = findViewById(R.id.layout_image);
        layoutSwitch = findViewById(R.id.layout_switch);
        mainSwitch = findViewById(R.id.mainswitch);
        layoutImage.setVisibility(View.GONE);
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
        uploadButton = findViewById(R.id.uploadbutton);
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
                startActivityForResult(gallery_intent, PICK_IMAGE_REQUEST);
               // Crop.pickImage(MainActivity.this);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loader here
                AsyncUpload asyncupload = new AsyncUpload();
                asyncupload.execute();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri;
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK )
        {
            uri = getCaptureImageOutputUri();
            Log.e("Route","In imagecaputre");
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            displayImageView.setImageBitmap(bitmap);
            Log.e("Travelate", "Image shown");
            isImageShown = true;
            toggleViews();
            if(option == 2)
               CropImage.activity(uri).start(this);
        }
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            try {
                uri = data.getData();
                Log.e("Route","In imagePick");
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                displayImageView.setImageBitmap(bitmap);
                isImageShown = true;
                toggleViews();
                if(option == 2)
                    CropImage.activity(uri).start(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && option == 2) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    uri = result.getUri();
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    displayImageView.setImageBitmap(bitmap);
                    displayImageView.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    Log.e("Travelate", "onActivityResult: ", e);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("Travelate", "onActivityResult: ", error);
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

    private void takepicture() {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (camera_intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(camera_intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private class AsyncUpload extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            uploadImage();
            return null;
        }
    }

    private String imageToString(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte,Base64.DEFAULT);
    }

    private void uploadImage(){
        final String Image = imageToString();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ImageClass> call = apiInterface.uploadImage(Image);
        call.enqueue(new Callback<ImageClass>() {
            @Override
            public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {
                ImageClass imageClass = response.body();
                Toast.makeText(getApplicationContext(),imageClass.getResponse(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ImageClass> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_LONG).show();
            }
        });
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new  File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }
}


