package com.msi.diagnostic.app;

import java.io.IOException;

import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DeviceConfig;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.data.ThresholdVerifiedInfo;
import com.msi.diagnostic.ui.SpecialBatteryTestCaseView;
import com.msi.diagnostic.utils.CountDownWaitingTimer;
import com.msi.diagnostic.utils.ICountDownListener;
import com.msi.diagnostic.utils.Utils;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

public class SpecialBatteryPagePresenter extends AbstractTestCasePresenter
        implements ICountDownListener {

    private static final String TAG = "BatteryPagePresenter";

    private static final int COUNT_DOWN_WAITING_TIME = 20000; // 20 second
    private static final int COUNT_DOWN_TIME_INTERVAL = 1000; // 1 second

    private static class TestItemId {
        public static final int IS_VALID_CURRENT = 0;
    }

    private static final String[] TEST_ITEM_NAME = {
            "isValidCurrent",
    };

    private SpecialBatteryTestCaseView mView;
    private CountDownWaitingTimer mCountDownTimer;

    public IntentFilter mFilter;

    public SpecialBatteryPagePresenter(SpecialBatteryTestCaseView view) {
        super(view);
        mView = view;

        mFilter = new IntentFilter();
        mFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        mFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);

        mCountDownTimer = new CountDownWaitingTimer(COUNT_DOWN_WAITING_TIME,
                COUNT_DOWN_TIME_INTERVAL, this);

        mView.setTimePromptColor(Color.rgb(255, 20, 20));
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

    private void updateCurrentResult(TestResult result) {
        String isPass = result.getResultAsString();
        mView.setCurrentTestResult(isPass);
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
                IDiagnosticApp app = (IDiagnosticApp) mView.getDiagnosticApp();
                DeviceConfig config = app.getDeviceConfig();
                if (config.getDeviceName().contentEquals(DeviceConfig.SPECIAL)
                        || config.getDeviceName().contentEquals(DeviceConfig.SPECIAL_N915)
                        || config.getDeviceName().contentEquals(DeviceConfig.SPECIAL_VIT_T4100)
                        || config.getDeviceName().contentEquals(DeviceConfig.SPECIAL_N71J)
                        || config.getDeviceName().contentEquals(DeviceConfig.SPECIAL_N821)
                        || config.getDeviceName().contentEquals(DeviceConfig.SPECIAL_N71L)
                        || config.getDeviceName().contentEquals(DeviceConfig.SPECIAL_N823)
                        || config.getDeviceName().contentEquals(DeviceConfig.SPECIAL_POLARIS_KURIO7)) {
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

    @Override
    public void OnButtonClick(int buttonId) {

        switch (buttonId) {
        case R.id.submit:
            IDiagnosticApp app = (IDiagnosticApp) mView.getDiagnosticApp();
            DeviceConfig config = app.getDeviceConfig();
            if (config.getDeviceName().contentEquals(DeviceConfig.SPECIAL)){
                mView.startActivity("com.softwinner.dragonfire", ".Main");
            }
            mView.finishTestCase();
            break;
        }
    }

    /**
     * handle the battery information from the BroadcastReceiver
     * @param context
     * @param intent
     */
    public void onBatteryStatusChanged( Context context, Intent intent ) {

        Log.i(TAG, "intent=" + intent.getAction());

        TestResult currentResult = isValidCurrent();

        if (currentResult.isPass()) {
            IDiagnosticApp app = (IDiagnosticApp) mView.getDiagnosticApp();
            DeviceConfig config = app.getDeviceConfig();
            if (config.getDeviceName().contentEquals(DeviceConfig.SPECIAL)){
                mView.startActivity("com.softwinner.dragonfire", ".Main");
            }
            mView.finishTestCase();
        }
    }

    @Override
    public void onCountDownFinish() {
        mView.setTimePrompt(0);
        mView.setSubmitButtonVisibility(View.VISIBLE);
    }

    @Override
    public void onCountDownTick(long millisUntilFinished) {
        float time = millisUntilFinished / 1000;
        mView.setTimePrompt(time);
    }
}
