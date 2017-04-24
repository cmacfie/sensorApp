package com.example.christoffer.sensorapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {

    private ImageView needle;
    private SensorManager sensorManager;
    private Sensor sensorGravity;
    private Sensor sensorMagnetic;
    private float[] lastGravity = new float[3];
    private float[] lastMagnetic = new float[3];
    private boolean gravityHasBeenSet = false;
    private boolean magneticHasBeenSet = false;
    private float[] rotateMatrix = new float[9];
    private float[] orient = new float[3];
    private float currDegree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        needle = (ImageView) findViewById(R.id.needle);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            for(int i = 0; i < event.values.length; i++){
                lastGravity[i] = event.values[i];
            }
            gravityHasBeenSet = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            for(int i = 0; i < event.values.length; i++){
                lastMagnetic[i] = event.values[i];
            }
            magneticHasBeenSet = true;
        }
        if (magneticHasBeenSet && gravityHasBeenSet) {
            SensorManager.getRotationMatrix(rotateMatrix, null, lastGravity, lastMagnetic);
            SensorManager.getOrientation(rotateMatrix, orient);
            float currRadians = orient[0];
            float newDegrees = -(float)(Math.toDegrees(currRadians)+360)%360;
            RotateAnimation ra = new RotateAnimation(
                    currDegree,
                    -newDegrees,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            ra.setDuration(500);

            ra.setFillAfter(true);

            needle.startAnimation(ra);
            currDegree = newDegrees;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorGravity, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, sensorMagnetic, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, sensorGravity);
        sensorManager.unregisterListener(this, sensorMagnetic);
    }
}
