package com.wilsong.tumble;
//imports
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Class sets up a service to run in the background being launched regularly via an intent and
 * using a repeating alarm via AlarmManager
 * Created by Gerard on 12/07/2016.
 */
public class SensorLoggerActivity extends Service implements SensorEventListener{

    private static final String DEBUG_TAG = "AccelLoggerService";
    /**
     * Variable for obtaining an instance of the SensorManager class
     */
    private SensorManager sensorManager;
    /**
     * Variable to obtain an instance of a Sensor
     */
    private Sensor sensor;

    /**
     * Method starts the service
     * @param intent
     * @param flags
     * @param startID
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long timeStamp = sensorEvent.timestamp;
        float value = sensorEvent.values[0];
        float[] accelerometerData = new float[3];
        String comment = timeStamp + " "+ value;

        sensorManager.unregisterListener(this);
        stopSelf();
    }

    private class SensorLoggerTask extends AsyncTask<SensorEvent, Void, Void>{
        @Override
        protected Void doInBackground(SensorEvent... sensorEvents) {
            SensorEvent event = sensorEvents[0];
                //log the value
            return null;
        }


    }

}
