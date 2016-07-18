package com.wilsong.tumble;
// imports required
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  Method sets up a listener to acquire data readings from the accelerometer sensor.
 *  It also
 *  Created by Gerard on 12/07/2016.
 */
public class SensorActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Variable for obtaining an instance of the SensorManager class
     */
    private SensorManager sensorManager;
    /**
     * Variable to obtain an instance of a Sensor
     */
    private Sensor accel;
    /**
     * variable for button
     */
    private Button listenButton;


    /**
     *  Method sets up a new instance of the SensorEventListener called accelerometer listener
     */
    private SensorEventListener accelerometerListener = new SensorEventListener() {
        /**
         * Method to receive sensor events via the SensorEvent class.
         * The SensorEvent returns an array consisting of the float values of the X, Y, Z axis data
         * An array called accelerometerData is created to store the X, Y, Z axis values
         * @param sensorEvent
         */
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            long timeStamp = sensorEvent.timestamp;
            float value = sensorEvent.values[0];
            float[] accelerometerData = new float[3];
            accelerometerData[0] = sensorEvent.values[0];
            accelerometerData[1] = sensorEvent.values[1];
            accelerometerData[2] = sensorEvent.values[2];
            Threshold(accelerometerData);

            String comment = timeStamp + " "+ value;
            String commenter = timeStamp+ " "+ Double.toString(accelerometerData[0])+ " "+ Double.toString(accelerometerData[1])+ " "+ Double.toString(accelerometerData[2]);
            //Toast.makeText(SensorActivity.this, commenter, Toast.LENGTH_SHORT).show();
            TextView tAX = (TextView) findViewById(R.id.ax);
            TextView tAY = (TextView) findViewById(R.id.ay);
            TextView tAZ = (TextView) findViewById(R.id.az);

            accelerometerData = sensorEvent.values.clone();
            tAX.setText(Double.toString(accelerometerData[0]));
            tAY.setText(Double.toString(accelerometerData[1]));
            tAZ.setText(Double.toString(accelerometerData[2]));


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            long timeStamp =
            Log.d("MY_APP", sensor.toString() + " - " + accuracy);
        }
    };

    private void Threshold(float[] accelerometerData) {
        double x = accelerometerData[0];
        double y = accelerometerData[1];
        double z = accelerometerData[2];
        double gravity = 9.81;
        double acc = Math.sqrt(x * x + y * y + z * z);
        double gForce = acc/gravity;

        if (gForce > 4 ){
            Toast.makeText(SensorActivity.this, "Threshold hit", Toast.LENGTH_SHORT).show();
        }



    }

    /**
     * Method initialises the activity_sensor.xml
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        // Get an instance of the SensorManager class
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // Query for the accelerometer sensor
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // set the listenButton to type button and link it with the listenButton id in the activitySensor xml
        listenButton = (Button) findViewById(R.id.listenButton);
        // set the on click listener for the listenButton
        listenButton.setOnClickListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(accel !=null){
            // register the sensor listener to pick up updates in the accelerometer data using the default update rate (1 reading per second)
            sensorManager.registerListener(accelerometerListener, accel, sensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(accel != null){
            sensorManager.unregisterListener(accelerometerListener);
        }

    }

    @Override
    public void onClick(View view) {
        int time = 20;
        AlarmManager sensorScheduler = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent sensorIntent = new Intent(getApplicationContext(), SensorLoggerActivity.class);
        PendingIntent scheduledSensorIntent = PendingIntent.getService(getApplicationContext(), 0, sensorIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        sensorScheduler.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), time, scheduledSensorIntent);
    }
}
