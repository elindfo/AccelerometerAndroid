package com.example.erik.androidlabb3;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class AccelerometerListener implements SensorEventListener {

    private Callback callbackActivity;

    public AccelerometerListener(Callback<Double[]> callback) {
        this.callbackActivity = callback;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double[] g = convertFloatsToDoubles(event.values.clone());
        //Log.d("ACCEL", "g[0]x: " + g[0] + " g[1]y: " + g[1] + " g[2]z: " + g[2] + " g[3]w: " + g[3]);
        double normOfG = Math.sqrt(g[0] * g[0] + g[1] * g[1] + g[2] * g[2]);
        g[0] = g[0] / normOfG;
        g[1] = g[1] / normOfG;
        g[2] = g[2] / normOfG;

        double x = g[0];
        double y = g[1];
        double z = g[2];

        double inclination = Math.round(Math.toDegrees(Math.acos(z)));
        Log.d("ACCEL","x: " + x + " y: " + y + " z: " + z + " inclination: " + inclination);
        long time = event.timestamp;

        callbackActivity.callback(new Double[]{x, y, z, inclination});
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private double[] convertFloatsToDoubles(float[] input)
    {
        if (input == null)
            return null;

        double[] output = new double[input.length];

        for (int i = 0; i < input.length; i++)
            output[i] = input[i];

        return output;
    }
}
