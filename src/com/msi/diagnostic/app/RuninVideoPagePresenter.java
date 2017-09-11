package com.msi.diagnostic.app;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.ui.RuninVideoTestCaseView;
import com.msi.diagnostic.utils.CountDownWaitingTimer;
import com.msi.diagnostic.utils.ICountDownListener;

public class RuninVideoPagePresenter extends AbstractTestCasePresenter
        implements ICountDownListener {

    private static final String TAG = "RuninVideo";
    private static final boolean LOCAL_LOGD = true;
    private RuninVideoTestCaseView mPanelView;
    private Context mContext;
    private RuninWifiAdmin mWifiAdmin;

    private static final int COUNT_DOWN_WAITING_TIME = 120; // 120 minutes
    private static final int COUNT_DOWN_TIME_INTERVAL = 1000; // 1 second

    private static class TestItemId {
        public static final int IS_VIDEO_FINISH = 0;
    }

    private static final String[] TEST_ITEM_NAME = {
        "isVideoFinish"
    };

    private CountDownWaitingTimer mCountDownTimer;

    public RuninVideoPagePresenter(RuninVideoTestCaseView view) {
        super(view);
        mPanelView = view;
        mContext = view.getAppContext();
        mWifiAdmin = new RuninWifiAdmin(mContext);
    }

    /**
     * Method for play video by path
     * 
     * @author jack
     */
    private void videoStart() {

        try {
            mPanelView.setmMediaController();
            mPanelView.getmVideoView().setMediaController(
                    mPanelView.getmMediaController());
            mPanelView.getmVideoView().setVideoPath(
                    "android.resource://com.msi.diagnostic/" + R.raw.test);
            mPanelView.videoViewRequestFocus();
            Log.v(TAG, "play video!");
            mPanelView.videoViewStart();

            mPanelView.getmVideoView().setOnCompletionListener(
                    new OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer arg0) {
                            if (LOCAL_LOGD) {
                                Log.v(TAG, "play video again!");
                            }
                            mPanelView.getmVideoView().destroyDrawingCache();
                            mPanelView.setmMediaController();
                            mPanelView.getmVideoView().setMediaController(
                                    mPanelView.getmMediaController());
                            mPanelView.getmVideoView().setVideoPath(
                                    "android.resource://com.msi.diagnostic/"
                                            + R.raw.test);
                            mPanelView.videoViewRequestFocus();
                            mPanelView.videoViewStart();
                        }
                    });
        } catch (Exception e) {
            if (LOCAL_LOGD) {
                Log.e(TAG, e.toString());
            }
            mPanelView.finishTestCase();
        }
    }

    @Override
    public void OnButtonClick(int buttonId) {

    }

    @Override
    public void resume() {

        if (LOCAL_LOGD) {
            Log.v(TAG, "start video");
        }
        mWifiAdmin.closeWifi();
        videoStart();
        mCountDownTimer = new CountDownWaitingTimer(
                COUNT_DOWN_WAITING_TIME * 60000, COUNT_DOWN_TIME_INTERVAL, this);
        mCountDownTimer.start();
        mCountDownTimer.onTick(COUNT_DOWN_WAITING_TIME * 60000);
        super.resume();
    }

    @Override
    public void pause() {
        mCountDownTimer.cancel();
        mWifiAdmin.openWifi();
        super.pause();
    }

    @Override
    public void onCountDownFinish() {
        String itemName = TEST_ITEM_NAME[TestItemId.IS_VIDEO_FINISH];
        TestItem item = getTestItemByName(itemName);
        TestResult result = item.verify(new DetectionVerifiedInfo(true));
        mCountDownTimer.cancel();
        mWifiAdmin.openWifi();
        mPanelView.finishTestCase();
    }

    @Override
    public void onCountDownTick(long millisUntilFinished) {
        int hour = (int) (millisUntilFinished / 3600000);
        int minutes = (int) ((millisUntilFinished % 3600000) / 60000);
        int seconds = (int) (((millisUntilFinished % 3600000) % 60000) / 1000);
        String str = "remaining " + hour + " : " + minutes + " : " + seconds;
        mPanelView.setmShowTimeViewText(str);
    }

}
