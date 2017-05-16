package com.example.accel.accelerometertest;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by aawesh on 5/15/17.
 */

public class HandleMessage{
    String TAG = "HandleMessage";

    private GoogleApiClient googleApiClient;
    private Context context;
    private static HandleMessage instance;
    private ExecutorService executorService;

    private static final int CONNECTION_TIMEOUT = 15000;

    public static synchronized HandleMessage getInstance(Context context){
        if(instance == null){
            instance = new HandleMessage(context.getApplicationContext());
        }
        return instance;
    }

    private HandleMessage(Context context){
        this.context = context;
        this.googleApiClient = new GoogleApiClient.Builder(context).addApi(Wearable.API).build();
        this.executorService = Executors.newCachedThreadPool();
    }

    public void startMeasurement(){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                controlMeasurementInBackground("/start");
            }
        });
    }


    public void stopMeasurement(){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                controlMeasurementInBackground("/stop");
            }
        });
    }

    //check if the node is connected or not
    private boolean validateConnection(){
        if(googleApiClient.isConnected()){
            Log.d(TAG,"connection established");
            return true;
        }else{
            ConnectionResult result = googleApiClient.blockingConnect(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
            Log.d(TAG,"First time connection established");
            return result.isSuccess();
        }
    }

    //send start or stop control signal to wearable
    private void controlMeasurementInBackground(final String path){
        if (validateConnection()) {
            List<Node> nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await().getNodes();

            for (Node node : nodes) {
                Wearable.MessageApi.sendMessage(
                        googleApiClient, node.getId(), path, null
                ).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                    @Override
                    public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                        Log.d(TAG, "controlMeasurementInBackground(" + path + "): " + sendMessageResult.getStatus().isSuccess());
                    }
                });
            }
        } else {
            Log.w(TAG, "No connection possible");
        }
    }


}
