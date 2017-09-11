package com.msi.diagnostic.app;

import android.util.Log;

import com.msi.diagnostic.R;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.data.ThresholdVerifiedInfo;
import com.msi.diagnostic.ui.LightSensorTestCaseView;
import com.msi.diagnostic.utils.CountDownWaitingTimer;
import com.msi.diagnostic.utils.ICountDownListener;

public class LightSensorPagePresenter extends AbstractTestCasePresenter implements
        ICountDownListener, ISensorListener {
    private static final String TAG = "LightSensorPagePresenter";

    private static final boolean LOCAL_LOG = false;

    private static final float F_INVALID = -1.0f;

    private CountDownWaitingTimer mWait;

    private LightSensorTestCaseView mView;

    private DiagnosticSensorManager mSensorManager;

    private ThresholdVerifiedInfo mDarkVerifiedInfo;

    private ThresholdVerifiedInfo mLightVerifiedInfo;

    private ThresholdVerifiedInfo mLuxVerifiedInfo;

    private TestItem mDarkTestItem, mLightTestItem, mLuxItem;

    private TestResult mDarkItemResult, mLightItemResult, mLuxResult;

    private boolean mTestDone;

    private float value;

    private static class TestItemId {
        public static final int IS_LUX_DARK_EXCEEDED = 0;

        public static final int IS_LUX_LIGHT_EXCEEDED = 1;

        public static final int IS_LUX__EXISTED = 2;
    }

    private static final String[] TEST_ITEM_NAME = {
            "isLuxDarkExceed", "isLuxLightExceed", "isLuxExisted"
    };

    public LightSensorPagePresenter(LightSensorTestCaseView view) {
        super(view);
        mView = view;

        mTestDone = false;
    }

    private void initDarkTestItem() {
        mDarkVerifiedInfo = new ThresholdVerifiedInfo(F_INVALID);
        String itemName = TEST_ITEM_NAME[TestItemId.IS_LUX_DARK_EXCEEDED];
        mDarkTestItem = getTestItemByName(itemName);
        mDarkItemResult = mDarkTestItem.verify(mDarkVerifiedInfo);
    }

    private void initLightTestItem() {
        mLightVerifiedInfo = new ThresholdVerifiedInfo(F_INVALID);
        String itemName = TEST_ITEM_NAME[TestItemId.IS_LUX_LIGHT_EXCEEDED];
        mLightTestItem = getTestItemByName(itemName);
        mLightItemResult = mLightTestItem.verify(mLightVerifiedInfo);
    }

    private void initLuxtTestItem() {
        mLuxVerifiedInfo = new ThresholdVerifiedInfo(F_INVALID);
        String itemName = TEST_ITEM_NAME[TestItemId.IS_LUX__EXISTED];
        mLuxItem = getTestItemByName(itemName);
        mLuxResult = mLuxItem.verify(mLuxVerifiedInfo);

        mWait = new CountDownWaitingTimer(10000, 1000, this);
        mWait.start();
    }

    private void getSensorManager() {
        mSensorManager = super.mApp.getSensorManager();
        mSensorManager.registerSensor(this, SensorType.TYPE_LIGHT_SENSOR);
    }

    @Override
    public void OnButtonClick(int buttonId) {

        switch (buttonId) {
            case R.id.submit:
                mView.finishTestCase();
                break;

            default:
                break;
        }
    }

    @Override
    public void resume() {
        if (LOCAL_LOG)
            Log.d(TAG, "resume");
        super.resume();

        if (TestLevel.TYPES[TestLevel.TYPE_PCBA].equals(this.mTestLevelName)) {
            initLuxtTestItem();
            mView.setButton(false);
        }

        else if (TestLevel.TYPES[TestLevel.TYPE_ASSY].equals(this.mTestLevelName)) {
            initDarkTestItem();
            initLightTestItem();
        }
        getSensorManager();

    }

    @Override
    public void pause() {
        if (LOCAL_LOG)
            Log.d(TAG, "pause");
        super.pause();
        mSensorManager.unregisterSensor();
    }

    private void handlePCBA() {
        if (mLuxItem.getResult() != TestResult.PASS) {
            mLuxVerifiedInfo.mInfo = value;
            mLuxResult = mLuxItem.verify(mLuxVerifiedInfo);

        } else {
            mTestDone = true;
            mWait.cancel();
            mView.finishTestCase();
        }
    }

    private void handleASSY() {
        if (mDarkTestItem.getResult() != TestResult.PASS) {
            mDarkVerifiedInfo.mInfo = value;
            mDarkItemResult = mDarkTestItem.verify(mDarkVerifiedInfo);

        } else {
            String result = mDarkItemResult.getResultAsString();
            mView.setDarkTestResult(result);
        }

        if (mLightTestItem.getResult() != TestResult.PASS) {
            mLightVerifiedInfo.mInfo = value;
            mLightItemResult = mLightTestItem.verify(mLightVerifiedInfo);

        } else {
            String result = mLightItemResult.getResultAsString();
            mView.setLightTestResult(result);
        }
    }

    @Override
    public void onCountDownFinish() {
        mWait.cancel();
        mView.finishTestCase();
    }

    @Override
    public void onCountDownTick(long millisUntilFinished) {
        // ignore the operation when time countdowntick
    }

    @Override
    public void onSensorChanged(SensorEventInfo sensorEventInfo) {
        value = sensorEventInfo.SENSOR_EVENT_VALUE[0];
        mView.setCurrentValue(value);

        if (!mTestDone) {
            if (TestLevel.TYPES[TestLevel.TYPE_PCBA].equals(this.mTestLevelName))
                handlePCBA();
            else if (TestLevel.TYPES[TestLevel.TYPE_ASSY].equals(this.mTestLevelName))
                handleASSY();
        }
    }

}
