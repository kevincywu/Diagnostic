
package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.app.Pcba3gModulePagePresenter;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDetectionTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.test.AndroidTestCase;
import android.util.Log;

public class Pcba3gModuleTest extends AndroidTestCase
{
    private static final String TAG = "TGModuleTest";

    private MockPcba3gModulePanelView mMockView;

    private Pcba3gModulePagePresenter mPresenter;

    private MockDiagnosticApp mApp;

    private IDiagnoseModel mModel;

    private final String mTestLevelName = TestLevel.TYPES[TestLevel.TYPE_PCBA];

    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;

    private final String mTestCaseName = "com.msi.diagnostic.ui.TGModulePanelView";

    private final String mTestItemName = "is3gValid";

    private final String mTestItemfile = "/ueventd.rc";

    private final boolean mVerifiedInfoDefine = true;

    @Override
    protected void setUp() throws Exception
    {
        Log.i(TAG, "setUp...");

        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockPcba3gModulePanelView(mApp);

        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelName, mTestLevelCaption);
        TestCase testCase = new TestCase(mApp, 0, mTestCaseName, mTestLevelName, null,
                TestResult.create(TestResult.NONE), 0);
        TestItem testItem = new MockDetectionTestItem(mApp, 0, mTestItemName, mTestItemfile,
                mTestLevelName, mTestCaseName, mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItem);

        mPresenter = new Pcba3gModulePagePresenter(mMockView);
        mPresenter.resume();
    }

    public void test_OnButtonClick_ValidValue_WithResultPass()
    {

        mPresenter.OnButtonClick(R.id.startTest);
        TestItem testItem = mModel.loadTestItem(mTestLevelName, mTestItemName);
        assertEquals(TestResult.PASS, testItem.getResult());
    }

    @Override
    protected void tearDown() throws Exception
    {
        Log.i(TAG, "tearDown...");

        mPresenter = null;
        mMockView = null;
    }
}
