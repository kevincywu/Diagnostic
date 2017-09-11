package com.msi.diagnostic.app;

public interface IPatformSensorManager {

    public void registerSensor(int sensorType);

    public void unregisterSensor();

    public void setSensorListener(IPatformSensorListener listener);
}
