package com.wilsong.tumble;

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
 * Created by Gerard on 12/07/2016.
 */
public class SensorActivity extends AppCompatActivity implements View.OnClickListener {
    private SensorManager sensorManager ;
    private Sensor accel;
    private Button listenButton;



    private SensorEventListener accelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            long timeStamp = sensorEvent.timestamp;
            float value = sensorEvent.values[0];
            float[] accelerometerData = new float[3];
            String comment = timeStamp + " "+ value;
            Toast.makeText(SensorActivity.this, comment, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        // get sensor manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // get the accelerometer sensor
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listenButton = (Button) findViewById(R.id.listenButton);
        listenButton.setOnClickListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(accel !=null){
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
