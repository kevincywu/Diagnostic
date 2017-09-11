
package com.msi.diagnostic.ui;

import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.data.ThresholdCriteria;

public class ThresholdCriteriaTest extends AndroidTestCase
{
    private static final String TAG = "ThresholdCriteriaTest";
    private ThresholdCriteria mThresholdCriteria;
    private float mMax = (float) 1000.0;
    private float mMin = (float) 10.0;

    @Override
    protected void setUp() throws Exception
    {
        Log.d(TAG, "setUp...");
        mThresholdCriteria = new ThresholdCriteria(mMax, mMin);
    }

    @Override
    protected void tearDown() throws Exception
    {
        Log.d(TAG, "tearDown...");
    }

    public void test_getMax()
    {
        assertEquals(mMax, mThresholdCriteria.getMax());
    }

    public void test_getMin()
    {
        assertEquals(mMin, mThresholdCriteria.getMin());
    }
}
