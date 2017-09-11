package com.msi.diagnostic.app;

public interface IPatformSensorListener {
    void onAccuracyChanged();

    void onSensorChanged(SensorEventInfo sensorEventInfo);
}
