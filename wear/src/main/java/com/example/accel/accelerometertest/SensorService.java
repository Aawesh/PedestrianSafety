package com.example.accel.accelerometertest;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by aawesh on 5/16/17.
 */

public class SensorService extends Service implements SensorEventListener {

    private TextView mTextView;
    private static final String TAG = "AccelerometerTest";

    private final static int c_accelerometer = Sensor.TYPE_ACCELEROMETER;

    SensorManager mSensorManager;
    private Client client;

    @Override
    public void onCreate() {
        super.onCreate();

        client = Client.getInstance(this);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Sensor Dashboard");
        builder.setContentText("Collecting sensor data..");
        builder.setSmallIcon(R.mipmap.ic_launcher);

        startForeground(1, builder.build());

        startMeasurement();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopMeasurement();
    }

    private void stopMeasurement() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }

    }

    private void startMeasurement() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(c_accelerometer);

        if(accelerometerSensor != null){
            mSensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }else{
            Log.w(TAG,"Accelerometer not found");
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        client.sendSensorData(event.sensor.getType(), event.accuracy, event.timestamp, event.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
