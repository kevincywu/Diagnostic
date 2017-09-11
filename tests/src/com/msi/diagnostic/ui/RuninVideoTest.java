package com.msi.diagnostic.ui;

import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.app.RuninVideoPagePresenter;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDetectionTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

public class RuninVideoTest extends AndroidTestCase {
    private static final String TAG = "RuninVideoTest";

    private RuninVideoPagePresenter mPresenter;
    private MockRuninVideoTestCaseView mMockView;
    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;

    private final String mTestLevelRUNIN = TestLevel.TYPES[TestLevel.TYPE_RUNIN];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mTestCaseName = "com.msi.diagnostic.ui.RuninVideoTestCaseView";
    private final String mTestItemName = "isVideoFinish";
    private boolean mVerifiedInfoDefine = true;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Log.d(TAG, "setUp...");

        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockRuninVideoTestCaseView(mApp);

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
        TestItem testItem = new MockDetectionTestItem(
                mApp,
                0,
                mTestItemName,
                null,
                mTestLevelRUNIN,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));

        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItem);
        mPresenter = new RuninVideoPagePresenter(mMockView);
        mPresenter.resume();
    }

    public void test_RuninVideo_Pass() {
        TestItem testItemNone = mModel.loadTestItem(mTestLevelRUNIN, mTestItemName);
        assertEquals(TestResult.NONE, testItemNone.getResult());
        mPresenter.onCountDownFinish();
        TestItem testItemPass = mModel.loadTestItem(mTestLevelRUNIN, mTestItemName);
        assertEquals(TestResult.PASS, testItemPass.getResult());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Log.d(TAG, "tearDown...");
    }
}
