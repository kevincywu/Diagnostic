
package com.msi.diagnostic.ui;

import com.msi.diagnostic.utils.SystemInfoProvider;

import android.test.AndroidTestCase;
import android.util.Log;

public class SystemInfoProviderTest extends AndroidTestCase
{
    private static final String TAG = "SystemInfoProviderTest";

    @Override
    protected void setUp() throws Exception
    {
        Log.d(TAG, "setUp...");
    }

    @Override
    protected void tearDown() throws Exception
    {
        Log.d(TAG, "tearDown...");
    }

    public void test_getBuildNumber()
    {
        assertNotNull(SystemInfoProvider.getBuildNumber());
    }

    public void test_getDate()
    {
        assertNotNull(SystemInfoProvider.getDate());
    }

    public void test_getMemorySize()
    {
        assertNotNull(SystemInfoProvider.getMemorySize());
    }

    public void test_getModel()
    {
        assertNotNull(SystemInfoProvider.getModel());
    }

    public void test_getOSVersion()
    {
        assertNotNull(SystemInfoProvider.getOSVersion());
    }

    public void test_getSerialNumber()
    {
        assertNotNull(SystemInfoProvider.getSerialNumber());
    }

    public void test_getStorageSize()
    {
        assertNotNull(SystemInfoProvider.getStorageSize());
    }

    public void test_getTime()
    {
        assertNotNull(SystemInfoProvider.getTime());
    }
}
