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
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.app.SpecialBatteryPagePresenter;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockThresholdTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

public class SpecialBatteryTest extends AndroidTestCase {
    private static String TAG = "SpecialBatteryTest";
    private SpecialBatteryPagePresenter mPresenter;
    private MockSpecialBatteryTestCaseView mMockView;
    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;

    private final String mTestLevelPCBA = TestLevel.TYPES[TestLevel.TYPE_PCBA];
    private final String mTestLevelASSY = TestLevel.TYPES[TestLevel.TYPE_ASSY];
    private final String mTestLevelRUNIN = TestLevel.TYPES[TestLevel.TYPE_RUNIN];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;

    private final String mTestCasePCBA = "com.msi.diagnostic.ui.PCBAPanelView";
    private final String mTestCaseASSY = "com.msi.diagnostic.ui.ASSYPanelView";
    private final String mTestCaseRUNIN = "com.msi.diagnostic.ui.RUNINPanelView";
    private final String mTestItemNameCurrent = "isValidCurrent";
    private final String fakeCurrentfile = "fake_currentValue";

    private final float mCriteriaMaxValue = (float) 2000.0;
    private final float mCriteriaMinValue = (float) 1.0;

    @Override
    protected void setUp() throws Exception {
        Log.i(TAG, "setUp...");
        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockSpecialBatteryTestCaseView(mApp);
    }

    @Override
    protected void tearDown() throws Exception {
        Log.d(TAG, "tearDown...");
    }

    public void test_handlePCBAItem_ValidValue_WithResult() {
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelPCBA,
                mTestLevelCaption);
        TestCase testCase = new TestCase(
                mApp,
                0,
                mTestCasePCBA,
                mTestLevelPCBA,
                null,
                TestResult.create(TestResult.NONE),
                0);
        TestItem testItemCurrent = new MockThresholdTestItem(
                mApp,
                0,
                mTestItemNameCurrent,
                "/data/data/com.msi.diagnostic/files/"
                        + fakeCurrentfile, mTestLevelPCBA, mTestCasePCBA,
                mCriteriaMaxValue,
                mCriteriaMinValue,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItemCurrent);
        mPresenter = new SpecialBatteryPagePresenter(mMockView);
        mPresenter.resume();

        /** current pass and fail ,not file test **/
        Intent intent1 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent1.putExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_CHARGING);
        intent1.putExtra(BatteryManager.EXTRA_PLUGGED,
                BatteryManager.BATTERY_PLUGGED_AC);
        // Construct fake current file
        try {

            String currentString = "1500";
            FileOutputStream fos = mMockView.getDiagnosticApp().getAppContext()
                    .openFileOutput(fakeCurrentfile, Context.MODE_PRIVATE);
            fos.write(currentString.getBytes());
            fos.close();
            Log.i("TAG_BatteryTester", "fakeCurrentFile..ok");

        } catch (IOException e) {
            e.printStackTrace();
        }
        mPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp()
                .getAppContext(), intent1);
        assertEquals("PASS", mMockView.getCurrentTestResult());

        Intent intent2 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent2.putExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_CHARGING);
        intent2.putExtra(BatteryManager.EXTRA_PLUGGED,
                BatteryManager.BATTERY_PLUGGED_AC);

        // Construct fake current file
        try {

            String currentString = "3500";
            FileOutputStream fos = mMockView.getDiagnosticApp().getAppContext()
                    .openFileOutput(fakeCurrentfile, Context.MODE_PRIVATE);
            fos.write(currentString.getBytes());
            fos.close();
            Log.i("TAG_BatteryTester", "fakeCurrentFile..ok");

        } catch (IOException e) {
            e.printStackTrace();
        }
        mPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp()
                .getAppContext(), intent2);
        assertEquals("FAIL", mMockView.getCurrentTestResult());

        Intent intent3 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent3.putExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_CHARGING);
        intent3.putExtra(BatteryManager.EXTRA_PLUGGED,
                BatteryManager.BATTERY_PLUGGED_AC);

        // Remove the fake file.
        File dir = mMockView.getDiagnosticApp().getAppContext().getFilesDir();
        File file = new File(dir, fakeCurrentfile);
        if (file.exists()) {
            Log.i("TAG_BatteryTester", "fake File is existed...remove....");
            boolean isDeleted = file.delete();
            if (isDeleted) {
                Log.i("TAG_BatteryTester", "remove fake File..ok");
            } else {
                Log.i("TAG_BatteryTester", "remove fake File..fail");
            }
        } else {
            Log.i("TAG_BatteryTester", "fake File is not existed....pass..");
        }
        mPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp()
                .getAppContext(), intent3);
        assertEquals(Integer.toString(R.string.gain_battery_current_path_fail),
                mMockView.getCurrentTestResult());
    }

    public void test_handleASSYItem_ValidValue_WithResult() {
        TestLevel testLevel = new TestLevel(
                mApp,
                0,
                mTestLevelASSY,
                mTestLevelCaption);
        TestCase testCase = new TestCase(
                mApp,
                0,
                mTestCaseASSY,
                mTestLevelASSY,
                null,
                TestResult.create(TestResult.NONE),
                0);
        TestItem testItemCurrent = new MockThresholdTestItem(
                mApp,
                0,
                mTestItemNameCurrent,
                "/data/data/com.msi.diagnostic/files/"
                        + fakeCurrentfile, mTestLevelASSY, mTestCaseASSY,
                mCriteriaMaxValue, mCriteriaMinValue,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItemCurrent);
        mPresenter = new SpecialBatteryPagePresenter(mMockView);
        mPresenter.resume();

        /** current pass and fail ,not file test **/
        Intent intent1 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent1.putExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_CHARGING);
        intent1.putExtra(BatteryManager.EXTRA_PLUGGED,
                BatteryManager.BATTERY_PLUGGED_AC);

        // Construct fake current file
        try {

            String currentString = "1500";
            FileOutputStream fos = mMockView.getDiagnosticApp().getAppContext()
                    .openFileOutput(fakeCurrentfile, Context.MODE_PRIVATE);
            fos.write(currentString.getBytes());
            fos.close();
            Log.i("TAG_BatteryTester", "fakeCurrentFile..ok");

        } catch (IOException e) {
            e.printStackTrace();
        }
        mPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp()
                .getAppContext(), intent1);
        assertEquals("PASS", mMockView.getCurrentTestResult());

        Intent intent2 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent2.putExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_CHARGING);
        intent2.putExtra(BatteryManager.EXTRA_PLUGGED,
                BatteryManager.BATTERY_PLUGGED_AC);

        // Construct fake current file
        try {

            String currentString = "3500";
            FileOutputStream fos = mMockView.getDiagnosticApp().getAppContext()
                    .openFileOutput(fakeCurrentfile, Context.MODE_PRIVATE);
            fos.write(currentString.getBytes());
            fos.close();
            Log.i("TAG_BatteryTester", "fakeCurrentFile..ok");

        } catch (IOException e) {
            e.printStackTrace();
        }
        mPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp()
                .getAppContext(), intent2);
        assertEquals("FAIL", mMockView.getCurrentTestResult());

        Intent intent3 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent3.putExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_CHARGING);
        intent3.putExtra(BatteryManager.EXTRA_PLUGGED,
                BatteryManager.BATTERY_PLUGGED_AC);

        // Remove the fake file.
        File dir = mMockView.getDiagnosticApp().getAppContext().getFilesDir();
        File file = new File(dir, fakeCurrentfile);
        if (file.exists()) {
            Log.i("TAG_BatteryTester", "fake File is existed...remove....");
            boolean isDeleted = file.delete();
            if (isDeleted) {
                Log.i("TAG_BatteryTester", "remove fake File..ok");
            } else {
                Log.i("TAG_BatteryTester", "remove fake File..fail");
            }
        } else {
            Log.i("TAG_BatteryTester", "fake File is not existed....pass..");
        }
        mPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp()
                .getAppContext(), intent3);
        assertEquals(Integer.toString(R.string.gain_battery_current_path_fail),
                mMockView.getCurrentTestResult());
    }

    public void test_handleRuninItem_ValidValue_WithResult() {
        TestLevel testLevel = new TestLevel(
                mApp,
                0,
                mTestLevelRUNIN,
                mTestLevelCaption);
        TestCase testCase = new TestCase(mApp,
                0,
                mTestCaseRUNIN,
                mTestLevelRUNIN,
                null,
                TestResult.create(TestResult.NONE),
                0);
        TestItem testItemCurrent = new MockThresholdTestItem(
                mApp,
                0,
                mTestItemNameCurrent,
                "/data/data/com.msi.diagnostic/files/"
                        + fakeCurrentfile, mTestLevelRUNIN, mTestCaseRUNIN,
                mCriteriaMaxValue,
                mCriteriaMinValue,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItemCurrent);
        mPresenter = new SpecialBatteryPagePresenter(mMockView);
        mPresenter.resume();

        /** current pass and fail ,not file test **/
        Intent intent1 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent1.putExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_CHARGING);
        intent1.putExtra(BatteryManager.EXTRA_PLUGGED,
                BatteryManager.BATTERY_PLUGGED_AC);

        // Construct fake current file
        try {

            String currentString = "1500";
            FileOutputStream fos = mMockView.getDiagnosticApp().getAppContext()
                    .openFileOutput(fakeCurrentfile, Context.MODE_PRIVATE);
            fos.write(currentString.getBytes());
            fos.close();
            Log.i("TAG_BatteryTester", "fakeCurrentFile..ok");

        } catch (IOException e) {
            e.printStackTrace();
        }
        mPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp()
                .getAppContext(), intent1);
        assertEquals("PASS", mMockView.getCurrentTestResult());

        Intent intent2 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent2.putExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_CHARGING);
        intent2.putExtra(BatteryManager.EXTRA_PLUGGED,
                BatteryManager.BATTERY_PLUGGED_AC);

        // Construct fake current file
        try {

            String currentString = "3500";
            FileOutputStream fos = mMockView.getDiagnosticApp().getAppContext()
                    .openFileOutput(fakeCurrentfile, Context.MODE_PRIVATE);
            fos.write(currentString.getBytes());
            fos.close();
            Log.i("TAG_BatteryTester", "fakeCurrentFile..ok");

        } catch (IOException e) {
            e.printStackTrace();
        }
        mPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp()
                .getAppContext(), intent2);
        assertEquals("FAIL", mMockView.getCurrentTestResult());

        Intent intent3 = new Intent(Intent.ACTION_POWER_CONNECTED);
        intent3.putExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_CHARGING);
        intent3.putExtra(BatteryManager.EXTRA_PLUGGED,
                BatteryManager.BATTERY_PLUGGED_AC);

        // Remove the fake file.
        File dir = mMockView.getDiagnosticApp().getAppContext().getFilesDir();
        File file = new File(dir, fakeCurrentfile);
        if (file.exists()) {
            Log.i("TAG_BatteryTester", "fake File is existed...remove....");
            boolean isDeleted = file.delete();
            if (isDeleted) {
                Log.i("TAG_BatteryTester", "remove fake File..ok");
            } else {
                Log.i("TAG_BatteryTester", "remove fake File..fail");
            }
        } else {
            Log.i("TAG_BatteryTester", "fake File is not existed....pass..");
        }
        mPresenter.onBatteryStatusChanged(mMockView.getDiagnosticApp()
                .getAppContext(), intent3);
        assertEquals(Integer.toString(R.string.gain_battery_current_path_fail),
                mMockView.getCurrentTestResult());
    }
}
