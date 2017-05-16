package com.example.accel.accelerometertest;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.hardware.SensorEventListener;
import android.util.Log;

import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by aawesh on 5/15/17.
 */

public class MessageReceiverService extends WearableListenerService{
    String TAG = "MessageReceiverService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service started");
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        super.onDataChanged(dataEventBuffer);
        //TODO
    }

    //Start or stop the SensorService based on the message
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        //super.onMessageReceived(messageEvent);
        Log.d(TAG,"Message has been recieved "+ messageEvent.getPath());

        //TODO Don't use constants
        if(messageEvent.getPath().equals("/start")){
            startService(new Intent(this,SensorService.class));
        }else{ //"stop"
            stopService(new Intent(this,SensorService.class));
        }
    }
}
