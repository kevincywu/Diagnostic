package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.HDMIPagePresenter;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDetectionTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.test.AndroidTestCase;
import android.util.Log;

public class HDMITest extends AndroidTestCase{

    private static final String TAG = "HDMITest";

    private MockHDMIPanelView mMockView;
    private HDMIPagePresenter mPresenter;
    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;

    private final String mTestLevelPCBA = TestLevel.TYPES[TestLevel.TYPE_PCBA];
    private final String mTestLevelASSY = TestLevel.TYPES[TestLevel.TYPE_ASSY];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mTestCaseNameHdmi = "com.msi.diagnostic.ui.HDMIPanelView";
    private final String mTestVideoName = "isVideoPlayed";
    private final String mTestVoiceName = "isVoicePlayed";
    private final String mPlayInfo = "正在播放音乐......";
    private final String mStopInfo = "暂停播放.......";
    private final boolean mVerifiedInfoDefine = true;

    @Override
    protected void setUp() throws Exception {
        Log.i(TAG, "setUp...");

        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockHDMIPanelView(mApp);
    }

    public void test_handlePCBAItem_ValidValue_ResultPass() {
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelPCBA, mTestLevelCaption);
        TestCase testCase = new TestCase(
                mApp,
                0,
                mTestCaseNameHdmi,
                mTestLevelPCBA,
                null,
                TestResult.create(TestResult.NONE),
                0);
        TestItem testVideo = new MockDetectionTestItem(
                mApp,
                0,
                mTestVideoName,
                null,
                mTestLevelPCBA,
                mTestCaseNameHdmi,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        TestItem testVoice = new MockDetectionTestItem(
                mApp,
                0,
                mTestVoiceName,
                null,
                mTestLevelPCBA,
                mTestCaseNameHdmi,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testVideo);
        mModel.addTestItem(testVoice);

        mPresenter = new HDMIPagePresenter(mMockView);
        mPresenter.resume();

        mPresenter.OnButtonClick(R.id.pass);
        TestItem testVideoPassNone = mModel.loadTestItem(mTestLevelPCBA, mTestVideoName);
        TestItem testVoicePassNone = mModel.loadTestItem(mTestLevelPCBA, mTestVoiceName);
        assertEquals(TestResult.NONE, testVideoPassNone.getResult());
        assertEquals(TestResult.NONE, testVoicePassNone.getResult());

        mPresenter.OnButtonClick(R.id.vfail);
        TestItem testVideoFailNone = mModel.loadTestItem(mTestLevelPCBA, mTestVideoName);
        assertEquals(TestResult.NONE, testVideoFailNone.getResult());

        mPresenter.OnButtonClick(R.id.pfail);
        TestItem testVoiceFailNone = mModel.loadTestItem(mTestLevelPCBA, mTestVoiceName);
        assertEquals(TestResult.NONE, testVoiceFailNone.getResult());

        mPresenter.OnButtonClick(R.id.play);
        mPresenter.OnButtonClick(R.id.stop);
        mPresenter.OnButtonClick(R.id.pass);
        TestItem testVideoPass = mModel.loadTestItem(mTestLevelPCBA, mTestVideoName);
        TestItem testVoicePass = mModel.loadTestItem(mTestLevelPCBA, mTestVoiceName);
        assertEquals(TestResult.PASS, testVideoPass.getResult());
        assertEquals(TestResult.PASS, testVoicePass.getResult());

        mPresenter.OnButtonClick(R.id.vfail);
        TestItem testVideoFail = mModel.loadTestItem(mTestLevelPCBA, mTestVideoName);
        assertEquals(TestResult.FAIL, testVideoFail.getResult());

        mPresenter.OnButtonClick(R.id.pfail);
        TestItem testVoiceFail = mModel.loadTestItem(mTestLevelPCBA, mTestVoiceName);
        assertEquals(TestResult.FAIL, testVoiceFail.getResult());

        mPresenter.OnButtonClick(R.id.play);
        assertEquals(mPlayInfo, mMockView.getTextView());

        mPresenter.OnButtonClick(R.id.play);
        mPresenter.OnButtonClick(R.id.stop);
        assertEquals(mStopInfo, mMockView.getTextView());
    }

    public void test_handleASSYItem_ValidValue_ResultPass() {
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelASSY, mTestLevelCaption);
        TestCase testCase = new TestCase(
                mApp,
                0,
                mTestCaseNameHdmi,
                mTestLevelASSY,
                null,
                TestResult.create(TestResult.NONE),
                0);
        TestItem testVideo = new MockDetectionTestItem(
                mApp,
                0,
                mTestVideoName,
                null,
                mTestLevelASSY,
                mTestCaseNameHdmi,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        TestItem testVoice = new MockDetectionTestItem(
                mApp,
                0,
                mTestVoiceName,
                null,
                mTestLevelASSY,
                mTestCaseNameHdmi,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testVideo);
        mModel.addTestItem(testVoice);

        mPresenter = new HDMIPagePresenter(mMockView);
        mPresenter.resume();

        mPresenter.OnButtonClick(R.id.pass);
        TestItem testVideoPassNone = mModel.loadTestItem(mTestLevelASSY, mTestVideoName);
        TestItem testVoicePassNone = mModel.loadTestItem(mTestLevelASSY, mTestVoiceName);
        assertEquals(TestResult.NONE, testVideoPassNone.getResult());
        assertEquals(TestResult.NONE, testVoicePassNone.getResult());

        mPresenter.OnButtonClick(R.id.vfail);
        TestItem testVideoFailNone = mModel.loadTestItem(mTestLevelASSY, mTestVideoName);
        assertEquals(TestResult.NONE, testVideoFailNone.getResult());

        mPresenter.OnButtonClick(R.id.pfail);
        TestItem testVoiceFailNone = mModel.loadTestItem(mTestLevelASSY, mTestVoiceName);
        assertEquals(TestResult.NONE, testVoiceFailNone.getResult());

        mPresenter.OnButtonClick(R.id.play);
        mPresenter.OnButtonClick(R.id.stop);
        mPresenter.OnButtonClick(R.id.pass);
        TestItem testVideoPass = mModel.loadTestItem(mTestLevelASSY, mTestVideoName);
        TestItem testVoicePass = mModel.loadTestItem(mTestLevelASSY, mTestVoiceName);
        assertEquals(TestResult.PASS, testVideoPass.getResult());
        assertEquals(TestResult.PASS, testVoicePass.getResult());

        mPresenter.OnButtonClick(R.id.vfail);
        TestItem testVideoFail = mModel.loadTestItem(mTestLevelASSY, mTestVideoName);
        assertEquals(TestResult.FAIL, testVideoFail.getResult());

        mPresenter.OnButtonClick(R.id.pfail);
        TestItem testVoiceFail = mModel.loadTestItem(mTestLevelASSY, mTestVoiceName);
        assertEquals(TestResult.FAIL, testVoiceFail.getResult());

        mPresenter.OnButtonClick(R.id.play);
        assertEquals(mPlayInfo, mMockView.getTextView());

        mPresenter.OnButtonClick(R.id.play);
        mPresenter.OnButtonClick(R.id.stop);
        assertEquals(mStopInfo, mMockView.getTextView());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Log.d(TAG, "tearDown...");
    }
}
