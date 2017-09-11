package com.msi.diagnostic.ui;

import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.LcdPagePresenter;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDetectionTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

public class LcdTest extends AndroidTestCase {
    private static final String TAG = "LcdTest";

    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;
    private MockLcdTestCaseView mMockView;
    private LcdPagePresenter mPresenter;

    private final String mTestLevelName = TestLevel.TYPES[TestLevel.TYPE_ASSY];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mTestCaseName = "com.msi.diagnostic.BacklightTestCaseView";
    private final String mTestSingleTouchName = "isSingleTouchPass";
    private final String mTestLcdName = "isLcdPass";
    private final boolean mVerifiedInfoDefine = true;
    @Override
    protected void setUp() throws Exception {
        Log.i(TAG, "setUp...");
        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockLcdTestCaseView(mApp);

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
        TestItem testLcd = new MockDetectionTestItem(
                mApp,
                0,
                mTestLcdName,
                null,
                mTestLevelName,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        TestItem testSingleTouch = new MockDetectionTestItem(
                mApp,
                0,
                mTestSingleTouchName,
                null,
                mTestLevelName,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testLcd);
        mModel.addTestItem(testSingleTouch);
        mPresenter = new LcdPagePresenter(mMockView);
        mPresenter.resume();
    }

    public void test_OnButtonClick_ValidValue_LcdWithResultPass() {
        mPresenter.OnButtonClick(R.id.lcd_pass_button);
        TestItem testLcdPassItem = mModel.loadTestItem(mTestLevelName, mTestLcdName);
        assertEquals(TestResult.PASS, testLcdPassItem.getResult());
    }

    public void test_OnButtonClick_ValidValue_LcdWithResultFail() {
        mPresenter.OnButtonClick(R.id.lcd_fail_button);
        TestItem testLcdFailItem = mModel.loadTestItem(mTestLevelName, mTestLcdName);
        assertEquals(TestResult.FAIL, testLcdFailItem.getResult());
    }

    public void test_OnButtonClick_ValidValue_SingleTouchWithResultPass() {
        mPresenter.OnButtonClick(R.id.click_button);
        mPresenter.OnButtonClick(R.id.lcd_pass_button);
        TestItem testSingleTouchPassItem = mModel.loadTestItem(mTestLevelName,
                mTestSingleTouchName);
        assertEquals(TestResult.PASS, testSingleTouchPassItem.getResult());
    }

    @Override
    protected void tearDown() throws Exception {
        Log.d(TAG, "tearDown...");
    }
}
