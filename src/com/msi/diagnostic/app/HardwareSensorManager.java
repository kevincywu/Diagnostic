package com.msi.diagnostic.app;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class HardwareSensorManager implements SensorEventListener, IPatformSensorManager {
    private IPatformSensorListener mListener;

    private SensorManager mSensorManager;

    private SensorEventInfo mSensorEventInfo;

    private Context mContext;
    private int mSensorType;

    public HardwareSensorManager(Context context) {
        this.mContext = context;
    }

    @Override
    public void setSensorListener(IPatformSensorListener listener) {
        this.mListener = listener;
    }

    @Override
    public void registerSensor(int sensorType) {
        mSensorManager = (SensorManager) this.mContext.getSystemService(Context.SENSOR_SERVICE);
        mSensorEventInfo = new SensorEventInfo();
        switch (sensorType) {
            case 0:
                mSensorManager.registerListener(this,
                        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_GAME);
                mSensorType = Sensor.TYPE_ACCELEROMETER;
                break;
            case 1:
                mSensorManager.registerListener(this,
                        mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                        SensorManager.SENSOR_DELAY_GAME);
                mSensorType = Sensor.TYPE_GYROSCOPE;
                break;
            case 2:
                mSensorManager.registerListener(this,
                        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_GAME);
                mSensorManager.registerListener(this,
                        mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                        SensorManager.SENSOR_DELAY_GAME);
                mSensorType = Sensor.TYPE_MAGNETIC_FIELD;
            case 3:
                mSensorManager.registerListener(this,
                        mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                        SensorManager.SENSOR_DELAY_GAME);
                mSensorType = Sensor.TYPE_LIGHT;
                break;
            default:
                break;
        }
    }

    @Override
    public void unregisterSensor() {
        mSensorManager.unregisterListener(this);
        mSensorType = -2;
    };

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // ignore the operation when the accuracy of sensor changed
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mSensorEventInfo.SENSOR_EVENT_VALUE = event.values;
        mSensorEventInfo.TYPE_SESOR = event.sensor.getType();

        this.mListener.onSensorChanged(mSensorEventInfo);

    }

    public int getSensorType() {
        return mSensorType;
    }
}
