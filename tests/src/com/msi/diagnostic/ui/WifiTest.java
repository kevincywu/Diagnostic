package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.app.MotorPagePresenter;
import com.msi.diagnostic.app.WifiPagePresenter;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDetectionTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.test.AndroidTestCase;

public class WifiTest extends AndroidTestCase {

    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;
    private MockWifiTestCaseView mMockView;
    private WifiPagePresenter mPresenter;

    private final String mTestLevelName = TestLevel.TYPES[TestLevel.TYPE_PCBA];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mTestCaseName = "com.msi.diagnostic.ui.WifiTestCaseView";
    private final String mTestItemName = "isGetMacAddress";
    private final boolean mVerifiedInfoDefine = true;
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockWifiTestCaseView(mApp);

        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelName, mTestLevelCaption);
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

        mPresenter = new WifiPagePresenter(mMockView);
        mPresenter.resume();

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
