
package com.msi.diagnostic.ui;

import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.app.SpeakerFrontPagePresenter;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDetectionTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

public class SpeakerFrontTest extends AndroidTestCase
{
    private static final String TAG = "SpeakerFrontTest";

    private MockSpeakerFrontTestCaseView mMockView;
    private SpeakerFrontPagePresenter mPresenter;
    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;

    private final String mTestLevel = TestLevel.TYPES[TestLevel.TYPE_ASSY];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mTestCaseName = "com.msi.diagnostic.ui.SpeakerFrontTestCaseView";
    private final String mTestItem = "isValidSpeakerFront";
    private final boolean mVerifiedInfoDefine = true;

    @Override
    protected void setUp() throws Exception
    {
        Log.d(TAG, "setUp...");
        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockSpeakerFrontTestCaseView(mApp);
        TestLevel testLevel = new TestLevel(
                mApp,
                0,
                mTestLevel,
                mTestLevelCaption);
        TestCase testCase = new TestCase(
                mApp,
                0,
                mTestCaseName,
                mTestLevel,
                null,
                TestResult.create(TestResult.NONE),
                0);
        TestItem testItem = new MockDetectionTestItem(
                mApp,
                0,
                mTestItem,
                null,
                mTestLevel,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItem);
        mPresenter = new SpeakerFrontPagePresenter(mMockView);
        mPresenter.resume();
    }

    public void test_speakerFrontTest()
    {
        mPresenter.OnButtonClick(R.id.speaker_front_play);
        mPresenter.OnButtonClick(R.id.speaker_front_stop);
        TestItem testItem1Pass = mModel.loadTestItem(mTestLevel, mTestItem);
        assertEquals(TestResult.PASS, testItem1Pass.getResult());
        mPresenter.OnButtonClick(R.id.speaker_front_play);
        mPresenter.OnButtonClick(R.id.speaker_front_stop);
        mPresenter.OnButtonClick(R.id.speaker_front_fail);
        TestItem testItem1Fail = mModel.loadTestItem(mTestLevel, mTestItem);
        assertEquals(TestResult.FAIL, testItem1Fail.getResult());
    }

    @Override
    protected void tearDown() throws Exception
    {
        Log.d(TAG, "tearDown...");
    }

}
