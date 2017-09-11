
package com.msi.diagnostic.ui;

import android.hardware.Sensor;
import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.app.HardwareSensorManager;

public class HardwareSensorManagerTest extends AndroidTestCase
{
    private static final String TAG = "HardwareSensorManagerTest";
    private HardwareSensorManager mHardwareSensorManager;

    @Override
    protected void setUp() throws Exception
    {
        Log.d(TAG, "setUp...");
        mHardwareSensorManager = new HardwareSensorManager(getContext());
    }

    @Override
    protected void tearDown() throws Exception
    {
        Log.d(TAG, "tearDown...");
    }

    public void test_registerSensor()
    {
        mHardwareSensorManager.registerSensor(3); // Sensor.TYPE_LIGHT
        assertEquals(Sensor.TYPE_LIGHT, mHardwareSensorManager.getSensorType());
    }

    public void test_unregisterSensor()
    {
        mHardwareSensorManager.registerSensor(3); // Sensor.TYPE_LIGHT
        mHardwareSensorManager.unregisterSensor();
        assertEquals(-2, mHardwareSensorManager.getSensorType());
    }
}
