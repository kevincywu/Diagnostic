package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.BluetoothPagePresenter;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDetectionTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.test.AndroidTestCase;
import android.util.Log;

public class BluetoothTest extends AndroidTestCase {
    private static final String TAG = "BluetoothTest";

    private MockBluetoothTestCaseView mMockView;
    private BluetoothPagePresenter mPresenter;
    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;

    private final String mTestLevelPCBA = TestLevel.TYPES[TestLevel.TYPE_PCBA];
    private final String mTestLevelASSY = TestLevel.TYPES[TestLevel.TYPE_ASSY];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mTestCaseName = "com.msi.diagnostic.ui.BluetoothTestCaseView";
    private final String mTestItemName = "isFoundDevice";
    private final boolean mVerifiedInfoDefine = true;

    @Override
    protected void setUp() throws Exception {
        Log.i(TAG, "setUp...");

        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockBluetoothTestCaseView(mApp);
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

        mPresenter = new BluetoothPagePresenter(mMockView);
        mPresenter.resume();
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

        mPresenter = new BluetoothPagePresenter(mMockView);
        mPresenter.resume();
    }

    @Override
    protected void tearDown() throws Exception {
        Log.d(TAG, "tearDown...");
    }
}
