package com.example.ryu.walkpast.controller;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * STEP COUNTER SENSOR WITH LISTENER
 * counts the steps the user has made and hands over to Main Activity.
 * Created by RYU on 9/22/2017.
 */

public class StepCounter implements SensorEventListener {

    private static final String TAG = StepCounter.class.getSimpleName();

    /*
    GIVE MAIN ACTIVITY STEP COUNT
     */
    public interface Listener {
        void onStepChanged(int steps);
    }

    private final SensorManager mSensorManager;
    private final Sensor mStepSensor;
    private int mLastAccuracy;
    private Listener mListener;
    private int totalSteps;
    private int steps;

    /*
    INITIALIZE STEP COUNTER
     */
    public StepCounter(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Activity.SENSOR_SERVICE);
        mStepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    /*
    START SENSOR EVENT LISTENING
     */
    public void startListening(Listener listener) {
        if (mListener == listener) {
            return;
        }
        mListener = listener;
        if (mStepSensor == null) {
            Log.d(TAG, "No sensor");
            return;
        }
        mSensorManager.registerListener(this, mStepSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    /*
    STOP SENSOR EVENT LISTENING
     */
    public void stopListening() {
        mSensorManager.unregisterListener(this);
        mListener = null;
    }

    /*
    ON SENSOR EVENT CHANGE UPDATE STEP COUNT
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (mListener == null) {
            return;
        }
        if (mLastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }

        //counter returns total number since registered so we need to subtract the initial amount
        if (event.sensor == mStepSensor) {
            if (totalSteps < 1) {
                // initial value
                totalSteps = Math.round(event.values[0]);
            }
            steps = Math.round(event.values[0]) - totalSteps;
            mListener.onStepChanged(steps);
        }

    }

    /*
    SETTING SENSOR ACCURACY
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (mLastAccuracy != accuracy) {
            mLastAccuracy = accuracy;
        }
    }

}
