package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.app.MotorPagePresenter;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDetectionTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.test.AndroidTestCase;
import android.util.Log;

public class MotorTest extends AndroidTestCase {
    private static final String TAG = "MotorTest";
    private MockMotorPanelView mMockView;
    private MotorPagePresenter mPresenter;
    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;
    private final String mTestLevelPCBA = TestLevel.TYPES[TestLevel.TYPE_PCBA];
    private final String mTestLevelASSY = TestLevel.TYPES[TestLevel.TYPE_ASSY];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mTestCaseName = "com.msi.diagnostic.ui.MotorTestCaseView";
    private final String mTestItemName = "isVibrated";
    private boolean mVerifiedInfoDefine = true;

    @Override
    protected void setUp() throws Exception {
        Log.i(TAG, "setUp...");

        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockMotorPanelView(mApp);
    }

    public void test_handlePCBAItem_ValidValue_ResultPass() {
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelPCBA, mTestLevelCaption);
        TestCase testCase =
                new TestCase(mApp, 0, mTestCaseName, mTestLevelPCBA, null,
                        TestResult.create(TestResult.NONE), 0);
        TestItem testItem =
                new MockDetectionTestItem(mApp, 0, mTestItemName, null, mTestLevelPCBA,
                        mTestCaseName, mVerifiedInfoDefine, TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItem);

        mPresenter = new MotorPagePresenter(mMockView);
        mPresenter.resume();

        mPresenter.OnButtonClick(R.id.toggle);
        mPresenter.OnButtonClick(R.id.pass);
        TestItem testPassItem = mModel.loadTestItem(mTestLevelPCBA, mTestItemName);
        assertEquals(TestResult.PASS, testPassItem.getResult());

        mPresenter.OnButtonClick(R.id.fail);
        TestItem testFailItem = mModel.loadTestItem(mTestLevelPCBA, mTestItemName);
        assertEquals(TestResult.FAIL, testFailItem.getResult());
    }

    public void test_handleASSYItem_ValidValue_ResultPass() {
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelASSY, mTestLevelCaption);
        TestCase testCase =
                new TestCase(mApp, 0, mTestCaseName, mTestLevelASSY, null,
                        TestResult.create(TestResult.NONE), 0);
        TestItem testItem =
                new MockDetectionTestItem(mApp, 0, mTestItemName, null, mTestLevelASSY,
                        mTestCaseName, mVerifiedInfoDefine, TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItem);

        mPresenter = new MotorPagePresenter(mMockView);
        mPresenter.resume();

        mPresenter.OnButtonClick(R.id.toggle);
        mPresenter.OnButtonClick(R.id.pass);
        TestItem testPassItem = mModel.loadTestItem(mTestLevelASSY, mTestItemName);
        assertEquals(TestResult.PASS, testPassItem.getResult());

        mPresenter.OnButtonClick(R.id.fail);
        TestItem testFailItem = mModel.loadTestItem(mTestLevelASSY, mTestItemName);
        assertEquals(TestResult.FAIL, testFailItem.getResult());
    }

    @Override
    protected void tearDown() throws Exception {
        Log.i(TAG, "tearDown...");
    }
}
