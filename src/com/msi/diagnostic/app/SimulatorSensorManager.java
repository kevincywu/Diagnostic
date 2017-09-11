package com.msi.diagnostic.app;

import org.openintents.sensorsimulator.hardware.Sensor;
import org.openintents.sensorsimulator.hardware.SensorEvent;
import org.openintents.sensorsimulator.hardware.SensorEventListener;
import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.StrictMode;

public class SimulatorSensorManager implements SensorEventListener, IPatformSensorManager {
    private IPatformSensorListener mListener;

    private SensorManagerSimulator mSensorManager;

    private SensorEventInfo mSensorEventInfo;

    private Context mContext;

    private int mSensorType;

    public SimulatorSensorManager(Context context) {
        this.mContext = context;
    }

    public void setSensorListener(IPatformSensorListener listener) {
        this.mListener = listener;
    }

    public void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
                .detectDiskWrites().detectNetwork().penaltyLog().build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().penaltyLog().penaltyDeath()
                .build());
    }

    @Override
    public void registerSensor(int sensorType) {
        setStrictMode();

        mSensorManager = SensorManagerSimulator.getSystemService(mContext, Context.SENSOR_SERVICE);
        mSensorManager.connectSimulator();
//        this.mSensorType = sensorType;
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
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // ignore the operation when the accuracy of sensor changed
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mSensorEventInfo.SENSOR_EVENT_VALUE = event.values;
        mSensorEventInfo.TYPE_SESOR = event.type;

        this.mListener.onSensorChanged(mSensorEventInfo);
    }

    public int getSensorType() {
        return mSensorType;
    }
}
