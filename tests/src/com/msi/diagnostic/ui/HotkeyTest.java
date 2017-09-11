package com.msi.diagnostic.ui;

import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.app.HotKeyPagePresenter;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.ui.MockHotkeyPanel;
import com.msi.diagnostic.data.MockDetectionTestItem;

public class HotkeyTest extends AndroidTestCase {
    private static final String TAG = "HotkeyTest";

    private HotKeyPagePresenter mPresenter;
    private MockHotkeyPanel mMockPanel;
    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;

    private final String mTestLevelPCBA = TestLevel.TYPES[TestLevel.TYPE_PCBA];
    private final String mTestLevelASSY = TestLevel.TYPES[TestLevel.TYPE_ASSY];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mTestCaseName = "com.msi.diagnostic.ui.HotKeyPanelView";
    private final String mTestItemNameDown = "isVolumeDownCatched";
    private final String mTestItemNameUp = "isVolumeUpCatched";
    private final boolean mVerifiedInfoDefine = true;

    @Override
    protected void setUp() throws Exception {
        Log.i(TAG, "setUp...");

        // Ready to initialize the PanelView and PagePresenter
        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockPanel = new MockHotkeyPanel(mApp);
    }

    public void test_handlePCBAItem_ValidValue_ResultPass() {
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelPCBA, mTestLevelCaption);
        TestCase testCase =
                new TestCase(mApp, 0, mTestCaseName, mTestLevelPCBA, null,
                        TestResult.create(TestResult.NONE), 0);
        TestItem testItemDown =
                new MockDetectionTestItem(mApp, 0, mTestItemNameDown, null, mTestLevelPCBA,
                        mTestCaseName, mVerifiedInfoDefine, TestResult.create(TestResult.NONE));
        TestItem testItemUp =
                new MockDetectionTestItem(mApp, 0, mTestItemNameUp, null, mTestLevelPCBA,
                        mTestCaseName, mVerifiedInfoDefine, TestResult.create(TestResult.NONE));

        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItemDown);
        mModel.addTestItem(testItemUp);

        mPresenter = new HotKeyPagePresenter(mMockPanel);
        mPresenter.resume(); // To initialize the data

        assertEquals("NONE", mMockPanel.getVolumeDownResult());

        mPresenter.onVolumnDownKeyUp();
        assertEquals("PASS", mMockPanel.getVolumeDownResult());

        assertEquals("NONE", mMockPanel.getVolumeUpResult());

        mPresenter.onVolumnUpKeyUp();
        assertEquals("PASS", mMockPanel.getVolumeUpResult());
    }

    public void test_handleASSYItem_ValidValue_ResultPass() {
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelASSY, mTestLevelCaption);
        TestCase testCase =
                new TestCase(mApp, 0, mTestCaseName, mTestLevelASSY, null,
                        TestResult.create(TestResult.NONE), 0);
        TestItem testItemDown =
                new MockDetectionTestItem(mApp, 0, mTestItemNameDown, null, mTestLevelASSY,
                        mTestCaseName, mVerifiedInfoDefine, TestResult.create(TestResult.NONE));
        TestItem testItemUp =
                new MockDetectionTestItem(mApp, 0, mTestItemNameUp, null, mTestLevelASSY,
                        mTestCaseName, mVerifiedInfoDefine, TestResult.create(TestResult.NONE));

        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItemDown);
        mModel.addTestItem(testItemUp);

        mPresenter = new HotKeyPagePresenter(mMockPanel);
        mPresenter.resume(); // To initialize the data

        assertEquals("NONE", mMockPanel.getVolumeDownResult());

        mPresenter.onVolumnDownKeyUp();
        assertEquals("PASS", mMockPanel.getVolumeDownResult());

        assertEquals("NONE", mMockPanel.getVolumeUpResult());

        mPresenter.onVolumnUpKeyUp();
        assertEquals("PASS", mMockPanel.getVolumeUpResult());
    }

    @Override
    protected void tearDown() throws Exception {
        Log.i(TAG, "tearDown...");
    }
}
