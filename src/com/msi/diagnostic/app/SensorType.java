package com.msi.diagnostic.app;

public class SensorType {
    public static final int TYPE_G_SENSOR = 0;

    public static final int TYPE_GYROSOPE_SENSOR = 1;

    public static final int TYPE_COMPASS_SENSOR = 2;

    public static final int TYPE_LIGHT_SENSOR = 3;

    public static final int TYPE_UNKNOWN = 4;

    public static final String SENSOR_TYPES[] = {
            "accelerometerSensor", "gyroscopeSensor", "compassSensor", "lightSensor", "UNKNOWN"
    };
}
