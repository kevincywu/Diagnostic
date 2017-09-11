package com.msi.diagnostic.data;


import android.os.AsyncTask;

import com.msi.diagnostic.app.IDiagnosticApp;

public class ThresholdTestItem extends TestItem {

    protected ThresholdCriteria mCriteria;
    protected ThresholdVerifiedInfo mVerifiedInfo;
    protected IVerifierVisitor mVisitor;

    public ThresholdTestItem(
            IDiagnosticApp app,
            long id,
            String itemName,
            String itemFile,
            String parentLevelName,
            String parentCaseName,
            float max, float min,
            TestResult result) {

        super(
            app,
            id,
            itemName,
            itemFile,
            parentLevelName,
            parentCaseName,
            TestItem.Types.TYPE_THRESHOLD);

        mCriteria = new ThresholdCriteria(max, min);
        mResult = result;
        mVisitor = app.getVerifierVisitor();
    }

    public ThresholdTestItem(
            IDiagnosticApp app,
            String itemName,
            String itemFile,
            String parentLevelName,
            String parentCaseName,
            float max, float min,
            TestResult result) {

        this(
            app,
            NONE_ID,
            itemName,
            itemFile,
            parentLevelName,
            parentCaseName,
            max, min,
            result);
    }

    public float getMin() {
        return mCriteria.getMin();
    }

    public float getMax() {
        return mCriteria.getMax();
    }

    @Override
    public TestResult verify(Object info) {
        mResult = TestResult.RESULT_FAIL;

        try {
            mVerifiedInfo = (ThresholdVerifiedInfo) info;

            boolean valid = mVisitor.verify(this, mVerifiedInfo);
            mResult = TestResult.RESULT_FAIL;
            if (valid) {
                mResult = TestResult.RESULT_PASS;
            }

        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        updateContent();
        return mResult;
    }

    @Override
    public void reload() {

        ReloadTask reloadTask = new ReloadTask();
        reloadTask.execute();

    }

    class ReloadTask extends AsyncTask<Void, Void, TestResult> {

        @Override
        protected TestResult doInBackground(Void... params) {

            IDiagnoseModel model = mApp.getDiagnoseModel();
            TestItem item = model.loadTestItem(mParentLevelName, mName);
            mResult = TestResult.create(item.getResult());
            return mResult;

        }
    }
}
