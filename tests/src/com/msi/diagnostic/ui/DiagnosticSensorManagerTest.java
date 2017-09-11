
package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.DiagnosticSensorManager;
import com.msi.diagnostic.app.HardwareSensorManager;

import android.hardware.Sensor;
import android.test.AndroidTestCase;
import android.util.Log;

public class DiagnosticSensorManagerTest extends AndroidTestCase
{
    private static final String TAG = "DiagnosticSensorManagerTest";
    private DiagnosticSensorManager mDiagnosticSensorManager;
    private HardwareSensorManager mSensorManager;

    @Override
    protected void setUp() throws Exception
    {
        Log.d(TAG, "setUp...");
        mSensorManager = new HardwareSensorManager(getContext());
        mDiagnosticSensorManager = new DiagnosticSensorManager(mSensorManager);
    }

    @Override
    protected void tearDown() throws Exception
    {
        Log.d(TAG, "tearDown...");
    }

    public void test_registerSensor()
    {
        mDiagnosticSensorManager.registerSensor(null, 3);
        assertEquals(Sensor.TYPE_LIGHT, mSensorManager.getSensorType());
    }

    public void test_unRegisterSensor()
    {
        mDiagnosticSensorManager.registerSensor(null, 3);
        mDiagnosticSensorManager.unregisterSensor();
        assertEquals(-2, mSensorManager.getSensorType());
    }
}
