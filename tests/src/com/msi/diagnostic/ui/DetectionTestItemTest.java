package com.msi.diagnostic.ui;

import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.app.IDiagnosticApp;
import com.msi.diagnostic.data.DetectionTestItem;
import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

public class DetectionTestItemTest extends AndroidTestCase
{
    private static final String TAG = "DetectionTestItemTest";
    private DetectionTestItem mDetectionTestItem;

    @Override
    protected void setUp() throws Exception
    {
        Log.d(TAG, "setUp...");
        IDiagnosticApp app = (IDiagnosticApp) getContext().getApplicationContext();
        String itemName = "isVibrated";
        String parentLevelName = TestLevel.TYPES[TestLevel.TYPE_PCBA];
        String parentTestCaseName = "com.msi.diagnostic.ui.MotorTestCaseView";
        boolean mustBeDetected = true;
        TestResult result = TestResult.create(TestResult.NONE);
        mDetectionTestItem = new DetectionTestItem(
                app,
                itemName,
                null,
                parentLevelName,
                parentTestCaseName,
                mustBeDetected,
                result);
    }

    @Override
    protected void tearDown() throws Exception
    {
        Log.d(TAG, "tearDown...");
    }

    public void test_isMustBeDetected()
    {
        boolean result = true;
        assertEquals(result, mDetectionTestItem.isMustBeDetected());
    }

    public void test_reload()
    {
        mDetectionTestItem.reload();
        assertEquals(TestResult.NONE, mDetectionTestItem.getResult());
    }

    public void test_verify()
    {
        DetectionVerifiedInfo info1 = new DetectionVerifiedInfo(true);
        mDetectionTestItem.verify(info1);
        assertEquals(TestResult.PASS, mDetectionTestItem.getResult());
        DetectionVerifiedInfo info2 = new DetectionVerifiedInfo(false);
        mDetectionTestItem.verify(info2);
        assertEquals(TestResult.FAIL, mDetectionTestItem.getResult());
    }
}
