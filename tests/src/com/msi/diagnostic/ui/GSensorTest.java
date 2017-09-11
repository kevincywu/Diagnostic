package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.GSensorPagePresenter;
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

public class GSensorTest extends AndroidTestCase {
    private static final String TAG = "GSensorTest";

    private GSensorPagePresenter mPresenter;

    private MockGSensorTestCaseView mMockView;

    private MockDiagnosticApp mApp;

    private IDiagnoseModel mModel;

    private final String mTestLevelName = TestLevel.TYPES[TestLevel.TYPE_PCBA];

    private final String mTestLevelPCBA = TestLevel.TYPES[TestLevel.TYPE_PCBA];

    private final String mTestLevelASSY = TestLevel.TYPES[TestLevel.TYPE_ASSY];

    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;

    private final String mTestCaseName = "com.msi.diagnostic.ui.GSensorTestCaseView";

    private final String mTestItemNameXPos = "isXPosVectorExceeded";

    private final String mTestItemNameXNeg = "isXNegVectorExceeded";

    private final String mTestItemNameYPos = "isYPosVectorExceeded";

    private final String mTestItemNameYNeg = "isYNegVectorExceeded";

    private float[][] mCriteriaValues = {
            {
                    9000, 4
            }, {
                    -4, -9000
            }, {
                    9000, 4
            }, {
                    -4, -9000
            }
    };

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockGSensorTestCaseView(mApp);
    }

    public void test_handlePCBAItem_ValidValue_ResultPass() {
        mMockView.setLevelName(mTestLevelPCBA);

        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelPCBA, mTestLevelCaption);
        TestCase testCase = new TestCase(mApp, 0, mTestCaseName, mTestLevelPCBA, null,
                TestResult.create(TestResult.NONE), 0);

        TestItem mXPosItem = new MockThresholdTestItem(mApp, 0, mTestItemNameXPos, null,
                mTestLevelName, mTestCaseName, mCriteriaValues[0][0], mCriteriaValues[0][1],
                TestResult.create(TestResult.NONE));

        TestItem mXNegItem = new MockThresholdTestItem(mApp, 0, mTestItemNameXNeg, null,
                mTestLevelName, mTestCaseName, mCriteriaValues[1][0], mCriteriaValues[1][1],
                TestResult.create(TestResult.NONE));

        TestItem mYPosItem = new MockThresholdTestItem(mApp, 0, mTestItemNameYPos, null,
                mTestLevelName, mTestCaseName, mCriteriaValues[2][0], mCriteriaValues[2][1],
                TestResult.create(TestResult.NONE));

        TestItem mYNegItem = new MockThresholdTestItem(mApp, 0, mTestItemNameYNeg, null,
                mTestLevelName, mTestCaseName, mCriteriaValues[3][0], mCriteriaValues[3][1],
                TestResult.create(TestResult.NONE));

        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(mXPosItem);
        mModel.addTestItem(mXNegItem);
        mModel.addTestItem(mYPosItem);
        mModel.addTestItem(mYNegItem);

        mPresenter = new GSensorPagePresenter(mMockView);
        mPresenter.resume();

        SensorEventInfo sensorEventInfo = new SensorEventInfo();
        sensorEventInfo.SENSOR_EVENT_VALUE[0] = 100;
        sensorEventInfo.SENSOR_EVENT_VALUE[1] = -100;
        sensorEventInfo.SENSOR_EVENT_VALUE[2] = 100;
        sensorEventInfo.TYPE_SESOR = Sensor.TYPE_ACCELEROMETER;

        mPresenter.onSensorChanged(sensorEventInfo);

        TestItem testXPosItem = mModel.loadTestItem(mTestLevelASSY, mTestItemNameXPos);
        assertEquals(TestResult.PASS, testXPosItem.getResult());

        TestItem testXNegItem = mModel.loadTestItem(mTestLevelASSY, mTestItemNameXNeg);
        assertEquals(TestResult.PASS, testXNegItem.getResult());

        TestItem testYPosItem = mModel.loadTestItem(mTestLevelASSY, mTestItemNameYPos);
        assertEquals(TestResult.PASS, testYPosItem.getResult());

        TestItem testYNegItem = mModel.loadTestItem(mTestLevelASSY, mTestItemNameYNeg);
        assertEquals(TestResult.PASS, testYNegItem.getResult());
    }

    public void test_handleASSYItem_InValidValue_ResultPASS() {
        mMockView.setLevelName(mTestLevelASSY);

        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelASSY, mTestLevelCaption);
        TestCase testCase = new TestCase(mApp, 0, mTestCaseName, mTestLevelPCBA, null,
                TestResult.create(TestResult.NONE), 0);

        TestItem mXPosItem = new MockThresholdTestItem(mApp, 0, mTestItemNameXPos, null,
                mTestLevelName, mTestCaseName, mCriteriaValues[0][0], mCriteriaValues[0][1],
                TestResult.create(TestResult.NONE));

        TestItem mXNegItem = new MockThresholdTestItem(mApp, 0, mTestItemNameXNeg, null,
                mTestLevelName, mTestCaseName, mCriteriaValues[1][0], mCriteriaValues[1][1],
                TestResult.create(TestResult.NONE));

        TestItem mYPosItem = new MockThresholdTestItem(mApp, 0, mTestItemNameYPos, null,
                mTestLevelName, mTestCaseName, mCriteriaValues[2][0], mCriteriaValues[2][1],
                TestResult.create(TestResult.NONE));

        TestItem mYNegItem = new MockThresholdTestItem(mApp, 0, mTestItemNameYNeg, null,
                mTestLevelName, mTestCaseName, mCriteriaValues[3][0], mCriteriaValues[3][1],
                TestResult.create(TestResult.NONE));

        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(mXPosItem);
        mModel.addTestItem(mXNegItem);
        mModel.addTestItem(mYPosItem);
        mModel.addTestItem(mYNegItem);

        mPresenter = new GSensorPagePresenter(mMockView);
        mPresenter.resume();

        SensorEventInfo sensorEventInfo = new SensorEventInfo();
        sensorEventInfo.SENSOR_EVENT_VALUE[0] = 100;
        sensorEventInfo.SENSOR_EVENT_VALUE[1] = -100;
        sensorEventInfo.SENSOR_EVENT_VALUE[2] = 100;
        sensorEventInfo.TYPE_SESOR = Sensor.TYPE_ACCELEROMETER;

        mPresenter.onSensorChanged(sensorEventInfo);

        TestItem testXPosItem = mModel.loadTestItem(mTestLevelASSY, mTestItemNameXPos);
        assertEquals(TestResult.PASS, testXPosItem.getResult());

        TestItem testXNegItem = mModel.loadTestItem(mTestLevelASSY, mTestItemNameXNeg);
        assertEquals(TestResult.FAIL, testXNegItem.getResult());

        TestItem testYPosItem = mModel.loadTestItem(mTestLevelASSY, mTestItemNameYPos);
        assertEquals(TestResult.FAIL, testYPosItem.getResult());

        TestItem testYNegItem = mModel.loadTestItem(mTestLevelASSY, mTestItemNameYNeg);
        assertEquals(TestResult.PASS, testYNegItem.getResult());
    }

    @Override
    protected void tearDown() throws Exception {
        Log.i(TAG, "tearDown...");

        mMockView = null;
    }
}
