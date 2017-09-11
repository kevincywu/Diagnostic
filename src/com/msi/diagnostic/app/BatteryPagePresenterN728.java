package com.msi.diagnostic.app;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.util.Log;
import android.view.View;

import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DefinitionVerifiedInfo;
import com.msi.diagnostic.data.DeviceConfig;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.data.ThresholdVerifiedInfo;
import com.msi.diagnostic.ui.BatteryTestCaseView;
import com.msi.diagnostic.ui.BatteryTestCaseViewN728;
import com.msi.diagnostic.utils.CountDownWaitingTimer;
import com.msi.diagnostic.utils.ICountDownListener;
import com.msi.diagnostic.utils.Utils;

public class BatteryPagePresenterN728 extends AbstractTestCasePresenter implements
ICountDownListener {
    private static final String TAG = "BatteryPagePresenter";

    private static final int COUNT_DOWN_WAITING_TIME = 20000; // 20 second
    private static final int COUNT_DOWN_TIME_INTERVAL = 1000; // 1 second

    private static class TestItemId {
        public static final int IS_VALID_HEALTH = 0;
        public static final int IS_VALID_CURRENT = 1;
        public static final int IS_VALID_LEVEL = 2;
        public static final int IS_VALID_VOLTAGE = 3;
        public static final int IS_VALID_TEMPERATURE = 4;
    }

    private static final String[] TEST_ITEM_NAME = {
            "isValidHealth",
            "isValidCurrent",
            "isValidLevel",
            "isValidVoltage",
            "isValidTemperature"
    };

    private BatteryTestCaseViewN728 mView;
    private CountDownWaitingTimer mCountDownTimer;

    public IntentFilter mFilter;

    public BatteryPagePresenterN728(BatteryTestCaseViewN728 view) {
        super(view);
        mView = view;

        mFilter = new IntentFilter();
        mFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        mFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);

        mCountDownTimer = new CountDownWaitingTimer(COUNT_DOWN_WAITING_TIME,
                COUNT_DOWN_TIME_INTERVAL, this);

        mView.setTimePromptColor(Color.rgb(255, 20, 20));
        mView.setWarningPromptColor(Color.rgb(255, 20, 20));
    }

    @Override
    public void resume() {
        super.resume();
        mCountDownTimer.start();
    }

    @Override
    public void pause() {
        super.pause();
    }

    private void updateHealthResult(TestResult result) {
        String isPass = result.getResultAsString();
        mView.setHealthTestResult(isPass);
    }

    private void updateCurrentResult(TestResult result) {
        String isPass = result.getResultAsString();
        mView.setCurrentTestResult(isPass);
    }

    private void updateLevelResult(TestResult result) {
        String isPass = result.getResultAsString();
        mView.setLevelTestResult(isPass);
    }

    private void updateVoltageResult(TestResult result) {
        String isPass = result.getResultAsString();
        mView.setVoltageTestResult(isPass);
    }

    private void updateTemperatureResult(TestResult result) {
        String isPass = result.getResultAsString();
        mView.setTemperatureTestResult(isPass);
    }

    private TestResult isValidHealth(Intent intent) {
        int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
        String itemName = TEST_ITEM_NAME[TestItemId.IS_VALID_HEALTH];
        TestItem item = getTestItemByName(itemName);

        int healthStringResId = getHealthStateResString(health);
        String healthState = getHealthState(health);
        TestResult result = item.verify(new DefinitionVerifiedInfo(healthState));

        mView.setHealthState(healthStringResId);
        updateHealthResult(result);
        return result;
    }

    private TestResult isValidCurrent() {
        String itemName = TEST_ITEM_NAME[TestItemId.IS_VALID_CURRENT];
        TestItem item = getTestItemByName(itemName);
        String infoFileName = item.getInfoFileName();

        TestResult result = TestResult.RESULT_FAIL;
        String currentString = null;
        try {
            currentString = Utils.readContentFromFile(infoFileName);
            int currentValue = 0;
            if (currentString != null) {
                currentValue = Integer.valueOf(currentString);
                currentValue = -(currentValue);
                IDiagnosticApp app = (IDiagnosticApp) mView.getDiagnosticApp();
                DeviceConfig config = app.getDeviceConfig();
                if (config.getDeviceName().contentEquals(DeviceConfig.SPECIAL)) {
                    currentValue = currentValue / 1000;
                }
            }
            result = item.verify(new ThresholdVerifiedInfo(currentValue));

            mView.setCurrentValue(currentValue);
            updateCurrentResult(result);

        } catch (NullPointerException e) {
            e.printStackTrace();
            mView.setCurrentValueNull(R.string.gain_battery_current_path_fail);

        } catch (IOException e) {
            e.printStackTrace();
            mView.setCurrentValueNull(R.string.gain_battery_current_path_fail);
        }

        return result;
    }

    private TestResult isValidLevel(Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        String itemName = TEST_ITEM_NAME[TestItemId.IS_VALID_LEVEL];
        TestItem item = getTestItemByName(itemName);

        int batteryPct = (level * 100) / scale;
        TestResult result = item.verify(new ThresholdVerifiedInfo(batteryPct));
        mView.setLevelValue(batteryPct);
        updateLevelResult(result);
        return result;
    }

    private TestResult isValidVoltage(Intent intent) {
        float voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
        String itemName = TEST_ITEM_NAME[TestItemId.IS_VALID_VOLTAGE];
        TestItem item = getTestItemByName(itemName);

        TestResult result = item.verify(new ThresholdVerifiedInfo(voltage));
        float _voltage = voltage / 1000.0f;
        mView.setVoltageValue(_voltage);
        updateVoltageResult(result);
        return result;
    }

    /**
     * Format a number of tenths-units as a decimal string without using a
     * conversion to float. E.g. 347 -> "34.7"
     */
    private float tenths(float x) {
        float tens = x / 10;
        float left = tens;
        float right = (x - 10 * tens);
        return (left + right);
    }

    private TestResult isValidTemperature(Intent intent) {
        float temperature = intent.getIntExtra(
                BatteryManager.EXTRA_TEMPERATURE, -1);
        String itemName = TEST_ITEM_NAME[TestItemId.IS_VALID_TEMPERATURE];
        TestItem item = getTestItemByName(itemName);

        float _temperature = tenths(temperature);
        TestResult result = item.verify(new ThresholdVerifiedInfo(_temperature));
        mView.setTemperatureValue(_temperature);
        updateTemperatureResult(result);
        return result;
    }

    @Override
    public void OnButtonClick(int buttonId) {
        switch (buttonId) {
        // case R.id.submit:
        // mView.finishTestCase();
        // break;

        case R.id.submit:
            if (mStatus == BatteryManager.BATTERY_STATUS_NOT_CHARGING
                    || mStatus == BatteryManager.BATTERY_STATUS_DISCHARGING) {

                String itemName = TEST_ITEM_NAME[TestItemId.IS_VALID_CURRENT];
                TestItem item = getTestItemByName(itemName);
                item.setResult(TestResult.NONE);
                mView.getDiagnosticApp().getDiagnoseModel()
                        .updateTestItem(item);
            } else {

                String itemName = TEST_ITEM_NAME[TestItemId.IS_VALID_CURRENT];
                TestItem item = getTestItemByName(itemName);
                item.setResult(TestResult.PASS);
                mView.getDiagnosticApp().getDiagnoseModel()
                        .updateTestItem(item);

            }

            mView.finishTestCase();
            break;
        }
    }

    private String getHealthState(int health) {
        switch (health) {
        case BatteryManager.BATTERY_HEALTH_GOOD:
            return "Good";

        case BatteryManager.BATTERY_HEALTH_OVERHEAT:
            return "Overheat";

        case BatteryManager.BATTERY_HEALTH_DEAD:
            return "Dead";

        case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
            return "OverVoltage";
        }
        return "Unknown";
    }

    private int getHealthStateResString(int health) {

        switch (health) {
        case BatteryManager.BATTERY_HEALTH_GOOD:
            return R.string.battry_health_good;

        case BatteryManager.BATTERY_HEALTH_OVERHEAT:
            return R.string.battry_health_over_heat;

        case BatteryManager.BATTERY_HEALTH_DEAD:
            return R.string.battry_health_dead;

        case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
            return R.string.battry_health_over_voltage;
        }
        return R.string.battry_health_unknown;
    }

    private int getStatusResString(int status) {

        switch (status) {
        case BatteryManager.BATTERY_STATUS_CHARGING:
            return R.string.battery_status_charging;

        case BatteryManager.BATTERY_STATUS_DISCHARGING:
            return R.string.battery_status_discharging;

        case BatteryManager.BATTERY_STATUS_FULL:
            return R.string.battery_status_full;

        case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
            return R.string.battery_status_not_charging;
        }
        return R.string.battery_status_unknown;
    }

    private int getPluggedStateResString(int plugged) {

        switch (plugged) {
        case BatteryManager.BATTERY_PLUGGED_AC:
            return R.string.battery_plugged_ac;

        case BatteryManager.BATTERY_PLUGGED_USB:
            return R.string.battery_plugged_usb;
        }
        return R.string.battery_plugged_noplugged;
    }

    private int mStatus;
    private int mPlugged;

    /**
     * handle the battery information from the BroadcastReceiver
     * @param context
     * @param intent
     */
    public void onBatteryStatusChanged( Context context, Intent intent ) {

        Log.i(TAG, "intent=" + intent.getAction());

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

        mStatus = status;
        mPlugged = plugged;
        int statusStringResId = getStatusResString(mStatus);
        int pluggedStringResId = getPluggedStateResString(mPlugged);
        mView.setStatusValue(statusStringResId);
        mView.setPluggedState(pluggedStringResId);

        if (mStatus == BatteryManager.BATTERY_STATUS_DISCHARGING) {

            mView.setWarningPrompt(R.string.battery_sign);
            mView.setWarningPromptVisibility(View.VISIBLE);

        } else {
            mView.setWarningPromptVisibility(View.INVISIBLE);
        }

        TestResult healthResult = isValidHealth(intent);
        TestResult currentResult = isValidCurrent();
        TestResult levelResult = isValidLevel(intent);
        TestResult voltageResult = isValidVoltage(intent);
        TestResult temperatureResult = isValidTemperature(intent);

        if (healthResult.isPass() && currentResult.isPass() &&
                levelResult.isPass() && voltageResult.isPass() &&
                temperatureResult.isPass()) {
            mView.finishTestCase();
        }
    }


    @Override
    public void onCountDownFinish() {
        mView.setTimePrompt(0);

        if (mStatus == BatteryManager.BATTERY_STATUS_NOT_CHARGING ||
                mStatus == BatteryManager.BATTERY_STATUS_DISCHARGING) {

            mView.setWarningPrompt(R.string.battery_sign);
            mView.setWarningPromptVisibility(View.VISIBLE);
        } else {
            mView.setWarningPromptVisibility(View.INVISIBLE);
        }
        mView.setSubmitButtonVisibility(View.VISIBLE);
    }

    @Override
    public void onCountDownTick(long millisUntilFinished) {
        float time = millisUntilFinished / 1000;
        mView.setTimePrompt(time);
    }
}
