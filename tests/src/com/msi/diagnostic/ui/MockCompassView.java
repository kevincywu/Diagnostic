package com.msi.diagnostic.ui;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.msi.diagnostic.app.CompassView;
import com.msi.diagnostic.app.SensorEventInfo;

public class MockCompassView extends CompassView {

    private float[] accelerometerValues, magneticFieldValues;

    private float ANGLE_SCREEN = 90;

    public float mDirection = 0;

    public MockCompassView(Context context) {
        super(context);
        accelerometerValues = new float[3];
        magneticFieldValues = new float[3];
    }

    @Override
    public void updateDirection(SensorEventInfo sensorEventInfo) {
        mDirection = handleCompassDirect(sensorEventInfo);
    }

    private float handleCompassDirect(SensorEventInfo sensorEventInfo) {
        switch (sensorEventInfo.TYPE_SESOR) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                magneticFieldValues = sensorEventInfo.SENSOR_EVENT_VALUE;
                break;
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerValues = sensorEventInfo.SENSOR_EVENT_VALUE;
                break;
        }
        if (magneticFieldValues != null && accelerometerValues != null) {
            float[] values = new float[3];
            float[] R = new float[9];
            SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
            SensorManager.getOrientation(R, values);

            values[0] = (float) Math.toDegrees(values[0]) + ANGLE_SCREEN;
            return values[0];
        }
        return 0;
    }
}
