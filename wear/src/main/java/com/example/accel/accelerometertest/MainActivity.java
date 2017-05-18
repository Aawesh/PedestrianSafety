package com.example.accel.accelerometertest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

/** TODO
 * Don't know whether a wearable app should be an Activity or a service.
 * If it should be a service then we can start the app with MessageReceiverService.java
 */

public class MainActivity extends Activity {

    private TextView mTextView;
    private final static String TAG = "WearMainActivity";
    private Intent serviceIntent;

    private static final int MY_PERMISSIONS_REQUEST_BODY_SENSORS = 001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });

        Log.d(TAG,"Activity created");


        if (checkSelfPermission(Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.BODY_SENSORS},
                    MY_PERMISSIONS_REQUEST_BODY_SENSORS);

            return;
        }

        serviceIntent = new Intent(this, MessageReceiverService.class);
        this.startService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stopService(serviceIntent);
    }
}
