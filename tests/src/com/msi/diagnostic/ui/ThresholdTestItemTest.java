package com.msi.diagnostic.ui;

import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.app.IDiagnosticApp;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.data.ThresholdTestItem;
import com.msi.diagnostic.data.ThresholdVerifiedInfo;

public class ThresholdTestItemTest extends AndroidTestCase
{
    private static final String TAG = "THresholdTestItemTest";
    private ThresholdTestItem mThresholdTestItem;

    @Override
    protected void setUp() throws Exception
    {
        Log.d(TAG, "setUp...");
        IDiagnosticApp app = (IDiagnosticApp) getContext().getApplicationContext();
        String itemName = "isXPosVectorExceeded";
        String parentLevelName = TestLevel.TYPES[TestLevel.TYPE_PCBA];
        String parentTestCaseName = "com.msi.diagnostic.ui.GSensorTestCaseView";
        float max = (float) 9000.0;
        float min = (float) 4.0;
        TestResult result = TestResult.create(TestResult.NONE);
        mThresholdTestItem = new ThresholdTestItem(
                app,
                0,
                itemName,
                null,
                parentLevelName,
                parentTestCaseName,
                max,
                min,
                result);
    }

    @Override
    protected void tearDown() throws Exception
    {
        Log.d(TAG, "tearDown...");
    }

    public void test_getMax()
    {
        assertEquals((float) 9000.0, mThresholdTestItem.getMax());
    }

    public void test_getMin()
    {
        assertEquals((float) 4.0, mThresholdTestItem.getMin());
    }

    public void test_reload()
    {
        mThresholdTestItem.reload();
        assertEquals(TestResult.NONE, mThresholdTestItem.getResult());
    }

    public void test_verify()
    {
        ThresholdVerifiedInfo info1 = new ThresholdVerifiedInfo((float) 1000);
        ThresholdVerifiedInfo info2 = new ThresholdVerifiedInfo((float) 3.0);
        ThresholdVerifiedInfo info3 = new ThresholdVerifiedInfo((float) 9050.0);
        mThresholdTestItem.verify(info1);
        assertEquals(TestResult.PASS, mThresholdTestItem.getResult());
        mThresholdTestItem.verify(info2);
        assertEquals(TestResult.FAIL, mThresholdTestItem.getResult());
        mThresholdTestItem.verify(info3);
        assertEquals(TestResult.FAIL, mThresholdTestItem.getResult());
    }
}
