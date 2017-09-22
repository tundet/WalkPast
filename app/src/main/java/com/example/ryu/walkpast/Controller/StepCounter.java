package com.example.ryu.walkpast.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * StepCounter sensor with listener.
 * Created by RYU on 9/22/2017.
 */

public class StepCounter implements SensorEventListener {

    private static final String TAG = StepCounter.class.getSimpleName();

    public interface Listener {
        void onStepChanged(int steps);
    }

    private final SensorManager mSensorManager;

    @Nullable
    private final Sensor mStepSensor;

    private int mLastAccuracy;
    private Listener mListener;
    private int totalSteps;
    private int steps;

    public StepCounter(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Activity.SENSOR_SERVICE);
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mStepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

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

    public void stopListening() {
        mSensorManager.unregisterListener(this);
        mListener = null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (mListener == null) {
            return;
        }
        if (mLastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }

        //counter returns total number since registered so we need to substract the initial amount
        if (event.sensor == mStepSensor) {
            if (totalSteps < 1) {
                // initial value
                totalSteps = Math.round(event.values[0]);
            }
            steps = Math.round(event.values[0]) - totalSteps;
            mListener.onStepChanged(steps);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (mLastAccuracy != accuracy) {
            mLastAccuracy = accuracy;
        }
    }

}
