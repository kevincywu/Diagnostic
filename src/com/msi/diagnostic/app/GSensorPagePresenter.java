package com.msi.diagnostic.app;

import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.data.ThresholdVerifiedInfo;
import com.msi.diagnostic.ui.GSensorTestCaseView;
import com.msi.diagnostic.utils.CountDownWaitingTimer;
import com.msi.diagnostic.utils.ICountDownListener;

import android.util.Log;

public class GSensorPagePresenter extends AbstractTestCasePresenter implements ICountDownListener,
        ISensorListener {

    private static final String TAG = "GSensorPagePresenter";

    private static final boolean LOCAL_LOG = false;

    private static final float F_INVALID = -1.0f;

    private GSensorTestCaseView mView;

    private DiagnosticSensorManager mSensorManager;

    private CountDownWaitingTimer mWait;

    private ThresholdVerifiedInfo mXPosVerifiedInfo, mXNegVerifiedInfo, mYPosVerifiedInfo,
            mYNegVerifiedInfo;

    private TestItem mXPosItem, mXNegItem, mYPosItem, mYNegItem;

    private TestResult mXPosResult, mXNegResult, mYPosResult, mYNegResult;

    private float mMaxValue, mValueX, mValueY, mValueZ;

    private boolean mTestDone;

    private static class TestItemId {

        public static final int IS_XPOSEXCEEDED = 0;

        public static final int IS_XNEGEXCEEDED = 1;

        public static final int IS_YPOSEXCEEDED = 2;

        public static final int IS_YNEGEXCEEDED = 3;
    }

    private static final String[] TEST_ITEM_NAME = {
            "isXPosVectorExceeded", "isXNegVectorExceeded", "isYPosVectorExceeded",
            "isYNegVectorExceeded"
    };

    public GSensorPagePresenter(GSensorTestCaseView view) {
        super(view);
        mView = view;

        mTestDone = false;
    }

    private void initXPosItem() {
        String itemNameXPos = TEST_ITEM_NAME[TestItemId.IS_XPOSEXCEEDED];
        mXPosItem = getTestItemByName(itemNameXPos);
        mXPosVerifiedInfo = new ThresholdVerifiedInfo(F_INVALID);
        mXPosResult = mXPosItem.verify(mXPosVerifiedInfo);
    }

    private void initXNegItem() {
        String itemNameXNeg = TEST_ITEM_NAME[TestItemId.IS_XNEGEXCEEDED];
        mXNegItem = getTestItemByName(itemNameXNeg);
        mXNegVerifiedInfo = new ThresholdVerifiedInfo(F_INVALID);
        mXNegResult = mXNegItem.verify(mXNegVerifiedInfo);
    }

    private void initYPosItem() {
        String itemNameYPos = TEST_ITEM_NAME[TestItemId.IS_YPOSEXCEEDED];
        mYPosItem = getTestItemByName(itemNameYPos);
        mYPosVerifiedInfo = new ThresholdVerifiedInfo(F_INVALID);
        mYPosResult = mYPosItem.verify(mYPosVerifiedInfo);
    }

    private void initYNegItem() {
        String itemNameYNeg = TEST_ITEM_NAME[TestItemId.IS_YNEGEXCEEDED];
        mYNegItem = getTestItemByName(itemNameYNeg);
        mYNegVerifiedInfo = new ThresholdVerifiedInfo(F_INVALID);
        mYNegResult = mYNegItem.verify(mYNegVerifiedInfo);
    }

    private void getSensorManager() {
        mSensorManager = super.mApp.getSensorManager();
        mSensorManager.registerSensor(this, SensorType.TYPE_G_SENSOR);
    }

    @Override
    public void OnButtonClick(int buttonId) {
        throw new UnsupportedOperationException();
    }

    private void handlePCBA() {

        if (mXPosResult.getResult() == TestResult.PASS) {
            mTestDone = true;
            mWait.cancel();
            mView.finishTestCase();

        } else {
            mXPosVerifiedInfo.mInfo = mMaxValue;
            mXPosResult = mXPosItem.verify(mXPosVerifiedInfo);

            mXNegVerifiedInfo.mInfo = -mMaxValue;
            mXNegResult = mXNegItem.verify(mXNegVerifiedInfo);

            mYPosVerifiedInfo.mInfo = mMaxValue;
            mYPosResult = mYPosItem.verify(mYPosVerifiedInfo);

            mYNegVerifiedInfo.mInfo = -mMaxValue;
            mYNegResult = mYNegItem.verify(mYNegVerifiedInfo);
        }
    }

    private void handleASSY() {
        if (mXPosResult.getResult() != TestResult.PASS) {
            mXPosVerifiedInfo.mInfo = mValueX;
            mXPosResult = mXPosItem.verify(mXPosVerifiedInfo);
        }
        if (mXNegResult.getResult() != TestResult.PASS) {
            mXNegVerifiedInfo.mInfo = mValueX;
            mXNegResult = mXNegItem.verify(mXNegVerifiedInfo);
        }
        if (mYPosResult.getResult() != TestResult.PASS) {
            mYPosVerifiedInfo.mInfo = mValueY;
            mYPosResult = mYPosItem.verify(mYPosVerifiedInfo);
        }
        if (mYNegResult.getResult() != TestResult.PASS) {
            mYNegVerifiedInfo.mInfo = mValueY;
            mYNegResult = mYNegItem.verify(mYNegVerifiedInfo);
        }

        if (mXPosResult.getResult() == TestResult.PASS
                && mXNegResult.getResult() == TestResult.PASS
                && mYPosResult.getResult() == TestResult.PASS
                && mYNegResult.getResult() == TestResult.PASS) {
            mTestDone = true;
            mWait.cancel();
            mView.finishTestCase();
        }
    }

    @Override
    public void resume() {
        if (LOCAL_LOG)
            Log.d(TAG, "resume");
        super.resume();

        mWait = new CountDownWaitingTimer(30000, 1000, this);
        mWait.start();

        initXPosItem();
        initXNegItem();
        initYPosItem();
        initYNegItem();
        getSensorManager();

    }

    @Override
    public void pause() {
        if (LOCAL_LOG)
            Log.d(TAG, "pause");
        super.pause();
        mSensorManager.unregisterSensor();
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
        mValueX = sensorEventInfo.SENSOR_EVENT_VALUE[0];
        mValueY = sensorEventInfo.SENSOR_EVENT_VALUE[1];
        mValueZ = sensorEventInfo.SENSOR_EVENT_VALUE[2];

        mView.setTextX(mValueX);
        mView.setTextY(mValueY);
        mView.setTextZ(mValueZ);

        mMaxValue = Math.max(Math.abs(mValueZ), Math.max(Math.abs(mValueX), Math.abs(mValueY)));
        if (!mTestDone) {
            if (TestLevel.TYPES[TestLevel.TYPE_PCBA].equals(this.mTestLevelName))
                handlePCBA();
            else if (TestLevel.TYPES[TestLevel.TYPE_ASSY].equals(this.mTestLevelName))
                handleASSY();
        }
    }

}
