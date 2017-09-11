package com.msi.diagnostic.ui;

import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.app.CompassView;
import com.msi.diagnostic.app.SensorEventInfo;

public class CompassViewTest extends AndroidTestCase {
    private static String TAG = "CompassViewTest";
    private CompassView mCompassView;

    @Override
    protected void setUp() throws Exception {
        Log.d(TAG, "setUp...");
        mCompassView = new CompassView(getContext());
    }

    public void test_updateDirection() {
        SensorEventInfo sei = new SensorEventInfo();
        assertTrue(mCompassView.getFirstDraw());
        mCompassView.updateDirection(sei);
        assertFalse(mCompassView.getFirstDraw());
    }

    @Override
    protected void tearDown() throws Exception {
        Log.d(TAG, "tearDown...");
    }

}
