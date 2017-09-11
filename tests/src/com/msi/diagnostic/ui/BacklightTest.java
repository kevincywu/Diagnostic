package com.msi.diagnostic.ui;

import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.BacklightPagePresenter;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDetectionTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

public class BacklightTest extends AndroidTestCase {
    private static final String TAG = "BacklightTest";

    private MockBacklightTestCaseView mMockView;
    private BacklightPagePresenter mPresenter;

    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;

    private final String mTestLevelName = TestLevel.TYPES[TestLevel.TYPE_ASSY];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mTestCaseName = "com.msi.diagnostic.BacklightTestCaseView";
    private final String mTestItemName = "isBacklightChanged";
    private final boolean mVerifiedInfoDefine = true;

    @Override
    protected void setUp() throws Exception {
        Log.i(TAG, "setUp...");

        super.setUp();
        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockBacklightTestCaseView(mApp);

        TestLevel testLevel = new TestLevel(
                mApp,
                0,
                mTestLevelName,
                mTestLevelCaption);
        TestCase testCase = new TestCase(
                mApp,
                0,
                mTestCaseName,
                mTestLevelName,
                null,
                TestResult.create(TestResult.NONE),
                0);
        TestItem testItem = new MockDetectionTestItem(
                mApp,
                0,
                mTestItemName,
                null,
                mTestLevelName,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItem);
        mPresenter = new BacklightPagePresenter(mMockView);
        mPresenter.resume();
    }

    public void test_OnButtonClick_ValidValue_WithResultPass(){
        int value = 5;
        mPresenter.setBrightness(value);
        mPresenter.OnButtonClick(R.id.backlight_pass);
        TestItem testItem = mModel.loadTestItem(mTestLevelName, mTestItemName);
        assertEquals(TestResult.PASS, testItem.getResult());
    }

    public void test_OnButtonClick_ValidValue_WithResultFail(){
        int value = 5;
        mPresenter.setBrightness(value);
        mPresenter.OnButtonClick(R.id.backlight_fail);
        TestItem testItem = mModel.loadTestItem(mTestLevelName, mTestItemName);
        assertEquals(TestResult.FAIL, testItem.getResult());
    }

    @Override
    protected void tearDown() throws Exception {
        Log.d(TAG, "tearDown...");
    }
}
