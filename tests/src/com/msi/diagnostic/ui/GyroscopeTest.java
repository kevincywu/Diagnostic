package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.GyroscopePagePresenter;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.app.SensorEventInfo;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDetectionTestItem;
import com.msi.diagnostic.data.MockThresholdTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.hardware.Sensor;
import android.test.AndroidTestCase;
import android.util.Log;

public class GyroscopeTest extends AndroidTestCase {
    private static final String TAG = "GyroscopeTest";

    private GyroscopePagePresenter mPresenter;

    private MockGyroscopeTestCaseView mMockView;

    private MockDiagnosticApp mApp;

    private IDiagnoseModel mModel;

    private final String mTestLevelPCBA = TestLevel.TYPES[TestLevel.TYPE_PCBA];

    private final String mTestLevelASSY = TestLevel.TYPES[TestLevel.TYPE_ASSY];

    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;

    private final String mTestCaseName = "com.msi.diagnostic.ui.GyroscopeTestCaseView";

    private final String mTestItemfile = "/ueventd.rc";

    private final String mGyroscopeItemName = "isGyroscopeExisted";

    private final String mXItemName = "isXVectorExceeded";

    private final String mYItemName = "isYVectorExceeded";

    private final String mZItemName = "isZVectorExceeded";

    private final boolean mVerifiedInfoDefine = true;

    private float[][] mCriteriaValues = {
        {
                9000, 2
        }
    };

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockGyroscopeTestCaseView(mApp);
    }

    public void test_handlePCBAItem_ValidValue_ResultPass() {
        mMockView.setLevelName(mTestLevelPCBA);

        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelPCBA, mTestLevelCaption);
        TestCase testCase = new TestCase(mApp, 0, mTestCaseName, mTestLevelPCBA, null,
                TestResult.create(TestResult.NONE), 0);

        TestItem mGyroscopeItem = new MockDetectionTestItem(mApp, 0, mGyroscopeItemName,
                mTestItemfile, mTestLevelPCBA, mTestCaseName, mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));

        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(mGyroscopeItem);

        mPresenter = new GyroscopePagePresenter(mMockView);
        mPresenter.resume();

        TestItem testItem = mModel.loadTestItem(mTestLevelPCBA, mGyroscopeItemName);
        assertEquals(TestResult.PASS, testItem.getResult());
        mPresenter = null;
    }

    public void test_handleASSYItem_InValidValue_ResultPASS() {
        mMockView.setLevelName(mTestLevelASSY);

        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelASSY, mTestLevelCaption);
        TestCase testCase = new TestCase(mApp, 0, mTestCaseName, mTestLevelASSY, null,
                TestResult.create(TestResult.NONE), 0);

        TestItem mXItem = new MockThresholdTestItem(mApp, 0, mXItemName, mTestItemfile,
                mTestLevelASSY, mTestCaseName, mCriteriaValues[0][0], mCriteriaValues[0][1],
                TestResult.create(TestResult.NONE));
        TestItem mYItem = new MockThresholdTestItem(mApp, 0, mYItemName, mTestItemfile,
                mTestLevelASSY, mTestCaseName, mCriteriaValues[0][0], mCriteriaValues[0][1],
                TestResult.create(TestResult.NONE));
        TestItem mZItem = new MockThresholdTestItem(mApp, 0, mZItemName, mTestItemfile,
                mTestLevelASSY, mTestCaseName, mCriteriaValues[0][0], mCriteriaValues[0][1],
                TestResult.create(TestResult.NONE));

        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(mXItem);
        mModel.addTestItem(mYItem);
        mModel.addTestItem(mZItem);

        mPresenter = new GyroscopePagePresenter(mMockView);
        mPresenter.resume();

        SensorEventInfo sensorEventInfo = new SensorEventInfo();
        sensorEventInfo.SENSOR_EVENT_VALUE[0] = 1;
        sensorEventInfo.SENSOR_EVENT_VALUE[1] = 10;
        sensorEventInfo.SENSOR_EVENT_VALUE[2] = 100;
        sensorEventInfo.TYPE_SESOR = Sensor.TYPE_GYROSCOPE;

        mPresenter.onSensorChanged(sensorEventInfo);

        TestItem testXItem = mModel.loadTestItem(mTestLevelASSY, mXItemName);
        assertEquals(TestResult.FAIL, testXItem.getResult());

        TestItem testYItem = mModel.loadTestItem(mTestLevelASSY, mYItemName);
        assertEquals(TestResult.PASS, testYItem.getResult());

        TestItem testZItem = mModel.loadTestItem(mTestLevelASSY, mZItemName);
        assertEquals(TestResult.PASS, testZItem.getResult());
    }

    @Override
    protected void tearDown() throws Exception {
        Log.i(TAG, "tearDown...");

        mMockView = null;
    }
}
