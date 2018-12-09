package com.example.erik.androidlabb3;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements Callback<AccelerometerListener.AccelerometerData> {

    private static final double SHAKE_TRESHOLD = 0.3;

    private AccelerometerListener accelerometerListener;
    private SensorManager manager;
    private Sensor accelerometer;
    private AccuracyFilter accuracyFilter, shakeFilter;

    private TextView valueText;
    private TextView filterFactorText;
    private TextView shakeText;
    private SeekBar filterFactorSeekBar;

    private long timePassed;

    @Override
    protected void onPause() {
        manager.unregisterListener(accelerometerListener);
        super.onPause();
    }

    @Override
    protected void onResume() {
        manager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valueText = findViewById(R.id.value_text);
        valueText.setTextSize(144);
        shakeText = findViewById(R.id.shake_text_field);
        filterFactorSeekBar = findViewById(R.id.filter_factor_seekbar);
        filterFactorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int value;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value = progress;
                filterFactorText.setText("Filter Factor: " + String.valueOf(progress / 100.0));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                accuracyFilter.reset();
                accuracyFilter = new AccuracyFilter(value / 100.0);
            }
        });

        filterFactorText = findViewById(R.id.filter_factor_text);
        filterFactorText.setText(String.valueOf(filterFactorSeekBar.getProgress() / 100.0));

        accuracyFilter = new AccuracyFilter(filterFactorSeekBar.getProgress() / 100.0);
        shakeFilter = new AccuracyFilter(0.5);
        accelerometerListener = new AccelerometerListener(this);
        manager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //Register sensor listener
        manager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);



        if(accelerometer != null){
            valueText.setText("SENSOR FOUND");
        }
        else{
            valueText.setText("SENSOR NOT FOUND");
        }
    }

    @Override
    public void callback(AccelerometerListener.AccelerometerData data) {
        double filteredResultant = shakeFilter.calculateValue(data.getResultant());
        if(filteredResultant > SHAKE_TRESHOLD){
            if(timePassed == 0){
                timePassed = System.currentTimeMillis();
            }
            else{
                if(System.currentTimeMillis() - timePassed >= 1000){
                    valueText.setTextColor(Color.RED);
                }
            }
        }
        else{
            if(timePassed != 0){
                timePassed = 0;
                valueText.setTextColor(Color.DKGRAY);
            }
        }
        int inclination = (int) accuracyFilter.calculateValue(data.getInclination()); // y pos 1 och 4, z pos 1 och 2
        valueText.setText(String.valueOf(String.valueOf(inclination)));
        shakeText.setText(String.valueOf(filteredResultant));
    }
}
