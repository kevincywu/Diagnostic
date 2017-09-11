package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.app.RuninEndPagePresenter;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockThresholdTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.test.AndroidTestCase;

public class RuninEndTest extends AndroidTestCase {
    private RuninEndPagePresenter mPresenter;
    private MockRuninEndTestCaseView mMockView;
    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;

    private final String mTestLevelRUNIN = TestLevel.TYPES[TestLevel.TYPE_RUNIN];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mTestCaseName = "com.msi.diagnostic.ui.RuninEndTestCaseView";
    private final String mTestItemName = "isCapacityPass";
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockRuninEndTestCaseView(mApp);

        TestLevel testLevel = new TestLevel(
                mApp,
                0,
                mTestLevelRUNIN,
                mTestLevelCaption);
        TestCase testCase = new TestCase(
                mApp,
                0,
                mTestCaseName,
                mTestLevelRUNIN,
                null,
                TestResult.create(TestResult.NONE),
                0);
        TestItem testItem = new MockThresholdTestItem(mApp, 0,
                mTestItemName,
                null, mTestLevelRUNIN, mTestCaseName,
                (float)96.0,(float)100.0, TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItem);
        mPresenter = new RuninEndPagePresenter(mMockView);
        mPresenter.resume();
    }

    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
    }

}
