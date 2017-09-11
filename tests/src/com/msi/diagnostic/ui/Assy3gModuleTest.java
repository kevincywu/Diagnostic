package com.msi.diagnostic.ui;

import org.apache.http.HttpStatus;

import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.app.Assy3gModulePagePresenter;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDetectionTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;

public class Assy3gModuleTest extends AndroidTestCase {

    private MockAssy3gModulePanelView mMockView;
    private Assy3gModulePagePresenter mPresenter;
    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;
    private WifiManager mWifiManager;
    private TelephonyManager mTelephonyManager;

    private final String mTestLevelName = TestLevel.TYPES[TestLevel.TYPE_ASSY];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mTestCaseName = "com.msi.diagnostic.ui.ThirdGenerationPanelView";
    private final String mTestItemName = "isConnectBaidu";
    private final boolean mVerifiedInfoDefine = true;

    @Override
    protected void setUp() throws Exception {
        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();

        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelName, mTestLevelCaption);
        TestCase testCase = new TestCase(mApp, 0, mTestCaseName,
                mTestLevelName, null, TestResult.create(TestResult.NONE), 0);
        TestItem testItem = new MockDetectionTestItem(mApp, 0, mTestItemName,
                null, mTestLevelName, mTestCaseName, mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItem);

        mMockView = new MockAssy3gModulePanelView(mApp);
        mWifiManager = (WifiManager) this.getContext().getSystemService(
                Context.WIFI_SERVICE);
        mTelephonyManager = (TelephonyManager) this.getContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        mPresenter = new Assy3gModulePagePresenter(mMockView, mWifiManager,
                mTelephonyManager, false, HttpStatus.SC_OK);
        mPresenter.resume();
    }

    @Override
    protected void tearDown() throws Exception {
        mPresenter = null;
        mMockView = null;
    }

    public void test_init3GTest_ValidValue_WithResultPass() {
        mPresenter.onCountDownFinish();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TestItem testItem = mModel.loadTestItem(mTestLevelName, mTestItemName);
        assertEquals(TestResult.PASS, testItem.getResult());
    }
}
