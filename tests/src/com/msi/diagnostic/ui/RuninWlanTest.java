package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.app.RuninWlanPagePresenter;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDetectionTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.test.AndroidTestCase;

public class RuninWlanTest extends AndroidTestCase {
    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;
    private MockRuninWlanTestCaseView mMockView;
    private RuninWlanPagePresenter mPresenter;

    private final String mTestLevelName = TestLevel.TYPES[TestLevel.TYPE_RUNIN];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mTestCaseName = "com.msi.diagnostic.ui.RuninWlanTestCaseView";
    private final String mTestItemSignalStrength = "isSignalStrengthPass";
    private final String mTestItemCopyFile = "isCopyFilePass";
    private final String mTestItemNumberExisted = "isNumberExisted";
    private final boolean mVerifiedInfoDefine = true;
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockRuninWlanTestCaseView(mApp);

        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelName, mTestLevelCaption);
        TestCase testCase = new TestCase(
                mApp,
                0,
                mTestCaseName,
                mTestLevelName,
                null,
                TestResult.create(TestResult.NONE),
                0);
        TestItem testItemSignal = new MockDetectionTestItem(
                mApp,
                0,
                mTestItemSignalStrength,
                null,
                mTestLevelName,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        TestItem testItemCopyFile = new MockDetectionTestItem(
                mApp,
                0,
                mTestItemCopyFile,
                null,
                mTestLevelName,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        TestItem testItemNumExisted = new MockDetectionTestItem(
                mApp,
                0,
                mTestItemNumberExisted,
                null,
                mTestLevelName,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItemSignal);
        mModel.addTestItem(testItemCopyFile);
        mModel.addTestItem(testItemNumExisted);

        mPresenter = new RuninWlanPagePresenter(mMockView);
        mPresenter.resume();
    }
    public void test_OnButtonClick_ValidSubmit_WithResultFail(){
        mPresenter.OnButtonClick(R.id.button1);
        TestItem testItem = mModel.loadTestItem(mTestLevelName, mTestItemCopyFile);
        assertEquals(TestResult.FAIL, testItem.getResult());
    }
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
