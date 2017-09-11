package com.msi.diagnostic.ui;

import com.msi.diagnostic.ui.MockRTCPanelView;
import com.msi.diagnostic.data.MockDefinitionTestItem;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.app.RTCPagePresenter;

import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.test.AndroidTestCase;
import android.util.Log;



public class RTCTest extends AndroidTestCase{

    private MockRTCPanelView mMockView;
    private RTCPagePresenter mPresenter;
    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;

    private final String TAG = "RTC_TEST";

    private final String mTestLevelPCBA = TestLevel.TYPES[TestLevel.TYPE_PCBA];
    private final String mTestLevelASSY = TestLevel.TYPES[TestLevel.TYPE_ASSY];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mTestCaseName = "com.msi.diagnostic.ui.RTCPanelView";

    private final String mTestItemName = "isNameExisted";
    private final String mTestItemDate = "isDateExisted";
    private final String mTestItemTime = "isTimeExisted";

    private final String mTestItemNameFile = "/sys/class/rtc/rtc0/name";
    private final String mTestItemDateFile = "/sys/class/rtc/rtc0/date";
    private final String mTestItemTimeFile = "/sys/class/rtc/rtc0/time";

    private final String mVerifiedInfoDefine = "@null";

    @Override
    protected void setUp() throws Exception {
        Log.d(TAG, "setUp...");
        mApp = new MockDiagnosticApp(this.getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockRTCPanelView(mApp);


    }

    public void test_isPCBAItemExisted_ValidValue_WithResultPass() {

        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelPCBA, mTestLevelCaption);
        TestCase testCase = new TestCase(
                mApp,
                0,
                mTestCaseName,
                mTestLevelPCBA,
                null,
                TestResult.create(TestResult.NONE),
                0);
        TestItem testItemName = new MockDefinitionTestItem(
                mApp,
                0,
                mTestItemName,
                mTestItemNameFile,
                mTestLevelPCBA,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        TestItem testItemDate = new MockDefinitionTestItem(
                mApp,
                0,
                mTestItemDate,
                mTestItemDateFile,
                mTestLevelPCBA,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        TestItem testItemTime = new MockDefinitionTestItem(
                mApp,
                0,
                mTestItemTime,
                mTestItemTimeFile,
                mTestLevelPCBA,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItemName);
        mModel.addTestItem(testItemDate);
        mModel.addTestItem(testItemTime);

        mPresenter = new RTCPagePresenter(mMockView);
        mPresenter.resume();

        TestItem testItemN = mModel.loadTestItem(mTestLevelPCBA, mTestItemName);
        assertEquals(TestResult.PASS, testItemN.getResult());

        TestItem testItemD = mModel.loadTestItem(mTestLevelPCBA, mTestItemDate);
        assertEquals(TestResult.PASS, testItemD.getResult());

        TestItem testItemT = mModel.loadTestItem(mTestLevelPCBA, mTestItemTime);
        assertEquals(TestResult.PASS, testItemT.getResult());
    }

    public void test_isASSYItemExisted_ValidValue_WithResultPass() {

        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelASSY, mTestLevelCaption);
        TestCase testCase = new TestCase(
                mApp,
                0,
                mTestCaseName,
                mTestLevelASSY,
                null,
                TestResult.create(TestResult.NONE),
                0);
        TestItem testItemName = new MockDefinitionTestItem(
                mApp,
                0,
                mTestItemName,
                mTestItemNameFile,
                mTestLevelASSY,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        TestItem testItemDate = new MockDefinitionTestItem(
                mApp,
                0,
                mTestItemDate,
                mTestItemDateFile,
                mTestLevelASSY,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        TestItem testItemTime = new MockDefinitionTestItem(
                mApp,
                0,
                mTestItemTime,
                mTestItemTimeFile,
                mTestLevelASSY,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItemName);
        mModel.addTestItem(testItemDate);
        mModel.addTestItem(testItemTime);

        mPresenter = new RTCPagePresenter(mMockView);
        mPresenter.resume();

        TestItem testItemN = mModel.loadTestItem(mTestLevelASSY, mTestItemName);
        assertEquals(TestResult.PASS, testItemN.getResult());

        TestItem testItemD = mModel.loadTestItem(mTestLevelASSY, mTestItemDate);
        assertEquals(TestResult.PASS, testItemD.getResult());

        TestItem testItemT = mModel.loadTestItem(mTestLevelASSY, mTestItemTime);
        assertEquals(TestResult.PASS, testItemT.getResult());

    }


    @Override
    protected void tearDown() throws Exception {
        Log.d(TAG, "tearDown...");

    }
}
