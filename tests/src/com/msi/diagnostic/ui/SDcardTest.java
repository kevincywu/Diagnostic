package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.app.SDcardPagePresenter;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDetectionTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.test.AndroidTestCase;
import android.util.Log;

public class SDcardTest extends AndroidTestCase
{
    private static final String TAG = "SDcardTest";

    private MockSDcardPanelView mMockView;
    private SDcardPagePresenter mPresenter;
    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;

    private final String mTestLevelPCBA = TestLevel.TYPES[TestLevel.TYPE_PCBA];
    private final String mTestLevelASSY = TestLevel.TYPES[TestLevel.TYPE_ASSY];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mTestCaseName = "com.msi.diagnostic.ui.SDcardPanelView";
    private final String mTestItemName = "isSdFunctions";
    private final String mTestItemfile = "/mnt/sdcard";
    private final boolean mVerifiedInfoDefine = true;

    @Override
    protected void setUp() throws Exception {
        Log.i(TAG, "setUp...");

        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockSDcardPanelView(mApp);


    }

    public void test_handlePCBAItem_ValidValue_WithResult() {
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelPCBA, mTestLevelCaption);
        TestCase testCase = new TestCase(
                mApp,
                0,
                mTestCaseName,
                mTestLevelPCBA,
                null,
                TestResult.create(TestResult.NONE),
                0);
        TestItem testItemSd = new MockDetectionTestItem(
                mApp,
                0,
                mTestItemName,
                mTestItemfile,
                mTestLevelPCBA,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItemSd);

        mPresenter = new SDcardPagePresenter(mMockView);
        mPresenter.resume();

        mPresenter.onCountDownFinish();

        TestItem testItemTb = mModel.loadTestItem(mTestLevelPCBA, mTestItemName);
        assertEquals(TestResult.PASS, testItemTb.getResult());

        mPresenter.OnButtonClick(R.id.skip_button);
        TestItem testItemSb = mModel.loadTestItem(mTestLevelPCBA, mTestItemName);
        assertEquals(TestResult.PASS, testItemSb.getResult());
    }

    public void test_handleASSYItem_ValidValue_WithResult() {

        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelASSY, mTestLevelCaption);
        TestCase testCase = new TestCase(
                mApp,
                0,
                mTestCaseName,
                mTestLevelASSY,
                null,
                TestResult.create(TestResult.NONE),
                0);
        TestItem testItemSd = new MockDetectionTestItem(
                mApp,
                0,
                mTestItemName,
                mTestItemfile,
                mTestLevelASSY,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItemSd);

        mPresenter = new SDcardPagePresenter(mMockView);
        mPresenter.resume();

        mPresenter.onCountDownFinish();
        TestItem testItemTb = mModel.loadTestItem(mTestLevelASSY, mTestItemName);
        assertEquals(TestResult.PASS, testItemTb.getResult());

        mPresenter.OnButtonClick(R.id.skip_button);
        TestItem testItemSb = mModel.loadTestItem(mTestLevelASSY, mTestItemName);
        assertEquals(TestResult.PASS, testItemSb.getResult());
    }

    @Override
    protected void tearDown() throws Exception {
        Log.i(TAG, "tearDown...");

        mPresenter = null;
        mMockView = null;
    }
}
