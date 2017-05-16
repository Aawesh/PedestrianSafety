package com.example.accel.accelerometertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private HandleMessage handleMessage;
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"HandheldActivityCreated");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleMessage = HandleMessage.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleMessage.startMeasurement();

    }


    @Override
    protected void onPause() {
        super.onPause();
        handleMessage.stopMeasurement();
    }
}
