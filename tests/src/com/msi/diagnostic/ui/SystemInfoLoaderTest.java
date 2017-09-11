package com.msi.diagnostic.ui;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.app.SystemInfoLoader;

public class SystemInfoLoaderTest extends AndroidTestCase {
    private static String TAG = "SystemInfoLoaderTest";
    private SystemInfoLoader mSysInfoLoader;
    private String mAppVersion;

    @Override
    protected void setUp() throws Exception {
        Log.d(TAG, "setUp...");
        PackageInfo packageInfo;
        try {
            packageInfo = getContext().getPackageManager().getPackageInfo(
                    getContext().getPackageName(), 0);
            mAppVersion = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        mSysInfoLoader = new SystemInfoLoader(mAppVersion);
    }

    public void test_getInfo() {
        assertNotNull(mSysInfoLoader.getInfo());
    }

    @Override
    protected void tearDown() throws Exception {
        Log.d(TAG, "tearDown...");
    }

}
