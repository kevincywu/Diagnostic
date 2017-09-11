
package com.msi.diagnostic.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.BatteryPagePresenterN728;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDefinitionTestItem;
import com.msi.diagnostic.data.MockThresholdTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

public class BatteryN728Test extends AndroidTestCase
{
    private static final String TAG = "BatteryN728Test";

    private BatteryPagePresenterN728 mTestPresenter;
    private MockBatteryTestCaseViewN728 mMockView;
    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;

    private final String mTestLevelPCBA = TestLevel.TYPES[TestLevel.TYPE_PCBA];
    private final String mTestLevelASSY = TestLevel.TYPES[TestLevel.TYPE_ASSY];
    private final String mTestLevelRUNIN = TestLevel.TYPES[TestLevel.TYPE_RUNIN];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mTestCasePCBA = "com.msi.diagnostic.ui.PCBAPanelView";
    private final String mTestCaseASSY = "com.msi.diagnostic.ui.ASSYPanelView";
    private final String mTestCaseRUNIN = "com.msi.diagnostic.ui.RUNINPanelView";

    private final String mTestItemNameHealth = "isValidHealth";
    private final String mTestItemNameCurrent = "isValidCurrent";
    private final String mTestItemNameLevel = "isValidLevel";
    private final String mTestItemNameVoltage = "isValidVoltage";
    private final String mTestItemNameTemperature = "isValidTemperature";

    private float[][] mCriteriaValues = {
            { // Level
                    (float) (1.0), (float) (100.0)
            }, { // Voltage
                    (float) (3000.0), (float) (4250.0)
            }, { // Temperature
                    (float) (0.0), (float) (45.0)
            }, { // Current
                    (float) (1.0), (float) (2000.0)
            }
    };
    private final String fakeCurrentfile = "fake_currentValue";

    @Override
    protected void setUp() throws Exception
    {
        Log.d(TAG, "setUp...");
        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockBatteryTestCaseViewN728(mApp);
    }
    @Override
    protected void tearDown() throws Exception
    {
        Log.d(TAG, "tearDown...");
    }

    public void test_handlePCBAItem_ValidValue_WithResult() {


        final String mVerifiedInfoDefine = "Good";
        // create the items for Model
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelPCBA, mTestLevelCaption);
        TestCase testCase = new TestCase(mApp, 0, mTestCasePCBA,
                mTestLevelPCBA, null, TestResult.create(TestResult.NONE),
                0);

        TestItem testItem = new MockDefinitionTestItem(mApp, 0, mTestItemNameHealth,
                null, mTestLevelPCBA, mTestCasePCBA,
                mVerifiedInfoDefine, TestResult.create(TestResult.NONE));
        TestItem testItemCurrent = new MockThresholdTestItem(mApp, 0, mTestItemNameCurrent,
                "/data/data/com.msi.diagnostic/files/" + fakeCurrentfile, mTestLevelPCBA,
                mTestCasePCBA,
                mCriteriaValues[3][1], mCriteriaValues[3][0], TestResult.create(TestResult.NONE));

        TestItem testItemLevel = new MockThresholdTestItem(mApp, 0, mTestItemNameLevel,
                null, mTestLevelPCBA, mTestCasePCBA,
                mCriteriaValues[0][1], mCriteriaValues[0][0], TestResult.create(TestResult.NONE));

        TestItem testItemVoltage = new MockThresholdTestItem(mApp, 0, mTestItemNameVoltage,
                null, mTestLevelPCBA, mTestCasePCBA,
                mCriteriaValues[1][1], mCriteriaValues[1][0], TestResult.create(TestResult.NONE));

        TestItem testItemTemperature = new MockThresholdTestItem(mApp, 0,
                mTestItemNameTemperature,
                null, mTestLevelPCBA, mTestCasePCBA,
                mCriteriaValues[2][1], mCriteriaValues[2][0], TestResult.create(TestResult.NONE));

        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItem);
        mModel.addTestItem(testItemCurrent);
        mModel.addTestItem(testItemLevel);
        mModel.addTestItem(testItemVoltage);
        mModel.addTestItem(testItemTemperature);

        mTestPresenter = new BatteryPagePresenterN728(mMockView);
        mTestPresenter.resume(); // To initialize the data

        /**status changed health pass and fail test***/
        // Ready to send the intent (intent extra about Battery Manager...)
        Intent intent1 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent1.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent1.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent1.putExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_GOOD);

        // Inform the BroadcastReceiver component
        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent1);

        assertEquals("PASS", mMockView.getHealthTestResult());

        Intent intent2 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent2.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent2.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent2.putExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_OVERHEAT);

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent2);

        assertEquals("FAIL", mMockView.getHealthTestResult());

        /**level pass and fail test**/
        Intent intent3 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent3.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent3.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent3.putExtra(BatteryManager.EXTRA_LEVEL, (int) (70));
        intent3.putExtra(BatteryManager.EXTRA_SCALE, (int) 100);

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent3);
        assertEquals("PASS", mMockView.getLevelTestResult());

        Intent intent4 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent4.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent4.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent4.putExtra(BatteryManager.EXTRA_LEVEL, (int) (-20));
        intent4.putExtra(BatteryManager.EXTRA_SCALE, (int) 100);

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent4);
        assertEquals("FAIL", mMockView.getLevelTestResult());

        /**voltage pass and fail test**/
        Intent intent5 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent5.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent5.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent5.putExtra(BatteryManager.EXTRA_VOLTAGE, (int) (4000));

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent5);
        assertEquals("PASS", mMockView.getVoltageTestResult());

        Intent intent6 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent6.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent6.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent6.putExtra(BatteryManager.EXTRA_VOLTAGE, (int) (2000));

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent6);
        assertEquals("FAIL", mMockView.getVoltageTestResult());

        /**temperature pass and fail test**/
        Intent intent7 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent7.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent7.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent7.putExtra(BatteryManager.EXTRA_TEMPERATURE, (int) (300));

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent7);
        assertEquals("PASS", mMockView.getTemperatureTestResult());

        Intent intent8 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent8.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent8.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent8.putExtra(BatteryManager.EXTRA_TEMPERATURE, (int) (-20));

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent8);
        assertEquals("FAIL", mMockView.getTemperatureTestResult());

        /**current pass and fail ,not file test**/
        Intent intent9 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent9.putExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_CHARGING);
        intent9.putExtra(BatteryManager.EXTRA_PLUGGED,
                BatteryManager.BATTERY_PLUGGED_AC);

        // Construct fake current file
        try {

            String currentString = "-1500";
            FileOutputStream fos = mMockView.getDiagnosticApp()
                    .getAppContext().openFileOutput(fakeCurrentfile, Context.MODE_PRIVATE);
            fos.write(currentString.getBytes());
            fos.close();
            Log.i("TAG_BatteryTester", "fakeCurrentFile..ok");

        } catch (IOException e) {
            e.printStackTrace();
        }
        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().
                getAppContext(), intent9);
        assertEquals("PASS", mMockView.getCurrentTestResult());

        Intent intent10 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent10.putExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_CHARGING);
        intent10.putExtra(BatteryManager.EXTRA_PLUGGED,
                BatteryManager.BATTERY_PLUGGED_AC);

        // Construct fake current file
        try {

            String currentString = "-3500";
            FileOutputStream fos = mMockView.getDiagnosticApp()
                    .getAppContext().openFileOutput(fakeCurrentfile, Context.MODE_PRIVATE);
            fos.write(currentString.getBytes());
            fos.close();
            Log.i("TAG_BatteryTester", "fakeCurrentFile..ok");

        } catch (IOException e) {
            e.printStackTrace();
        }
        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().
                getAppContext(), intent10);
        assertEquals("FAIL", mMockView.getCurrentTestResult());

        Intent intent11 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent11.putExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_CHARGING);
        intent11.putExtra(BatteryManager.EXTRA_PLUGGED,
                BatteryManager.BATTERY_PLUGGED_AC);

        // Remove the fake file.
        File dir = mMockView.getDiagnosticApp().getAppContext().getFilesDir();
        File file = new File(dir, fakeCurrentfile);
        if (file.exists()) {
            Log.i("TAG_BatteryTester", "fake File is existed...remove....");
            boolean isDeleted = file.delete();
            if (isDeleted) {
                Log.i("TAG_BatteryTester", "remove fake File..ok");
            }
            else {
                Log.i("TAG_BatteryTester", "remove fake File..fail");
            }
        }
        else {
            Log.i("TAG_BatteryTester", "fake File is not existed....pass..");
        }
        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().
                getAppContext(), intent11);
        assertEquals(Integer.toString(R.string.gain_battery_current_path_fail),
                mMockView.getCurrentTestResult());
    }

    public void test_handleASSYItem_ValidValue_WithResult() {

        final String mVerifiedInfoDefine = "Good";
        // create the items for Model
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelASSY, mTestLevelCaption);
        TestCase testCase = new TestCase(mApp, 0, mTestCaseASSY,
                mTestLevelASSY, null, TestResult.create(TestResult.NONE),
                0);

        TestItem testItem = new MockDefinitionTestItem(mApp, 0, mTestItemNameHealth,
                null, mTestLevelASSY, mTestCaseASSY,
                mVerifiedInfoDefine, TestResult.create(TestResult.NONE));
        TestItem testItemCurrent = new MockThresholdTestItem(mApp, 0, mTestItemNameCurrent,
                "/data/data/com.msi.diagnostic/files/" + fakeCurrentfile, mTestLevelASSY,
                mTestCaseASSY,
                mCriteriaValues[3][1], mCriteriaValues[3][0], TestResult.create(TestResult.NONE));

        TestItem testItemLevel = new MockThresholdTestItem(mApp, 0, mTestItemNameLevel,
                null, mTestLevelASSY, mTestCaseASSY,
                mCriteriaValues[0][1], mCriteriaValues[0][0], TestResult.create(TestResult.NONE));

        TestItem testItemVoltage = new MockThresholdTestItem(mApp, 0, mTestItemNameVoltage,
                null, mTestLevelASSY, mTestCaseASSY,
                mCriteriaValues[1][1], mCriteriaValues[1][0], TestResult.create(TestResult.NONE));

        TestItem testItemTemperature = new MockThresholdTestItem(mApp, 0,
                mTestItemNameTemperature,
                null, mTestLevelASSY, mTestCaseASSY,
                mCriteriaValues[2][1], mCriteriaValues[2][0], TestResult.create(TestResult.NONE));

        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItem);
        mModel.addTestItem(testItemCurrent);
        mModel.addTestItem(testItemLevel);
        mModel.addTestItem(testItemVoltage);
        mModel.addTestItem(testItemTemperature);

        mTestPresenter = new BatteryPagePresenterN728(mMockView);
        mTestPresenter.resume(); // To initialize the data

        /**status changed health pass and fail test***/
        // Ready to send the intent (intent extra about Battery Manager...)
        Intent intent1 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent1.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent1.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent1.putExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_GOOD);

        // Inform the BroadcastReceiver component
        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent1);

        assertEquals("PASS", mMockView.getHealthTestResult());

        Intent intent2 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent2.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent2.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent2.putExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_OVERHEAT);

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent2);

        assertEquals("FAIL", mMockView.getHealthTestResult());

        /**level pass and fail test**/
        Intent intent3 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent3.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent3.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent3.putExtra(BatteryManager.EXTRA_LEVEL, (int) (70));
        intent3.putExtra(BatteryManager.EXTRA_SCALE, (int) 100);

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent3);
        assertEquals("PASS", mMockView.getLevelTestResult());

        Intent intent4 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent4.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent4.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent4.putExtra(BatteryManager.EXTRA_LEVEL, (int) (-20));
        intent4.putExtra(BatteryManager.EXTRA_SCALE, (int) 100);

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent4);
        assertEquals("FAIL", mMockView.getLevelTestResult());

        /**voltage pass and fail test**/
        Intent intent5 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent5.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent5.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent5.putExtra(BatteryManager.EXTRA_VOLTAGE, (int) (4000));

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent5);
        assertEquals("PASS", mMockView.getVoltageTestResult());

        Intent intent6 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent6.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent6.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent6.putExtra(BatteryManager.EXTRA_VOLTAGE, (int) (2000));

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent6);
        assertEquals("FAIL", mMockView.getVoltageTestResult());

        /**temperature pass and fail test**/
        Intent intent7 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent7.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent7.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent7.putExtra(BatteryManager.EXTRA_TEMPERATURE, (int) (300));

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent7);
        assertEquals("PASS", mMockView.getTemperatureTestResult());

        Intent intent8 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent8.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent8.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent8.putExtra(BatteryManager.EXTRA_TEMPERATURE, (int) (-20));

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent8);
        assertEquals("FAIL", mMockView.getTemperatureTestResult());

        /**current pass and fail ,not file test**/
        Intent intent9 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent9.putExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_CHARGING);
        intent9.putExtra(BatteryManager.EXTRA_PLUGGED,
                BatteryManager.BATTERY_PLUGGED_AC);

        // Construct fake current file
        try {

            String currentString = "-1500";
            FileOutputStream fos = mMockView.getDiagnosticApp()
                    .getAppContext().openFileOutput(fakeCurrentfile, Context.MODE_PRIVATE);
            fos.write(currentString.getBytes());
            fos.close();
            Log.i("TAG_BatteryTester", "fakeCurrentFile..ok");

        } catch (IOException e) {
            e.printStackTrace();
        }
        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().
                getAppContext(), intent9);
        assertEquals("PASS", mMockView.getCurrentTestResult());

        Intent intent10 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent10.putExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_CHARGING);
        intent10.putExtra(BatteryManager.EXTRA_PLUGGED,
                BatteryManager.BATTERY_PLUGGED_AC);

        // Construct fake current file
        try {

            String currentString = "-3500";
            FileOutputStream fos = mMockView.getDiagnosticApp()
                    .getAppContext().openFileOutput(fakeCurrentfile, Context.MODE_PRIVATE);
            fos.write(currentString.getBytes());
            fos.close();
            Log.i("TAG_BatteryTester", "fakeCurrentFile..ok");

        } catch (IOException e) {
            e.printStackTrace();
        }
        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().
                getAppContext(), intent10);
        assertEquals("FAIL", mMockView.getCurrentTestResult());

        Intent intent11 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent11.putExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_CHARGING);
        intent11.putExtra(BatteryManager.EXTRA_PLUGGED,
                BatteryManager.BATTERY_PLUGGED_AC);

        // Remove the fake file.
        File dir = mMockView.getDiagnosticApp().getAppContext().getFilesDir();
        File file = new File(dir, fakeCurrentfile);
        if (file.exists()) {
            Log.i("TAG_BatteryTester", "fake File is existed...remove....");
            boolean isDeleted = file.delete();
            if (isDeleted) {
                Log.i("TAG_BatteryTester", "remove fake File..ok");
            }
            else {
                Log.i("TAG_BatteryTester", "remove fake File..fail");
            }
        }
        else {
            Log.i("TAG_BatteryTester", "fake File is not existed....pass..");
        }
        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().
                getAppContext(), intent11);
        assertEquals(Integer.toString(R.string.gain_battery_current_path_fail),
                mMockView.getCurrentTestResult());

    }

    public void test_handleRUNINItem_ValidValue_WithResult() {


        final String mVerifiedInfoDefine = "Good";
        // create the items for Model
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelRUNIN, mTestLevelCaption);
        TestCase testCase = new TestCase(mApp, 0, mTestCaseRUNIN,
                mTestLevelRUNIN, null, TestResult.create(TestResult.NONE),
                0);

        TestItem testItem = new MockDefinitionTestItem(mApp, 0, mTestItemNameHealth,
                null, mTestLevelRUNIN, mTestCaseRUNIN,
                mVerifiedInfoDefine, TestResult.create(TestResult.NONE));
        TestItem testItemCurrent = new MockThresholdTestItem(mApp, 0, mTestItemNameCurrent,
                "/data/data/com.msi.diagnostic/files/" + fakeCurrentfile, mTestLevelRUNIN,
                mTestCaseRUNIN,
                mCriteriaValues[3][1], mCriteriaValues[3][0], TestResult.create(TestResult.NONE));

        TestItem testItemLevel = new MockThresholdTestItem(mApp, 0, mTestItemNameLevel,
                null, mTestLevelRUNIN, mTestCaseRUNIN,
                mCriteriaValues[0][1], mCriteriaValues[0][0], TestResult.create(TestResult.NONE));

        TestItem testItemVoltage = new MockThresholdTestItem(mApp, 0, mTestItemNameVoltage,
                null, mTestLevelRUNIN, mTestCaseRUNIN,
                mCriteriaValues[1][1], mCriteriaValues[1][0], TestResult.create(TestResult.NONE));

        TestItem testItemTemperature = new MockThresholdTestItem(mApp, 0,
                mTestItemNameTemperature,
                null, mTestLevelRUNIN, mTestCaseRUNIN,
                mCriteriaValues[2][1], mCriteriaValues[2][0], TestResult.create(TestResult.NONE));

        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItem);
        mModel.addTestItem(testItemCurrent);
        mModel.addTestItem(testItemLevel);
        mModel.addTestItem(testItemVoltage);
        mModel.addTestItem(testItemTemperature);

        mTestPresenter = new BatteryPagePresenterN728(mMockView);
        mTestPresenter.resume(); // To initialize the data

        /**status changed health pass and fail test***/
        // Ready to send the intent (intent extra about Battery Manager...)
        Intent intent1 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent1.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent1.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent1.putExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_GOOD);

        // Inform the BroadcastReceiver component
        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent1);

        assertEquals("PASS", mMockView.getHealthTestResult());

        Intent intent2 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent2.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent2.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent2.putExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_OVERHEAT);

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent2);

        assertEquals("FAIL", mMockView.getHealthTestResult());

        /**level pass and fail test**/
        Intent intent3 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent3.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent3.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent3.putExtra(BatteryManager.EXTRA_LEVEL, (int) (70));
        intent3.putExtra(BatteryManager.EXTRA_SCALE, (int) 100);

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent3);
        assertEquals("PASS", mMockView.getLevelTestResult());

        Intent intent4 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent4.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent4.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent4.putExtra(BatteryManager.EXTRA_LEVEL, (int) (-20));
        intent4.putExtra(BatteryManager.EXTRA_SCALE, (int) 100);

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent4);
        assertEquals("FAIL", mMockView.getLevelTestResult());

        /**voltage pass and fail test**/
        Intent intent5 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent5.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent5.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent5.putExtra(BatteryManager.EXTRA_VOLTAGE, (int) (4000));

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent5);
        assertEquals("PASS", mMockView.getVoltageTestResult());

        Intent intent6 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent6.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent6.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent6.putExtra(BatteryManager.EXTRA_VOLTAGE, (int) (2000));

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent6);
        assertEquals("FAIL", mMockView.getVoltageTestResult());

        /**temperature pass and fail test**/
        Intent intent7 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent7.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent7.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent7.putExtra(BatteryManager.EXTRA_TEMPERATURE, (int) (300));

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent7);
        assertEquals("PASS", mMockView.getTemperatureTestResult());

        Intent intent8 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent8.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_CHARGING);
        intent8.putExtra(BatteryManager.EXTRA_PLUGGED, BatteryManager.BATTERY_PLUGGED_AC);
        intent8.putExtra(BatteryManager.EXTRA_TEMPERATURE, (int) (-20));

        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().getAppContext(),
                intent8);
        assertEquals("FAIL", mMockView.getTemperatureTestResult());

        /**current pass and fail ,not file test**/
        Intent intent9 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent9.putExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_CHARGING);
        intent9.putExtra(BatteryManager.EXTRA_PLUGGED,
                BatteryManager.BATTERY_PLUGGED_AC);

        // Construct fake current file
        try {

            String currentString = "-1500";
            FileOutputStream fos = mMockView.getDiagnosticApp()
                    .getAppContext().openFileOutput(fakeCurrentfile, Context.MODE_PRIVATE);
            fos.write(currentString.getBytes());
            fos.close();
            Log.i("TAG_BatteryTester", "fakeCurrentFile..ok");

        } catch (IOException e) {
            e.printStackTrace();
        }
        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().
                getAppContext(), intent9);
        assertEquals("PASS", mMockView.getCurrentTestResult());

        Intent intent10 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent10.putExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_CHARGING);
        intent10.putExtra(BatteryManager.EXTRA_PLUGGED,
                BatteryManager.BATTERY_PLUGGED_AC);

        // Construct fake current file
        try {

            String currentString = "-3500";
            FileOutputStream fos = mMockView.getDiagnosticApp()
                    .getAppContext().openFileOutput(fakeCurrentfile, Context.MODE_PRIVATE);
            fos.write(currentString.getBytes());
            fos.close();
            Log.i("TAG_BatteryTester", "fakeCurrentFile..ok");

        } catch (IOException e) {
            e.printStackTrace();
        }
        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().
                getAppContext(), intent10);
        assertEquals("FAIL", mMockView.getCurrentTestResult());

        Intent intent11 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent11.putExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_CHARGING);
        intent11.putExtra(BatteryManager.EXTRA_PLUGGED,
                BatteryManager.BATTERY_PLUGGED_AC);

        // Remove the fake file.
        File dir = mMockView.getDiagnosticApp().getAppContext().getFilesDir();
        File file = new File(dir, fakeCurrentfile);
        if (file.exists()) {
            Log.i("TAG_BatteryTester", "fake File is existed...remove....");
            boolean isDeleted = file.delete();
            if (isDeleted) {
                Log.i("TAG_BatteryTester", "remove fake File..ok");
            }
            else {
                Log.i("TAG_BatteryTester", "remove fake File..fail");
            }
        }
        else {
            Log.i("TAG_BatteryTester", "fake File is not existed....pass..");
        }
        mTestPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp().
                getAppContext(), intent11);
        assertEquals(Integer.toString(R.string.gain_battery_current_path_fail),
                mMockView.getCurrentTestResult());
    }
}
