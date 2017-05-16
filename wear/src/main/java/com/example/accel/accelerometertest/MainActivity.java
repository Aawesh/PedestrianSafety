package com.example.accel.accelerometertest;

import android.app.Activity;
import android.content.Intent;
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

        serviceIntent = new Intent(this, MessageReceiverService.class);
        this.startService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stopService(serviceIntent);
    }
}
