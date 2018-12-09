package com.example.erik.androidlabb3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements Callback<Double[]> {

    private AccelerometerListener accelerometerListener;
    private SensorManager manager;
    private Sensor accelerometer;
    private AccuracyFilter accuracyFilter;

    private TextView valueText;
    private TextView filterFactorText;
    private SeekBar filterFactorSeekBar;

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
    public void callback(Double[] d) {
        /*if(valueText != null){
            int degrees;
            if(d[1] >= 0 && d[2] >= 0){ //Kvadrant 1
                degrees = (int)(accuracyFilter.calculateValue(d[2]) * (90/9.82));
            }
            else if(d[1] < 0 && d[2] >= 0){ //Kvadrant 2
                degrees = 180 - (int)(accuracyFilter.calculateValue(d[2]) * (90/9.82));
            }
            else if(d[1] < 0 && d[2] < 0){ //Kvadrant 3
                degrees = -180 - (int)(accuracyFilter.calculateValue(d[2]) * (90/9.82));
            }
            else{
                degrees = (int)(accuracyFilter.calculateValue(d[2]) * (90/9.82));
            }
            valueText.setText(String.valueOf(degrees));
        }*/
        int degrees = (int) accuracyFilter.calculateValue(d[3]); 
        if(d[1] < 0 && d[2] <0){
            degrees = 360 - degrees;
        }
        if(d[1] <0 && d[2] >0){
            degrees = 360 - degrees;
        }
        valueText.setText(String.valueOf(String.valueOf(degrees)));

    }
}
