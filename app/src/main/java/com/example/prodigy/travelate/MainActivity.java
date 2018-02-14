package com.example.prodigy.travelate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout l1 = (LinearLayout)findViewById(R.id.item1);
        LinearLayout l2 = (LinearLayout)findViewById(R.id.item2);
        final Context context = getApplicationContext();
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ImageActivity.class);
                intent.putExtra("Option","Landmark");
                startActivity(intent);
            }
        });

        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ImageActivity.class);
                intent.putExtra("Option","SignBoard");
                startActivity(intent);
            }
        });
    }

    private void displayView(int position) {
        switch (position) {
            case 0:
                Intent intentHome = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intentHome);
                break;
        }
    }
}
