package com.msi.diagnostic.data;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockDefinitionTestItem extends DefinitionTestItem {

    public MockDefinitionTestItem(
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
                TestObject.NONE_ID,
                itemName,
                itemFile,
                parentLevelName,
                parentCaseName,
                definition,
                result);
    }

    public MockDefinitionTestItem(
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
