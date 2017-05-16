package com.example.accel.accelerometertest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by aawesh on 5/15/17.
 */

public class Client {

    public static Client instance;
    private GoogleApiClient googleApiClient;
    private Context context;
    private ExecutorService executorService;

    private static final int CONNECTION_TIMEOUT = 1500;
    private static final String TAG = "Client";

    public static synchronized Client getInstance(Context context){
        if(instance == null){
            instance = new Client(context.getApplicationContext());
        }
        return instance;
    }

    public Client(Context context){
        this.context = context;
        this.googleApiClient = new GoogleApiClient.Builder(context).addApi(Wearable.API).build();
        this.executorService = Executors.newCachedThreadPool();
    }

    protected boolean validateConnection(){
        if(googleApiClient.isConnected()){
            return true;
        }
        ConnectionResult result = googleApiClient.blockingConnect(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
        return result.isSuccess();
    }

    public void sendSensorData(final int sensorType, final int accuracy, final long timestamp, final float[] values){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                sendSensorDataInBackground(sensorType,accuracy,timestamp,values);
            }
        });
    }

    private void sendSensorDataInBackground(final int sensorType, int accuracy, long timestamp, final float[] values) {
        PutDataMapRequest dataMap = PutDataMapRequest.create("/sensors/"+sensorType);

        dataMap.getDataMap().putInt("accuracy",accuracy);
        dataMap.getDataMap().putLong("timestamp",timestamp);
        dataMap.getDataMap().putFloatArray("values",values);

        PutDataRequest putDataRequest = dataMap.asPutDataRequest();
        send(putDataRequest);
    }

    private void send(PutDataRequest putDataRequest) {
        if(validateConnection()){
            Wearable.DataApi.putDataItem(googleApiClient,putDataRequest).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                @Override
                public void onResult(DataApi.DataItemResult dataItemResult) {
                    Log.v(TAG, "Sending sensor data: " + dataItemResult.getStatus().isSuccess());
                }
            });
        }
    }


}
