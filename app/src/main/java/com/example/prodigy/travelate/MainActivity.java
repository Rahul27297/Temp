package com.example.prodigy.travelate;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
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
    private String monumentTitle, monumentInfo;
    private String browserUrl;
    private boolean isDetected = false;
    String inputLanguage = "English";
    Uri outputFileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Georgia.ttf");
        setupViews();
    }


    ViewGroup layoutImage;
    ViewGroup layoutSwitch;
    ViewGroup progressUpdate;
    ViewGroup parentProgressView;
    ViewGroup selectLanguageView;
    //Switch mainSwitch;
    ViewGroup option1View;
    ViewGroup option2View;

    Button uploadButton;
    Button cameraButton;
    Button galleryButton;
    Button browserButton;
    Button proceedButton;
    Button changeSourceLanguageButton;

    Bitmap bitmap = null;

    ImageView displayImageView;
    ImageView monumentImageView;
    ImageView translateImageView;
    ImageView landmarkIconImageView;
    ImageView signboardIconImageView;

    TextView monumentNameView;
    TextView monumentInfoView;
    TextView feedbackTextView;
    TextView displayResult1TextView;
    TextView displayResult2TextView;
    TextView option1TextView;
    TextView option2TextView;
    TextView displaySourceLanguageTextView;
    TextView displayInputLanguage1TextView;
    TextView displayInputLanguage2TextView;



    private void setupViews() {

        displayImageView = findViewById(R.id.image);

        layoutImage = findViewById(R.id.layout_image);
        layoutSwitch = findViewById(R.id.layout_switch);
        //mainSwitch = findViewById(R.id.mainswitch);
        selectLanguageView = findViewById(R.id.selectLanguage);
        option1View = findViewById(R.id.option1view);
        option2View = findViewById(R.id.option2view);
        option1TextView = findViewById(R.id.option1);
        option2TextView = findViewById(R.id.option2);
        landmarkIconImageView = findViewById(R.id.landmarkimage);
        signboardIconImageView = findViewById(R.id.signboardimage);
        displaySourceLanguageTextView = findViewById(R.id.sourcelanguage);
        changeSourceLanguageButton = findViewById(R.id.changesourcelanguage);

        option1View.setBackgroundColor(getResources().getColor(R.color.pc1));
        //option1TextView.setBackgroundColor(getResources().getColor(R.color.pc1));
        option1TextView.setTextColor(getResources().getColor(R.color.white));
        landmarkIconImageView.setColorFilter(getResources().getColor(R.color.white));


        parentProgressView = findViewById(R.id.ParentProgressView);
        progressUpdate = findViewById(R.id.progressUpdate);
        translateImageView = findViewById(R.id.translateimage);
        layoutImage.setVisibility(View.GONE);
        progressUpdate.setVisibility(View.GONE);
        parentProgressView.setVisibility(View.GONE);
        selectLanguageView.setVisibility(View.GONE);
        proceedButton = findViewById(R.id.proceedlanguagebutton);

        feedbackTextView = findViewById(R.id.feedbackTextView);

        option1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLanguageView.setVisibility(View.GONE);
                option1View.setBackgroundColor(getResources().getColor(R.color.pc1));
                //option1TextView.setBackgroundColor(getResources().getColor(R.color.pc1));
                option1TextView.setTextColor(getResources().getColor(R.color.white));
                landmarkIconImageView.setColorFilter(getResources().getColor(R.color.white));
                option2View.setBackgroundColor(getResources().getColor(R.color.white));
                option2TextView.setTextColor(getResources().getColor(R.color.pc1));
                signboardIconImageView.setColorFilter(getResources().getColor(R.color.pc1));
                option = 1;
                Log.e("llllll","Landmark");
            }
        });

        option2View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option = 2;
                option2View.setBackgroundColor(getResources().getColor(R.color.pc1));
                option2TextView.setTextColor(getResources().getColor(R.color.white));
                signboardIconImageView.setColorFilter(getResources().getColor(R.color.white));
                option1View.setBackgroundColor(getResources().getColor(R.color.white));
                //option1TextView.setBackgroundColor(getResources().getColor(R.color.pc1));
                option1TextView.setTextColor(getResources().getColor(R.color.pc1));
                landmarkIconImageView.setColorFilter(getResources().getColor(R.color.pc1));
                Log.e("llllll","signboard");
                changeSourceLanguageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectLanguageView.setVisibility(View.VISIBLE);
                        selectLanguageView.setBackgroundColor(getResources().getColor(R.color.background));
                        proceedButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectLanguageView.setVisibility(View.GONE);
                            }
                        });
                    }
                });


            }
        });

        /*mainSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Travelate", "Onclick");
                boolean b = ((Switch) view).isChecked();
                if (b) {
                    option = 2;
                    option2TextView.setBackgroundColor(getResources().getColor(R.color.pc1));
                    option2TextView.setTextColor(getResources().getColor(R.color.white));
                    option1TextView.setBackgroundColor(getResources().getColor(R.color.white));
                    option1TextView.setTextColor(getResources().getColor(R.color.dpc1));
                    selectLanguageView.setVisibility(View.VISIBLE);
                    selectLanguageView.setBackgroundColor(getResources().getColor(R.color.background));
                    proceedButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectLanguageView.setVisibility(View.GONE);
                        }
                    });
                    //Log.d("","signboard");
                } else {
                    option = 1;
                    selectLanguageView.setVisibility(View.GONE);
                    option1TextView.setBackgroundColor(getResources().getColor(R.color.pc1));
                    option1TextView.setTextColor(getResources().getColor(R.color.white));
                    option2TextView.setBackgroundColor(getResources().getColor(R.color.white));
                    option2TextView.setTextColor(getResources().getColor(R.color.dpc1));
                    //Log.d("", "landmark");
                }
            }
        });*/

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
                //layoutImage.setBackgroundColor(getResources().getColor(R.color.background));
                parentProgressView.setVisibility(View.VISIBLE);
                parentProgressView.setBackgroundColor(getResources().getColor(R.color.background));
                progressUpdate.setVisibility(View.VISIBLE);
                feedbackTextView.setText("Uploading Image, fetching Result");
                asyncupload.execute();
            }
        });

    }

    public void languageSelection(View view){

        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()){
            case R.id.eradio:
                if(checked) {
                    inputLanguage = "English";
                    displaySourceLanguageTextView.setText("English");
                }
                break;

            case R.id.mradio:
                if(checked) {
                    inputLanguage = "Marathi";
                    displaySourceLanguageTextView.setText("Marathi");
                }
                break;

            case R.id.hradio:
                if(checked) {
                    inputLanguage = "Hindi";
                    displaySourceLanguageTextView.setText("Hindi");
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri;
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK )
        {
            if(option == 1) {
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
                displayImageView.setImageBitmap(bitmap);
                isImageShown = true;
                toggleViews();
            }
            else if(option == 2){
                getCaptureImageOutputUri();
                uri = outputFileUri;
                isImageShown = true;
                toggleViews();
                CropImage.activity(uri).start(this);
            }

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
            Log.d("Back","backpressed");

            isImageShown = false;
            toggleViews();
            return;
        }
        else if(isDetected){
            Log.d("Back","backpressed");
            finish();
            startActivity(getIntent());
        }
        progressUpdate.setVisibility(View.GONE);
        super.onBackPressed();
    }

    private void takepicture() {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (option == 2){
            getCaptureImageOutputUri();
            camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        }
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
        if(option == 1) {
            Log.e("Travelate","Here2");
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<ImageClass> call = apiInterface.uploadImage(Image);
            call.enqueue(new Callback<ImageClass>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {
                    ImageClass imageClass = response.body();
                    monumentTitle = imageClass.getMonumentTitle();
                    monumentInfo = imageClass.getMonumentInfo();
                    setContentView(R.layout.display_monumentinfo);

                    isDetected = true;

                    monumentImageView = findViewById(R.id.monumentImageView);
                    monumentInfoView = findViewById(R.id.monumentInfoView);
                    monumentNameView = findViewById(R.id.monumentNameView);
                    browserButton = findViewById(R.id.browserOpen);
                    progressUpdate.setVisibility(View.GONE);
                    String tempname;

                    monumentNameView.setText(monumentTitle);
                    monumentInfoView.setText(monumentInfo);
                    monumentImageView.setImageBitmap(bitmap);
                    //monumentInfoView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

                    tempname = monumentTitle.replace(' ', '_');

                    browserUrl = "https://en.wikipedia.org/wiki/" + tempname;

                    browserButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                            browserIntent.setData(Uri.parse(browserUrl));
                            startActivity(browserIntent);
                        }
                    });

                    //Log.e("Title",monumentTitle);
                    //Log.e("Info",monumentInfo);

                    //Toast.makeText(getApplicationContext(),imageClass.getMonumentTitle(),Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<ImageClass> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                    progressUpdate.setVisibility(View.GONE);
                }
            });
        }
        else if(option == 2){

            Api2Interface api2Interface = Api2Client.getApi2Client().create(Api2Interface.class);
            Call<TranslateClass> call = api2Interface.uploadImage(Image,inputLanguage);
            call.enqueue(new Callback<TranslateClass>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<TranslateClass> call, Response<TranslateClass> response) {

                    TranslateClass translateClass = response.body();
                    setContentView(R.layout.translate);
                    displayResult1TextView = findViewById(R.id.displayresult1text);
                    displayResult2TextView = findViewById(R.id.displayresult2text);
                    translateImageView = findViewById(R.id.translateimage);
                    displayInputLanguage1TextView = findViewById(R.id.inputlanguage1);
                    displayInputLanguage2TextView = findViewById(R.id.inputlanguage2);
                    isDetected = true;

                    if(inputLanguage == "English"){
                        Log.e("travelate","In on response");
                        displayInputLanguage1TextView.setText("Marathi");
                        displayResult1TextView.setText(translateClass.getMarathi());
                        Log.e("Translate result",translateClass.getMarathi());
                        displayInputLanguage2TextView.setText("Hindi");
                        displayResult2TextView.setText(translateClass.getHindi());
                        Log.e("Translate result",translateClass.getHindi());
                    }
                    else if(inputLanguage == "Hindi"){
                        Log.e("Travelate","Here2,"+inputLanguage);
                        Log.e("Translate result",translateClass.getMarathi());
                        displayInputLanguage1TextView.setText("Marathi");
                        displayResult1TextView.setText(translateClass.getMarathi());

                        displayInputLanguage2TextView.setText("English");
                        displayResult2TextView.setText(translateClass.getEnglish());
                        Log.e("Translate result",translateClass.getEnglish());
                    }
                    else if(inputLanguage == "Marathi"){
                        displayInputLanguage1TextView.setText("English");
                        displayResult1TextView.setText(translateClass.getEnglish());
                        Log.e("Translate result",translateClass.getEnglish());
                        displayInputLanguage2TextView.setText("Hindi");
                        displayResult2TextView.setText(translateClass.getHindi());
                        Log.e("Translate result",translateClass.getHindi());
                    }
                    translateImageView.setImageBitmap(bitmap);
                   //reponse for 2nd part here

                    //Log.e("Title",monumentTitle);
                    //Log.e("Info",monumentInfo);

                    //Toast.makeText(getApplicationContext(),imageClass.getMonumentTitle(),Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<TranslateClass> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                    progressUpdate.setVisibility(View.GONE);
                    //startActivity(getParentActivityIntent());
                }
            });
        }
    }

    private void getCaptureImageOutputUri() {
        outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new  File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        //return outputFileUri;
    }
}


