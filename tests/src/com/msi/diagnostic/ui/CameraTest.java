package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.CameraPagePresenter;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDetectionTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.test.AndroidTestCase;

public class CameraTest extends AndroidTestCase {

    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;

    private MockCameraTestCaseView mMockView;
    private CameraPagePresenter mPresenter;

    private final String mTestLevelPCBA = TestLevel.TYPES[TestLevel.TYPE_PCBA];
    private final String mTestLevelASSY = TestLevel.TYPES[TestLevel.TYPE_ASSY];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;

    private final String mTestCaseName = "com.msi.diagnostic.ui.CameraTestCaseView";
    private final String mTestItemCamera1 = "isCamera1Captured";
    private final String mTestItemCamera2 = "isCamera2Captured";

    private final boolean mVerifiedInfoDefine = true;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockCameraTestCaseView(mApp);


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
        TestItem testItemCamera1 = new MockDetectionTestItem(
                mApp,
                0,
                mTestItemCamera1,
                null,
                mTestLevelPCBA,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        TestItem testItemCamera2 = new MockDetectionTestItem(
                mApp,
                0,
                mTestItemCamera2,
                null,
                mTestLevelPCBA,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItemCamera1);
        mModel.addTestItem(testItemCamera2);
        mPresenter = new CameraPagePresenter(mMockView);
        mPresenter.resume();

        mPresenter.OnButtonClick(R.id.cameraFail);
        TestItem testItemFailNONE = mModel.loadTestItem(mTestLevelPCBA, mTestItemCamera1);
        assertEquals(TestResult.NONE, testItemFailNONE.getResult());

        mPresenter.OnButtonClick(R.id.cameraPass);
        TestItem testItemPassNONE = mModel.loadTestItem(mTestLevelPCBA, mTestItemCamera1);
        assertEquals(TestResult.NONE, testItemPassNONE.getResult());

        mPresenter.OnButtonClick(R.id.takePic);
        mPresenter.OnButtonClick(R.id.cameraPass);
        TestItem testItem1Pass = mModel.loadTestItem(mTestLevelPCBA, mTestItemCamera1);
        assertEquals(TestResult.PASS, testItem1Pass.getResult());

        mPresenter.OnButtonClick(R.id.cameraSwitch);
        mPresenter.OnButtonClick(R.id.takePic);
        mPresenter.OnButtonClick(R.id.cameraFail);
        TestItem testItem2Fail = mModel.loadTestItem(mTestLevelPCBA, mTestItemCamera2);
        assertEquals(TestResult.FAIL, testItem2Fail.getResult());
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
        TestItem testItemCamera1 = new MockDetectionTestItem(
                mApp,
                0,
                mTestItemCamera1,
                null,
                mTestLevelASSY,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        TestItem testItemCamera2 = new MockDetectionTestItem(
                mApp,
                0,
                mTestItemCamera2,
                null,
                mTestLevelASSY,
                mTestCaseName,
                mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItemCamera1);
        mModel.addTestItem(testItemCamera2);
        mPresenter = new CameraPagePresenter(mMockView);
        mPresenter.resume();

        mPresenter.OnButtonClick(R.id.cameraFail);
        TestItem testItemFailNONE = mModel.loadTestItem(mTestLevelASSY, mTestItemCamera1);
        assertEquals(TestResult.NONE, testItemFailNONE.getResult());

        mPresenter.OnButtonClick(R.id.cameraPass);
        TestItem testItemPassNONE = mModel.loadTestItem(mTestLevelASSY, mTestItemCamera1);
        assertEquals(TestResult.NONE, testItemPassNONE.getResult());

        mPresenter.OnButtonClick(R.id.takePic);
        mPresenter.OnButtonClick(R.id.cameraPass);
        TestItem testItem1Pass = mModel.loadTestItem(mTestLevelASSY, mTestItemCamera1);
        assertEquals(TestResult.PASS, testItem1Pass.getResult());

        mPresenter.OnButtonClick(R.id.cameraSwitch);
        mPresenter.OnButtonClick(R.id.takePic);
        mPresenter.OnButtonClick(R.id.cameraFail);
        TestItem testItem2Fail = mModel.loadTestItem(mTestLevelASSY, mTestItemCamera2);
        assertEquals(TestResult.FAIL, testItem2Fail.getResult());
    }
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
