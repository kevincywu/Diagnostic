package com.msi.diagnostic.app;

import com.msi.diagnostic.R;

import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.ui.CompassTestCaseView;
import com.msi.diagnostic.utils.CountDownWaitingTimer;
import com.msi.diagnostic.utils.ICountDownListener;

import android.util.Log;

public class CompassPagePresenter extends AbstractTestCasePresenter implements ICountDownListener,
        ISensorListener {

    private static final String TAG = "CompassPagePresenter";

    private static final boolean LOCAL_LOG = false;

    private static final boolean F_INVALID = false;

    private CompassTestCaseView mView;

    private DiagnosticSensorManager mSensorManager;

    private CountDownWaitingTimer mWait;

    private DetectionVerifiedInfo mCompassVerifiedInfo;

    private TestItem mItem;

    private TestResult mResult;

    boolean mValueChange;

    private static class TestItemId {
        public static final int IS_DIRECTTONORTH = 0;
    }

    private static final String[] TEST_ITEM_NAME = {
        "isDirectToNorth"
    };

    public CompassPagePresenter(CompassTestCaseView view) {
        super(view);
        mView = view;

    }

    private void initItem() {
        String itemName = TEST_ITEM_NAME[TestItemId.IS_DIRECTTONORTH];
        mItem = getTestItemByName(itemName);
        mCompassVerifiedInfo = new DetectionVerifiedInfo(F_INVALID);
        mResult = TestResult.RESULT_NONE;

        mWait = new CountDownWaitingTimer(2000, 1000, this);
        mWait.start();
    }

    private void getSensorManager() {
        mSensorManager = super.mApp.getSensorManager();
        mSensorManager.registerSensor(this, SensorType.TYPE_COMPASS_SENSOR);
    }

    @Override
    public void OnButtonClick(int buttonId) {
        switch (buttonId) {
            case R.id.pass:
                mCompassVerifiedInfo.mInfo = true;
                break;

            case R.id.fail:
                mCompassVerifiedInfo.mInfo = false;
                break;
        }
        submit();
    }

    private void submit() {
        mResult = mItem.verify(mCompassVerifiedInfo);
        mWait.cancel();
        mView.finishTestCase();
    }

    @Override
    public void resume() {
        if (LOCAL_LOG)
            Log.d(TAG, "resume");
        super.resume();

        initItem();
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
        if (mValueChange)
            mView.setNotCompassInfo();

    }

    @Override
    public void onCountDownTick(long millisUntilFinished) {
        // ignore the operation when time countdowntick
    }

    @Override
    public void onSensorChanged(SensorEventInfo sensorEventInfo) {
        mWait.cancel();
        mView.setCompassInfo();
        mView.updateCompassDirection(sensorEventInfo);
    }

}
