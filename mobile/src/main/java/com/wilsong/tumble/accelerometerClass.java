package com.wilsong.tumble;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Gerard on 17/07/2016.
 */
public class accelerometerClass extends AppCompatActivity implements SensorEventListener {

    private float lastX, lastY, lastZ;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float mX = 0;
    private float mY = 0;
    private float mZ = 0;

    private float cX = 0;
    private float cY = 0;
    private float cZ = 0;

    private float vibrateThreshold = 0;

    private TextView currentX, currentY, currentZ, maxX, maxY, maxZ;

    public Vibrator vibrator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        initialiseViews();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            //success! accelerometer working
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            vibrateThreshold = accelerometer.getMaximumRange() / 2;
        } else {
            Toast.makeText(accelerometerClass.this, "Fail, no accelerometer", Toast.LENGTH_SHORT).show();
        }
        //initialise the vibrate
        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

    }

    private void initialiseViews() {
        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);

        maxX = (TextView) findViewById(R.id.maxX);
        maxY = (TextView) findViewById(R.id.maxY);
        maxZ = (TextView) findViewById(R.id.maxZ);
    }

    // onResume() register the accelerometer for listening to events
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, sensorManager.SENSOR_DELAY_NORMAL);
    }

    // onPause() unregister the accelerometer to stop listening to events
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // clean current values
        displayCleanValues();
        // display the current x, y, z values
        displayCurrentValues();
        // display the max x, y, z values
        displayMaxValues();

        // get the change of the x, y, z values
        cX = Math.abs(lastX - event.values[0]);
        cY = Math.abs(lastY - event.values[1]);
        cZ = Math.abs(lastZ - event.values[2]);

        // if the change is below 2, it is just noise
        if (cX < 2)
            cX = 0;
        if (cY < 2)
            cY = 0;
        if ((cX > vibrateThreshold) || (cY > vibrateThreshold) || (cZ > vibrateThreshold)) {
            vibrator.vibrate(50);
        }
    }

    private void displayCleanValues() {
        currentX.setText("0.0");
        currentY.setText("0.0");
        currentZ.setText("0.0");
    }

    // display the current x, y, z values
    private void displayCurrentValues() {
        currentX.setText(Float.toString(cX));
        currentY.setText(Float.toString(cY));
        currentZ.setText(Float.toString(cZ));
    }

    private void displayMaxValues() {
        if (cX > mX){
            mX = cX;
            maxX.setText(Float.toString(mX));
        }
        if (cY > mY){
            mY = cY;
            maxY.setText(Float.toString(mY));
        }
        if (cZ > mZ){
            mZ = cZ;
            maxZ.setText(Float.toString(mZ));
        }
    }
}