package com.msi.diagnostic.app;

public class MockDiagnosticSensorManager extends DiagnosticSensorManager {

    public MockDiagnosticSensorManager(IPatformSensorManager sensorManager) {
        super(sensorManager);
    }

    public void registerSensor(ISensorListener listener, int sensorType) {
    }

    public void unregisterSensor() {
    }

    @Override
    public void onAccuracyChanged() {
    }

    @Override
    public void onSensorChanged(SensorEventInfo sensorEventInfo) {
    }
}
