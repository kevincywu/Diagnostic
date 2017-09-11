package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.CompassPagePresenter;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.app.SensorEventInfo;

import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDetectionTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.test.AndroidTestCase;
import android.util.Log;

public class CompassTest extends AndroidTestCase {
    private static final String TAG = "CompassTest";

    private MockCompassTestCaseView mMockView;

    private CompassPagePresenter mPresenter;

    private MockDiagnosticApp mApp;

    private IDiagnoseModel mModel;

    private final String mTestLevelPCBA = TestLevel.TYPES[TestLevel.TYPE_PCBA];

    private final String mTestLevelASSY = TestLevel.TYPES[TestLevel.TYPE_ASSY];

    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;

    private final String mTestCaseName = "com.msi.diagnostic.ui.CompassTestCaseView";

    private final String mTestItemName = "isDirectToNorth";

    private final boolean mVerifiedInfoDefine = true;

    private float ANGLE_SCREEN = 90;

    @Override
    protected void setUp() throws Exception {
        Log.i(TAG, "setUp...");

        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockCompassTestCaseView(mApp);
    }

    public void test_handlePCBAItem_ValidValue_ResultPass() {
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelPCBA, mTestLevelCaption);
        TestCase testCase =
                new TestCase(mApp, 0, mTestCaseName, mTestLevelPCBA, null,
                        TestResult.create(TestResult.NONE), 0);
        TestItem testItem =
                new MockDetectionTestItem(mApp, 0, mTestItemName, null, mTestLevelPCBA,
                        mTestCaseName, mVerifiedInfoDefine, TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItem);

        mPresenter = new CompassPagePresenter(mMockView);
        mPresenter.resume();

        mPresenter.OnButtonClick(R.id.pass);
        TestItem testItemPass = mModel.loadTestItem(mTestLevelPCBA, mTestItemName);
        assertEquals(TestResult.PASS, testItemPass.getResult());

        mPresenter.OnButtonClick(R.id.fail);
        TestItem testItemFail = mModel.loadTestItem(mTestLevelPCBA, mTestItemName);
        assertEquals(TestResult.FAIL, testItemFail.getResult());

        SensorEventInfo accSensorEventInfo = new SensorEventInfo();
        accSensorEventInfo.SENSOR_EVENT_VALUE[0] = 59;
        accSensorEventInfo.SENSOR_EVENT_VALUE[1] = 25;
        accSensorEventInfo.SENSOR_EVENT_VALUE[2] = 58;
        accSensorEventInfo.TYPE_SESOR = Sensor.TYPE_ACCELEROMETER;

        mPresenter.onSensorChanged(accSensorEventInfo);

        SensorEventInfo magSensorEventInfo = new SensorEventInfo();
        magSensorEventInfo.SENSOR_EVENT_VALUE[0] = 20;
        magSensorEventInfo.SENSOR_EVENT_VALUE[1] = 18;
        magSensorEventInfo.SENSOR_EVENT_VALUE[2] = 25;
        magSensorEventInfo.TYPE_SESOR = Sensor.TYPE_MAGNETIC_FIELD;

        mPresenter.onSensorChanged(magSensorEventInfo);

        float[] expectValues = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accSensorEventInfo.SENSOR_EVENT_VALUE,
                magSensorEventInfo.SENSOR_EVENT_VALUE);
        SensorManager.getOrientation(R, expectValues);
        expectValues[0] = (float) Math.toDegrees(expectValues[0]) + ANGLE_SCREEN;

        assertEquals(expectValues[0], mMockView.getDirection());
    }

    public void test_handleASSYItem_ValidValue_ResultPass() {
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelASSY, mTestLevelCaption);
        TestCase testCase =
                new TestCase(mApp, 0, mTestCaseName, mTestLevelASSY, null,
                        TestResult.create(TestResult.NONE), 0);
        TestItem testItem =
                new MockDetectionTestItem(mApp, 0, mTestItemName, null, mTestLevelASSY,
                        mTestCaseName, mVerifiedInfoDefine, TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItem);

        mPresenter = new CompassPagePresenter(mMockView);
        mPresenter.resume();

        mPresenter.OnButtonClick(R.id.pass);
        TestItem testItemPass = mModel.loadTestItem(mTestLevelASSY, mTestItemName);
        assertEquals(TestResult.PASS, testItemPass.getResult());

        mPresenter.OnButtonClick(R.id.fail);
        TestItem testItemFail = mModel.loadTestItem(mTestLevelASSY, mTestItemName);
        assertEquals(TestResult.FAIL, testItemFail.getResult());

        SensorEventInfo accSensorEventInfo = new SensorEventInfo();
        accSensorEventInfo.SENSOR_EVENT_VALUE[0] = 59;
        accSensorEventInfo.SENSOR_EVENT_VALUE[1] = 25;
        accSensorEventInfo.SENSOR_EVENT_VALUE[2] = 58;
        accSensorEventInfo.TYPE_SESOR = Sensor.TYPE_ACCELEROMETER;

        mPresenter.onSensorChanged(accSensorEventInfo);

        SensorEventInfo magSensorEventInfo = new SensorEventInfo();
        magSensorEventInfo.SENSOR_EVENT_VALUE[0] = 20;
        magSensorEventInfo.SENSOR_EVENT_VALUE[1] = 18;
        magSensorEventInfo.SENSOR_EVENT_VALUE[2] = 25;
        magSensorEventInfo.TYPE_SESOR = Sensor.TYPE_MAGNETIC_FIELD;

        mPresenter.onSensorChanged(magSensorEventInfo);

        float[] expectValues = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accSensorEventInfo.SENSOR_EVENT_VALUE,
                magSensorEventInfo.SENSOR_EVENT_VALUE);
        SensorManager.getOrientation(R, expectValues);
        expectValues[0] = (float) Math.toDegrees(expectValues[0]) + ANGLE_SCREEN;

        assertEquals(expectValues[0], mMockView.getDirection());
    }

    @Override
    protected void tearDown() throws Exception {
        Log.i(TAG, "tearDown...");

        mMockView = null;
    }
}
