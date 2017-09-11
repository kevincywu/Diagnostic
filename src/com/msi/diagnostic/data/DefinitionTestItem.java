package com.msi.diagnostic.data;

import com.msi.diagnostic.app.IDiagnosticApp;

public class DefinitionTestItem extends TestItem {

    protected final DefinitionCriteria mCriteria;
    protected DefinitionVerifiedInfo mVerifiedInfo;
    protected IVerifierVisitor mVisitor;

    public DefinitionTestItem(
            IDiagnosticApp app,
            long id,
            String itemName,
            String itemFile,
            String parentLevelName,
            String parentCaseName,
            String definition,
            TestResult result) {

        super(
            app,
            id,
            itemName,
            itemFile,
            parentLevelName,
            parentCaseName,
            TestItem.Types.TYPE_DEFINITION);

        mCriteria = new DefinitionCriteria(definition);
        mResult = result;
        mVisitor = app.getVerifierVisitor();
    }

    public DefinitionTestItem(
            IDiagnosticApp app,
            String itemName,
            String itemFile,
            String parentLevelName,
            String parentCaseName,
            String definition,
            TestResult result) {

        this(
            app,
            TestObject.NONE_ID,
            itemName,
            itemFile,
            parentLevelName,
            parentCaseName,
            definition,
            result);
    }

    public String getDefinition() {
        return mCriteria.getDefinition();
    }

    @Override
    public TestResult verify(Object info) {
        mResult = TestResult.RESULT_FAIL;

        try {
            mVerifiedInfo = (DefinitionVerifiedInfo) info;

            boolean valid = mVisitor.verify(this, mVerifiedInfo);
            mResult = TestResult.RESULT_FAIL;
            if (valid) {
                mResult = TestResult.RESULT_PASS;
            }
            IDiagnoseModel model = mApp.getDiagnoseModel();
            model.updateTestItem(this);

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
