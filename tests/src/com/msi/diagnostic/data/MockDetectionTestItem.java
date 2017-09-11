package com.msi.diagnostic.data;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockDetectionTestItem extends DetectionTestItem {

    public MockDetectionTestItem(
            IDiagnosticApp app,
            long id,
            String itemName,
            String itemFile,
            String parentLevelName,
            String parentCaseName,
            boolean mustBeDetected,
            TestResult result) {

        super(
                app,
                TestObject.NONE_ID,
                itemName,
                itemFile,
                parentLevelName,
                parentCaseName,
                mustBeDetected,
                result);
    }

    @Override
    public boolean isMustBeDetected() {
        return mCriteria.getCriteria();
    }

    @Override
    public TestResult verify(Object info) {
        mResult = TestResult.RESULT_FAIL;
        try {
            mVerifiedInfo = (DetectionVerifiedInfo) info;
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
