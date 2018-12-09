package com.example.erik.androidlabb3;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class AccelerometerListener implements SensorEventListener {

    private Callback callbackActivity;

    public AccelerometerListener(Callback<AccelerometerData> callback) {
        this.callbackActivity = callback;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //https://stackoverflow.com/questions/11175599/how-to-measure-the-tilt-of-the-phone-in-xy-plane-using-accelerometer-in-android?fbclid=IwAR2RDNlBMFV7oXy54L_Z8QM5XxtCVsYYhRN9DFL88a7aXWnm19pgCIWyIYA
        double[] g = convertFloatsToDoubles(event.values.clone());
        double normOfG = Math.abs(Math.sqrt(g[0] * g[0] + g[1] * g[1] + g[2] * g[2]));
        double x = g[0] / normOfG;
        double y = g[1] / normOfG;
        double z = g[2] / normOfG;

        double inclination = Math.round(Math.toDegrees(Math.acos(y)));

        long time = event.timestamp;

        callbackActivity.callback(new AccelerometerData(x, y, z, inclination, time));
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

    class AccelerometerData{

        private double x;
        private double y;
        private double z;
        private double inclination;
        private long time;

        public AccelerometerData(double x, double y, double z, double inclination, long time) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.inclination = inclination;
            this.time = time;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getZ() {
            return z;
        }

        public void setZ(double z) {
            this.z = z;
        }

        public double getInclination() {
            return inclination;
        }

        public void setInclination(double inclination) {
            this.inclination = inclination;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
}
