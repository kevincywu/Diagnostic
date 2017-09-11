package com.msi.diagnostic.data;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockThresholdTestItem extends ThresholdTestItem {

    private float mMockMin;
    private float mMockMax;

    public MockThresholdTestItem(
            IDiagnosticApp app,
            long id,
            String itemName,
            String itemFile,
            String parentLevelName,
            String parentCaseName,
            float max,
            float min,
            TestResult result) {

        super(
                app,
                TestObject.NONE_ID,
                itemName,
                itemFile,
                parentLevelName,
                parentCaseName,
                max,
                min,
                result);

        mMockMin = min;
        mMockMax = max;

    }

    @Override
    public float getMin() {
        return mMockMin;
    }

    @Override
    public float getMax() {
        return mMockMax;
    }

    @Override
    public TestResult verify(Object info) {
        mResult = TestResult.RESULT_FAIL;
        try {
            mVerifiedInfo = (ThresholdVerifiedInfo) info;
            boolean valid = mVisitor.verify(this, mVerifiedInfo);
            if (valid) {
                mResult = TestResult.RESULT_PASS;
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return mResult;
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException();
    }

}
