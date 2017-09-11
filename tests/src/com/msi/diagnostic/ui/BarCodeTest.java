package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.BarCodePagePresenter;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.data.DeviceConfig;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDefinitionTestItem;
import com.msi.diagnostic.data.MockDetectionTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.test.AndroidTestCase;
import android.util.Log;
import android.view.KeyEvent;

public class BarCodeTest extends AndroidTestCase {
    private static final String TAG = "BarCodeTest";

    private MockBarCodePanelView mMockView;
    private BarCodePagePresenter mPresenter;
    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;

    private final String mTestLevelPCBA = TestLevel.TYPES[TestLevel.TYPE_PCBA];
    private final String mTestLevelASSY = TestLevel.TYPES[TestLevel.TYPE_ASSY];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mTestCaseName = "com.msi.diagnostic.ui.BarCodeTestCaseView";
    private final String mTestItemName = "isNumberExisted";
    private final boolean mVerifiedInfoDefine = true;

    @Override
    protected void setUp() throws Exception {
        Log.i(TAG, "setUp...");

        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockBarCodePanelView(mApp);
    }

    public void test_handlePCBAItem_ValidValue_ResultPass() {
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelPCBA, mTestLevelCaption);
        TestCase testCase = new TestCase(
                mApp,
                0,
                mTestCaseName,
                mTestLevelPCBA,
                null,
                TestResult.create(TestResult.NONE),
                0);
        TestItem testItem = new MockDetectionTestItem(
                mApp,
                0,
                mTestItemName,
                null,
                mTestLevelPCBA,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItem);

        mPresenter = new BarCodePagePresenter(mMockView);
        mPresenter.resume();

        DeviceConfig deviceConfigEnter = mApp.getDeviceConfig();
        String checkNameEnter = deviceConfigEnter.getDeviceName();
        mMockView.setBarCodeNumberText(checkNameEnter);
        mPresenter.OnButtonClick(KeyEvent.KEYCODE_ENTER);
        TestItem testBarCodeItemPass = mModel.loadTestItem(mTestLevelPCBA, mTestItemName);
        assertEquals(TestResult.PASS, testBarCodeItemPass.getResult());

        mMockView.setBarCodeNumberText("");
        mPresenter.OnButtonClick(KeyEvent.KEYCODE_ENTER);
        TestItem testBarCodeItemFail = mModel.loadTestItem(mTestLevelPCBA, mTestItemName);
        assertEquals(TestResult.FAIL, testBarCodeItemFail.getResult());
    }

    public void test_handleASSYItem_ValidValue_ResultPASS() {
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelASSY, mTestLevelCaption);
        TestCase testCase = new TestCase(
                mApp,
                0,
                mTestCaseName,
                mTestLevelASSY,
                null,
                TestResult.create(TestResult.NONE),
                0);
        TestItem testItem = new MockDetectionTestItem(
                mApp,
                0,
                mTestItemName,
                null,
                mTestLevelASSY,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItem);

        mPresenter = new BarCodePagePresenter(mMockView);
        mPresenter.resume();

        DeviceConfig deviceConfigEnter = mApp.getDeviceConfig();
        String checkNameEnter = deviceConfigEnter.getDeviceName();
        mMockView.setBarCodeNumberText(checkNameEnter);
        mPresenter.OnButtonClick(KeyEvent.KEYCODE_ENTER);
        TestItem testBarCodeItemPass = mModel.loadTestItem(mTestLevelASSY, mTestItemName);
        assertEquals(TestResult.PASS, testBarCodeItemPass.getResult());

        mMockView.setBarCodeNumberText("");
        mPresenter.OnButtonClick(KeyEvent.KEYCODE_ENTER);
        TestItem testBarCodeItemFail = mModel.loadTestItem(mTestLevelASSY, mTestItemName);
        assertEquals(TestResult.FAIL, testBarCodeItemFail.getResult());
    }

    @Override
    protected void tearDown() throws Exception {
        Log.i(TAG, "tearDown...");
    }
}
