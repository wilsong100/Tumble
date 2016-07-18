package com.wilsong.tumble;


import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.*;

import com.jjoe64.graphview.GraphView;

/**
 * Created by Gerard on 18/07/2016.
 */
public class GraphAccel_Activity extends AppCompatActivity implements SensorEventListener {

    private float mLastX, mLastY, mLastZ;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private GraphViewSeries seriesX;

}