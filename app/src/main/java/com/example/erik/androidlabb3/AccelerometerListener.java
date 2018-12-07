package com.example.erik.androidlabb3;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class AccelerometerListener implements SensorEventListener {

    private Callback callbackActivity;

    public AccelerometerListener(Callback<Double[]> callback) {
        this.callbackActivity = callback;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];
        long time = event.timestamp;
        callbackActivity.callback(new Double[]{x, y, z});
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
