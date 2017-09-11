
package com.msi.diagnostic.ui;

import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.app.DiagnosticApp;

public class DiagnosticAppTest extends AndroidTestCase
{
    private static final String TAG = "DiagnosticAppTest";
    private DiagnosticApp mApp;

    @Override
    protected void setUp() throws Exception
    {
        Log.d(TAG, "setUp...");
        mApp = (DiagnosticApp) getContext().getApplicationContext();
    }

    @Override
    protected void tearDown() throws Exception
    {
        Log.d(TAG, "tearDown...");
    }

    public void test_getAppContext()
    {
        assertNotNull(mApp.getAppContext());
    }

    public void test_getDeviceConfig()
    {
        assertNotNull(mApp.getDeviceConfig());
    }

    public void test_getVerifierVisitor()
    {
        assertNotNull(mApp.getVerifierVisitor());
    }

    public void test_getDiagnoseModel()
    {
        assertNotNull(mApp.getDiagnoseModel());
    }

    public void test_getPrivatePreferences()
    {
        assertNotNull(mApp.getPrivatePreferences());
    }

    public void test_getSensorManager()
    {
        assertNotNull(mApp.getSensorManager());
    }
}
