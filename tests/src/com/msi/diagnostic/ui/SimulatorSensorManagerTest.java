package com.msi.diagnostic.ui;

import android.hardware.Sensor;
import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.app.SimulatorSensorManager;

public class SimulatorSensorManagerTest extends AndroidTestCase
{
    private static final String TAG = "SimulatorSensorManagerTest";
    private SimulatorSensorManager mSimulatorSensorManager;

    @Override
    protected void setUp() throws Exception
    {
        Log.d(TAG, "setUp...");
        mSimulatorSensorManager = new SimulatorSensorManager(getContext());
    }

    @Override
    protected void tearDown() throws Exception
    {
        Log.d(TAG, "tearDown...");
    }

    public void test_registerSensor_unregisterSensor()
    {
        mSimulatorSensorManager.registerSensor(3); // Sensor.TYPE_LIGHT
        assertEquals(Sensor.TYPE_LIGHT, mSimulatorSensorManager.getSensorType());
        mSimulatorSensorManager.unregisterSensor();
        assertEquals(-2, mSimulatorSensorManager.getSensorType());
    }
}
