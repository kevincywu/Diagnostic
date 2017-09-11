package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.AudioBackMICPagePresenter;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDetectionTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.content.Context;
import android.media.AudioManager;
import android.test.AndroidTestCase;

public class AudioBackMicTest extends AndroidTestCase {
    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;
    private AudioBackMICPagePresenter mPresenter;
    private MockAudioBackMicTestCaseView mMockView;
    private AudioManager mAudioManager;

    private final String mTestLevelPCBA = TestLevel.TYPES[TestLevel.TYPE_PCBA];
    private final String mTestLevelASSY = TestLevel.TYPES[TestLevel.TYPE_ASSY];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;

    private final String mTestCaseName = "com.msi.diagnostic.ui.AudioBackMicTestCaseView";
    private final String mTestItemName = "isBackMicRecorded";
    private boolean mVerifiedInfoDefine = true;
    private final String START = "开始录音";
    private final String STOP = "录音停止";
    private final String PLAY = "播放录音";
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();

        mMockView = new MockAudioBackMicTestCaseView(mApp);
        mAudioManager = (AudioManager)mApp.getAppContext().getSystemService(Context.AUDIO_SERVICE);

    }

    public void test_handlePCBAItem_ValidValue_WithResult(){

        TestLevel testLevel = new TestLevel(
                mApp,
                0,
                mTestLevelPCBA,
                mTestLevelCaption);
        TestCase testCase = new TestCase(
                mApp,
                0,
                mTestCaseName,
                mTestLevelPCBA,
                null,
                TestResult.create(TestResult.NONE),
                0);
        TestItem testAudio = new MockDetectionTestItem(
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
        mModel.addTestItem(testAudio);
        mPresenter = new AudioBackMICPagePresenter(mMockView,mAudioManager);
        mPresenter.resume();

        mPresenter.OnButtonClick(R.id.audio_pass);
        TestItem testItemPNONE = mModel.loadTestItem(mTestLevelPCBA, mTestItemName);
        assertEquals(TestResult.NONE, testItemPNONE.getResult());

        mPresenter.OnButtonClick(R.id.audio_fail);
        TestItem testItemFNONE = mModel.loadTestItem(mTestLevelPCBA, mTestItemName);
        assertEquals(TestResult.NONE, testItemFNONE.getResult());

        mPresenter.OnButtonClick(R.id.audio_start);
        mPresenter.OnButtonClick(R.id.audio_stop);
        mPresenter.OnButtonClick(R.id.audio_play);
        mPresenter.OnButtonClick(R.id.audio_pass);
        TestItem testItemPass = mModel.loadTestItem(mTestLevelPCBA, mTestItemName);
        assertEquals(TestResult.PASS, testItemPass.getResult());
        mPresenter.OnButtonClick(R.id.audio_fail);
        TestItem testItemFail = mModel.loadTestItem(mTestLevelPCBA, mTestItemName);
        assertEquals(TestResult.FAIL, testItemFail.getResult());

        mMockView.setmMessage(START);
        mPresenter.OnButtonClick(R.id.audio_start);
        assertEquals(START, mMockView.getmMessage());

        mMockView.setmMessage(STOP);
        mPresenter.OnButtonClick(R.id.audio_start);
        mPresenter.OnButtonClick(R.id.audio_stop);
        assertEquals(STOP, mMockView.getmMessage());

        mMockView.setmMessage(PLAY);
        mPresenter.OnButtonClick(R.id.audio_start);
        mPresenter.OnButtonClick(R.id.audio_stop);
        mPresenter.OnButtonClick(R.id.audio_play);
        assertEquals(PLAY, mMockView.getmMessage());
    }

    public void test_handleASSYItem_ValidValue_WithResult(){
        TestLevel testLevel = new TestLevel(
                mApp,
                0,
                mTestLevelASSY,
                mTestLevelCaption);
        TestCase testCase = new TestCase(
                mApp,
                0,
                mTestCaseName,
                mTestLevelASSY,
                null,
                TestResult.create(TestResult.NONE),
                0);
        TestItem testAudio = new MockDetectionTestItem(
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
        mModel.addTestItem(testAudio);
        mPresenter = new AudioBackMICPagePresenter(mMockView,mAudioManager);
        mPresenter.resume();

        mPresenter.OnButtonClick(R.id.audio_pass);
        TestItem testItemPNONE = mModel.loadTestItem(mTestLevelASSY, mTestItemName);
        assertEquals(TestResult.NONE, testItemPNONE.getResult());

        mPresenter.OnButtonClick(R.id.audio_fail);
        TestItem testItemFNONE = mModel.loadTestItem(mTestLevelASSY, mTestItemName);
        assertEquals(TestResult.NONE, testItemFNONE.getResult());

        mPresenter.OnButtonClick(R.id.audio_start);
        mPresenter.OnButtonClick(R.id.audio_stop);
        mPresenter.OnButtonClick(R.id.audio_play);
        mPresenter.OnButtonClick(R.id.audio_pass);
        TestItem testItemPass = mModel.loadTestItem(mTestLevelASSY, mTestItemName);
        assertEquals(TestResult.PASS, testItemPass.getResult());
        mPresenter.OnButtonClick(R.id.audio_fail);
        TestItem testItemFail = mModel.loadTestItem(mTestLevelASSY, mTestItemName);
        assertEquals(TestResult.FAIL, testItemFail.getResult());

        mMockView.setmMessage(START);
        mPresenter.OnButtonClick(R.id.audio_start);
        assertEquals(START, mMockView.getmMessage());

        mMockView.setmMessage(STOP);
        mPresenter.OnButtonClick(R.id.audio_start);
        mPresenter.OnButtonClick(R.id.audio_stop);
        assertEquals(STOP, mMockView.getmMessage());

        mMockView.setmMessage(PLAY);
        mPresenter.OnButtonClick(R.id.audio_start);
        mPresenter.OnButtonClick(R.id.audio_stop);
        mPresenter.OnButtonClick(R.id.audio_play);
        assertEquals(PLAY, mMockView.getmMessage());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mPresenter = null;
    }
}
