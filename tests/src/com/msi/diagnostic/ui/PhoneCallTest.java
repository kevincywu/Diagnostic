package com.msi.diagnostic.ui;

import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.app.PhoneCallPagePresenter;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDetectionTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

public class PhoneCallTest extends AndroidTestCase {
    private static String TAG = "PhoneCallTest";

    private PhoneCallPagePresenter mPresenter;
    private MockPhoneCallTestView mMockView;
    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;

    private final String mTestLevelName = TestLevel.TYPES[TestLevel.TYPE_ASSY];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mTestCaseNamePhoneCall = "com.msi.diagnostic.ui.PhoneCallTestView";

    private static class TestItemId {
        public static final int PHONE_CALL = 0;
    }

    private static final String[] TEST_ITEM_NAME = { "isPhoneCallPass" };

    @Override
    protected void setUp() throws Exception {
        Log.d(TAG, "setUp...");

        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();

        final boolean mVerifiedInfoDefine = true;
        TestLevel testLevel = new TestLevel(
                mApp,
                0,
                mTestLevelName,
                mTestLevelCaption);
        TestCase testCase = new TestCase(
                mApp,
                0,
                mTestCaseNamePhoneCall,
                mTestLevelName,
                null,
                TestResult.create(TestResult.NONE),
                0);
        TestItem testItem = new MockDetectionTestItem(
                mApp,
                0,
                TEST_ITEM_NAME[TestItemId.PHONE_CALL],
                null,
                mTestLevelName,
                mTestCaseNamePhoneCall,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));

        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItem);

        mMockView = new MockPhoneCallTestView(mApp);
        mPresenter = new PhoneCallPagePresenter(mMockView);
        mPresenter.resume();
    }

    public void test_phoneCall() {
        mPresenter.OnButtonClick(R.id.phone_call_call_button);
        mPresenter.OnButtonClick(R.id.phone_call_pass);

        TestItem testItemPass = mModel.loadTestItem(mTestLevelName,
                TEST_ITEM_NAME[TestItemId.PHONE_CALL]);
        assertEquals(TestResult.PASS, testItemPass.getResult());
        mPresenter.OnButtonClick(R.id.phone_call_fail);
        TestItem testItemFail = mModel.loadTestItem(mTestLevelName,
                TEST_ITEM_NAME[TestItemId.PHONE_CALL]);
        assertEquals(TestResult.FAIL, testItemFail.getResult());
    }

    @Override
    protected void tearDown() throws Exception {
        Log.d(TAG, "tearDown...");
    }

}
