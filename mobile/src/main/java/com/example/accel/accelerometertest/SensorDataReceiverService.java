package com.example.accel.accelerometertest;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.Arrays;

/**
 * Created by aawesh on 5/16/17.
 */

public class SensorDataReceiverService extends WearableListenerService {

    private final static String TAG = "SensorDataReceiverService";

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "Recieved new accelerometer data : onDataChanged() called");

        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataItem dataItem = dataEvent.getDataItem();
                Uri uri = dataItem.getUri();
                String path = uri.getPath();

                if (path.startsWith("/sensors/")) {
                    unpackSensorData(
                            Integer.parseInt(uri.getLastPathSegment()),
                            DataMapItem.fromDataItem(dataItem).getDataMap()
                    );
                }
            }
        }
    }

    private void unpackSensorData(int sensorType, DataMap dataMap) {
        int accuracy = dataMap.getInt("accuracy");
        long timestamp = dataMap.getLong("timestamp");
        float[] values = dataMap.getFloatArray("values");

        Log.d(TAG, "SensorType = " + sensorType + " Accuracy = " + accuracy + " timestamp = " + timestamp + " values = " + Arrays.toString(values));

        //TODO save those sensor data values and do some further processing
    }
}
