package com.msi.diagnostic.app;

public class DiagnosticSensorManager implements IPatformSensorListener {
    private ISensorListener mListener;

    private IPatformSensorManager mSensorManager;

    public DiagnosticSensorManager(IPatformSensorManager sensorManager) {
        this.mSensorManager = sensorManager;
    }

    public void registerSensor(ISensorListener listener, int sensorType) {
        this.mListener = listener;
        mSensorManager.registerSensor(sensorType);
    }

    public void unregisterSensor() {
        mSensorManager.unregisterSensor();
    }

    @Override
    public void onAccuracyChanged() {
        // ignore the operation when the accuracy of sensor changed
    }

    @Override
    public void onSensorChanged(SensorEventInfo sensorEventInfo) {
        this.mListener.onSensorChanged(sensorEventInfo);
    }

}
