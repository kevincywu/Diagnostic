package com.msi.diagnostic.app;

import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;


import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.ui.HotKeyTestCaseView;

public final class HotKeyPagePresenter extends AbstractTestCasePresenter {
    private static final String TAG = "HotKeyPagePresenter";
    private static final boolean LOCAL_LOG = true;

    private final DetectionVerifiedInfo mDetectedInfo = new DetectionVerifiedInfo(true);

    private static class TestItemId {
        public static final int VOLUMN_UP   = 0;
        public static final int VOLUMN_DOWN = 1;
    }

    private static final String[] TEST_ITEM_NAME = {
        "isVolumeUpCatched",
        "isVolumeDownCatched"
    };

    private HotKeyTestCaseView mView;

    public HotKeyPagePresenter(HotKeyTestCaseView view) {
        super(view);
        mView = view;
    }

    private void initVolumnKeyResult() {
        mView.setVolumnUpResult(RESULT_NONE);
        mView.setVolumnDownResult(RESULT_NONE);
        mView.setVolumnUpResultTextColor(Color.rgb(255, 20, 20));
        mView.setVolumnDownResultTextColor(Color.rgb(255, 20, 20));
    }

    private TestResult volumeUpCatched() {
        mView.setVolumnUpResultTextColor(Color.rgb(20, 255, 20));
        String itemName = TEST_ITEM_NAME[TestItemId.VOLUMN_UP];
        TestItem item = getTestItemByName(itemName);
        TestResult result = item.verify(mDetectedInfo);
        return result;
    }

    private TestResult volumeDownCatched() {
        mView.setVolumnDownResultTextColor(Color.rgb(20, 255, 20));
        String itemName = TEST_ITEM_NAME[TestItemId.VOLUMN_DOWN];
        TestItem item = getTestItemByName(itemName);
        TestResult result = item.verify(mDetectedInfo);
        return result;
    }

    public void onVolumnUpKeyUp() {
        TestResult result = volumeUpCatched();
        mView.setVolumnUpResult(result.getResultAsString());
    }

    public void onVolumnDownKeyUp() {
        TestResult result = volumeDownCatched();
        mView.setVolumnDownResult(result.getResultAsString());
    }

    public boolean onKeyEventHandler(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_UP:
                    onVolumnUpKeyUp();
                    if (LOCAL_LOG)
                        Log.i("HotKeyP", "KEYCODE_VOLUME_UP");
                    return true;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    onVolumnDownKeyUp();
                    if (LOCAL_LOG)
                        Log.i("HotKeyP", "KEYCODE_VOLUME_DOWN");
                    return true;

                default:
                    return false;
            }
        }
        return false;
    }

    @Override
    public void OnButtonClick(int buttonId) {
        switch (buttonId) {
        case R.id.submit_button:
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
        initVolumnKeyResult();
    }

    @Override
    public void pause() {
        if (LOCAL_LOG)
            Log.d(TAG, "pause");
        super.pause();
    }

}
