package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.LightSensorPagePresenter;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.app.SensorEventInfo;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockThresholdTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.hardware.Sensor;
import android.test.AndroidTestCase;
import android.util.Log;

public class LightSensorTest extends AndroidTestCase {
    private static final String TAG = "LightSensorTest";

    private LightSensorPagePresenter mPresenter;

    private MockLightSensorTestCaseView mMockView;

    private MockDiagnosticApp mApp;

    private IDiagnoseModel mModel;

    private final String mTestLevelPCBA = TestLevel.TYPES[TestLevel.TYPE_PCBA];

    private final String mTestLevelASSY = TestLevel.TYPES[TestLevel.TYPE_ASSY];

    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;

    private final String mTestCaseName = "com.msi.diagnostic.ui.LightSensorTestCaseView";

    private final String mLightSensorItemName = "isLuxExisted";

    private final String mLuxDarkItemName = "isLuxDarkExceed";

    private final String mLuxLightItemName = "isLuxLightExceed";

    private float[][] mCriteriaValues = {
            {
                    90000, 2
            }, {
                    500, 0
            }, {
                    90000, 900
            }
    };

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockLightSensorTestCaseView(mApp);
    }

    public void test_handlePCBAItem_ValidValue_ResultPass() {
        mMockView.setLevelName(mTestLevelPCBA);

        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelPCBA, mTestLevelCaption);
        TestCase testCase = new TestCase(mApp, 0, mTestCaseName, mTestLevelPCBA, null,
                TestResult.create(TestResult.NONE), 0);

        TestItem mLightSensorItem = new MockThresholdTestItem(mApp, 0, mLightSensorItemName, null,
                mTestLevelPCBA, mTestCaseName, mCriteriaValues[0][0], mCriteriaValues[0][1],
                TestResult.create(TestResult.NONE));

        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(mLightSensorItem);

        mPresenter = new LightSensorPagePresenter(mMockView);
        mPresenter.resume();

        SensorEventInfo sensorEventInfo = new SensorEventInfo();
        sensorEventInfo.SENSOR_EVENT_VALUE[0] = 10;
        sensorEventInfo.SENSOR_EVENT_VALUE[1] = 100;
        sensorEventInfo.SENSOR_EVENT_VALUE[2] = 1000;
        sensorEventInfo.TYPE_SESOR = Sensor.TYPE_LIGHT;

        mPresenter.onSensorChanged(sensorEventInfo);

        TestItem testItem = mModel.loadTestItem(mTestLevelPCBA, mLightSensorItemName);
        assertEquals(TestResult.PASS, testItem.getResult());
        mPresenter = null;
    }

    public void test_handleASSYItem_InValidValue_ResultPASS() {
        mMockView.setLevelName(mTestLevelASSY);

        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelASSY, mTestLevelCaption);
        TestCase testCase = new TestCase(mApp, 0, mTestCaseName, mTestLevelASSY, null,
                TestResult.create(TestResult.NONE), 0);

        TestItem mLuxDarkItem = new MockThresholdTestItem(mApp, 0, mLuxDarkItemName, null,
                mTestLevelASSY, mTestCaseName, mCriteriaValues[1][0], mCriteriaValues[1][1],
                TestResult.create(TestResult.NONE));
        TestItem mLuxLightItem = new MockThresholdTestItem(mApp, 0, mLuxLightItemName, null,
                mTestLevelASSY, mTestCaseName, mCriteriaValues[2][0], mCriteriaValues[2][1],
                TestResult.create(TestResult.NONE));

        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(mLuxDarkItem);
        mModel.addTestItem(mLuxLightItem);

        mPresenter = new LightSensorPagePresenter(mMockView);
        mPresenter.resume();

        SensorEventInfo sensorEventInfo = new SensorEventInfo();
        sensorEventInfo.SENSOR_EVENT_VALUE[0] = 1000;
        sensorEventInfo.SENSOR_EVENT_VALUE[1] = 10;
        sensorEventInfo.SENSOR_EVENT_VALUE[2] = 100;
        sensorEventInfo.TYPE_SESOR = Sensor.TYPE_LIGHT;

        mPresenter.onSensorChanged(sensorEventInfo);

        TestItem testLuxDarkItem = mModel.loadTestItem(mTestLevelASSY, mLuxDarkItemName);
        assertEquals(TestResult.FAIL, testLuxDarkItem.getResult());

        TestItem testLuxLightItem = mModel.loadTestItem(mTestLevelASSY, mLuxLightItemName);
        assertEquals(TestResult.PASS, testLuxLightItem.getResult());

    }

    @Override
    protected void tearDown() throws Exception {
        Log.i(TAG, "tearDown...");

        mMockView = null;
    }
}
