package com.example.christoffer.sensorapp;


import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LightActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager sensorManager;
    private Sensor mLight;
    private View view;
    private TextView textEditor;
    private TextView infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        view = findViewById(R.id.wrapper);
        textEditor = (TextView) findViewById(R.id.textView);
        infoText = (TextView) findViewById(R.id.infoText);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LIGHT){
            getLightSensor(event);
        }
    }

    private void getLightSensor(SensorEvent event){
        float lightValue = event.values[0];
        int i = 0;
        double value = 36.42857142857143;
        textEditor.setText("" + lightValue);
        if(lightValue > 20000){
            i = 6;
            textEditor.setText("Light level 7");
        } else if(lightValue > 10000){
            i = 5;
            textEditor.setText("Light level 6");
        } else if(lightValue > 300){
            i = 4;
            textEditor.setText("Light level 5");
        } else if(lightValue > 100){
            textEditor.setText("Light level  4");
            i = 3;
        } else if(lightValue > 50){
            i = 2;
            textEditor.setText("Light level 3");
        } else if(lightValue > 5){
            i = 1;
            textEditor.setText("Light level 2");
        } else {
            i = 0;
            textEditor.setText("Light level 1");
        }
        view.setBackgroundColor(Color.rgb((int)value*i,(int)value*i,(int)value*i));
        textEditor.setTextColor(Color.rgb((int)value*(7-i),(int)value*(7-i),(int)value*(7-i)));
        infoText.setTextColor(Color.rgb((int)value*(7-i),(int)value*(7-i),(int)value*(7-i)));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        sensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
    }
}
