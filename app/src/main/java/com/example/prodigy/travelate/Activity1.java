package com.example.prodigy.travelate;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import static android.view.View.GONE;

public class Activity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);
         final Context context = this.getApplicationContext();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                //Checking network Connectivity
                ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activenetwork = cm.getActiveNetworkInfo();
                boolean connectivity = (activenetwork != null && activenetwork.isConnectedOrConnecting());

                ProgressBar spinner = (ProgressBar)findViewById(R.id.loading_spinner);
                spinner.setVisibility(View.VISIBLE);
                if(!connectivity){
                    //Display error message
                    spinner.setVisibility(View.GONE);
                    Toast t1 = Toast.makeText(getApplicationContext(),"Not Connected", Toast.LENGTH_LONG);
                    t1.show();
                    finishAffinity();
                }
                else{
                    //setContentView(R.layout.activity_main);
                    //Head on to the main activity
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
            }
        }, 3000);


    }
}
