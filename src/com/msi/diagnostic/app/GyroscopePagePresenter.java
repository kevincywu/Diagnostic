package com.msi.diagnostic.app;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.data.ThresholdVerifiedInfo;
import com.msi.diagnostic.ui.GyroscopeTestCaseView;
import com.msi.diagnostic.utils.CountDownWaitingTimer;
import com.msi.diagnostic.utils.ICountDownListener;
import com.msi.diagnostic.utils.Utils;

import android.util.Log;

public class GyroscopePagePresenter extends AbstractTestCasePresenter implements
        ICountDownListener, ISensorListener {
    private static final String TAG = "GyroscopePagePresenter";

    private static final boolean LOCAL_LOG = false;

    private static final float F_INVALID = -1.0f;

    private static final boolean F_PCBA_INVALID = false;

    private GyroscopeTestCaseView mView;

    private DiagnosticSensorManager mSensorManager;

    private CountDownWaitingTimer mWait;

    private DetectionVerifiedInfo mGyroscopeVerifiedInfo;

    private ThresholdVerifiedInfo mXVerifiedInfo, mYVerifiedInfo,
            mZVerifiedInfo;

    private TestItem mGyroscopeItem, mXItem, mYItem, mZItem;

    private TestResult mGyroscopeResult, mXResult, mYResult, mZResult;

    private float mValueX, mValueY, mValueZ;

    private boolean mTestDone;

    private static class TestItemId {
        public static final int IS_GYROSCOPE_EXISTED = 0;

        public static final int IS_XVECTOR_EXCEEDED = 1;

        public static final int IS_YVECTOR_EXCEEDED = 2;

        public static final int IS_ZVECTOR_EXCEEDED = 3;
    }

    private static final String[] TEST_ITEM_NAME = {
            "isGyroscopeExisted", "isXVectorExceeded", "isYVectorExceeded",
            "isZVectorExceeded"
    };

    public GyroscopePagePresenter(GyroscopeTestCaseView view) {
        super(view);
        mView = view;

        mTestDone = false;
    }

    private void initGyrocopeItem() {
        String itemGyroscopeName = TEST_ITEM_NAME[TestItemId.IS_GYROSCOPE_EXISTED];
        mGyroscopeItem = getTestItemByName(itemGyroscopeName);
        mGyroscopeVerifiedInfo = new DetectionVerifiedInfo(F_PCBA_INVALID);
        mGyroscopeResult = mGyroscopeItem.verify(mGyroscopeVerifiedInfo);
    }

    private void handlePCBA() {
        String infoFileName = mGyroscopeItem.getInfoFileName();
        try {
            String info = Utils.readContentFromFile(infoFileName);
            if (info.equals(mView.getGyroscopeSensorName())) {
                mGyroscopeVerifiedInfo.mInfo = true;
            }
        } catch (FileNotFoundException fileNotFind) {
            fileNotFind.printStackTrace();
            if (LOCAL_LOG)
                Log.d(TAG, "gyroscope_sensor doesn't existed!!");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mGyroscopeResult = mGyroscopeItem.verify(mGyroscopeVerifiedInfo);
        mWait.cancel();
        mView.finishTestCase();
    }

    private void initXItem() {
        String itemNameX = TEST_ITEM_NAME[TestItemId.IS_XVECTOR_EXCEEDED];
        mXItem = getTestItemByName(itemNameX);
        mXVerifiedInfo = new ThresholdVerifiedInfo(F_INVALID);
        mXResult = mXItem.verify(mXVerifiedInfo);
    }

    private void initYItem() {
        String itemNameY = TEST_ITEM_NAME[TestItemId.IS_YVECTOR_EXCEEDED];
        mYItem = getTestItemByName(itemNameY);
        mYVerifiedInfo = new ThresholdVerifiedInfo(F_INVALID);
        mYResult = mYItem.verify(mYVerifiedInfo);
    }

    private void initZItem() {
        String itemNameZ = TEST_ITEM_NAME[TestItemId.IS_ZVECTOR_EXCEEDED];
        mZItem = getTestItemByName(itemNameZ);
        mZVerifiedInfo = new ThresholdVerifiedInfo(F_INVALID);
        mZResult = mZItem.verify(mZVerifiedInfo);

    }

    private void getSensorManager() {
        mSensorManager = super.mApp.getSensorManager();
        mSensorManager.registerSensor(this, SensorType.TYPE_GYROSOPE_SENSOR);
    }

    private void handleASSY() {

        if (mXResult.getResult() != TestResult.PASS) {
            mXVerifiedInfo.mInfo = mValueX;
            mXResult = mXItem.verify(mXVerifiedInfo);
        }
        if (mYResult.getResult() != TestResult.PASS) {
            mYVerifiedInfo.mInfo = mValueY;
            mYResult = mYItem.verify(mYVerifiedInfo);
        }
        if (mZResult.getResult() != TestResult.PASS) {
            mZVerifiedInfo.mInfo = mValueZ;
            mZResult = mZItem.verify(mZVerifiedInfo);
        }

        if (mXResult.getResult() == TestResult.PASS
                && mYResult.getResult() == TestResult.PASS
                && mZResult.getResult() == TestResult.PASS) {
            mTestDone = true;
            mWait.cancel();
            mView.finishTestCase();
        }

    }

    @Override
    public void OnButtonClick(int buttonId) {
        // unused operation
    }

    @Override
    public void resume() {
        if (LOCAL_LOG)
            Log.d(TAG, "resume");
        super.resume();

        mWait = new CountDownWaitingTimer(30000, 1000, this);
        mWait.start();

        if (TestLevel.TYPES[TestLevel.TYPE_PCBA].equals(this.mTestLevelName)) {
            initGyrocopeItem();
            handlePCBA();
        } else if (TestLevel.TYPES[TestLevel.TYPE_ASSY]
                .equals(this.mTestLevelName)) {
            initXItem();
            initYItem();
            initZItem();
            getSensorManager();

        }
    }

    @Override
    public void pause() {
        if (LOCAL_LOG)
            Log.d(TAG, "pause");
        super.pause();

        if (TestLevel.TYPES[TestLevel.TYPE_ASSY].equals(this.mTestLevelName))
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

        if (!mTestDone)
            handleASSY();

    }

}
