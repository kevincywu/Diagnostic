package com.msi.diagnostic.data;

import com.msi.diagnostic.app.IDiagnosticApp;

public class DetectionTestItem extends TestItem {

    protected DetectionCriteria mCriteria;
    protected DetectionVerifiedInfo mVerifiedInfo;
    protected IVerifierVisitor mVisitor;

    public DetectionTestItem(
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
            id,
            itemName,
            itemFile,
            parentLevelName,
            parentCaseName,
            TestItem.Types.TYPE_DETECTION);

        mCriteria = new DetectionCriteria(mustBeDetected);
        mResult = result;
        mVisitor = app.getVerifierVisitor();
    }

    public DetectionTestItem(
            IDiagnosticApp app,
            String itemName,
            String itemFile,
            String parentLevelName,
            String parentCaseName,
            boolean mustBeDetected,
            TestResult result) {

        this(
            app,
            NONE_ID,
            itemName,
            itemFile,
            parentLevelName,
            parentCaseName,
            mustBeDetected,
            result);
    }

    public boolean isMustBeDetected() {
        return mCriteria.getCriteria();
    }

    /**
     * Verify the Test-Item to check whether it is pass or not.
     *
     * @param The verified information (value) from the Panel.
     * @return The result of verification.
     */
    @Override
    public TestResult verify(Object info) {
        mResult = TestResult.RESULT_FAIL;
        try {
            mVerifiedInfo = (DetectionVerifiedInfo) info;

            boolean valid = mVisitor.verify(this, mVerifiedInfo);
            mResult = TestResult.RESULT_FAIL;
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
        /**
         * TODO: FutureTask for this operation.
         */
        IDiagnoseModel model = mApp.getDiagnoseModel();
        TestItem item = model.loadTestItem(mParentLevelName, mName);
        mResult = TestResult.create(item.getResult());
    }
}
