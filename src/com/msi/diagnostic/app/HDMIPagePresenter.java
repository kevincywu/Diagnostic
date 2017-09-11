package com.msi.diagnostic.app;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.ui.HDMITestCaseView;

public class HDMIPagePresenter extends AbstractTestCasePresenter {

    private static final String TAG = "HDMIPagePresenter";
    private static final boolean LOCAL_LOG = true;

    private HDMITestCaseView mView;
    private Context mContext;
    private MediaPlayer mMediaPlayer = null;
    private AudioManager mAudioManager;
    private int mMaxVolume;
    private int mCurrentVolume;
    private boolean mHasPlay;
    private static class TestItemId {
        public static final int VIDIEO = 0;
        public static final int VOICE  = 1;
    }
    private static final String[] TEST_ITEM_NAME = {
        "isVideoPlayed",
        "isVoicePlayed"
    };

    public HDMIPagePresenter(HDMITestCaseView view) {
        super(view);
        mView = view;
        mContext = mView.getDiagnosticApp().getAppContext();
        init();
    }

    private void init() {
        // Set the path of music files
        mMediaPlayer = MediaPlayer.create(mContext, R.raw.song);
        // Get volume value
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mCurrentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        // Set the sound to the maximum
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mMaxVolume, 0);
        mHasPlay = false;
    }

    /**
     * Terminate playback and release resources.
      *
     * @author jack
     */
    private void releaseMediaPlayer()
    {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void OnButtonClick(int buttonId) {
        // Receives the log file path
        switch (buttonId) {
            case R.id.play:
                mHasPlay = true;
                playVideo();
                break;

            case R.id.stop:
                stopVideo();
                break;

            case R.id.pass:
                if (mHasPlay) {
                    verifyItemResult(TestItemId.VIDIEO, true);
                    verifyItemResult(TestItemId.VOICE, true);
                }
                mView.finishTestCase();
                break;

            case R.id.vfail:
                if (mHasPlay) {
                    verifyItemResult(TestItemId.VIDIEO, false);
                }
                mView.finishTestCase();
                break;

            case R.id.pfail:
                if (mHasPlay) {
                    verifyItemResult(TestItemId.VOICE, false);
                }
                mView.finishTestCase();
                break;
            default:
                break;
        }
    }

    private void playVideo() {
        try {
            if (mMediaPlayer == null) {
                mMediaPlayer = MediaPlayer.create(mContext, R.raw.song);
            }
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
            mView.setTextView(R.string.hdmi_play);
        } catch (Exception e) {
            releaseMediaPlayer();
            init();
            e.printStackTrace();
        }
    }

    private void stopVideo() {
        if (mMediaPlayer.isPlaying()) {
            try {
                mMediaPlayer.pause();
                mView.setTextView(R.string.hdmi_stop);
            } catch (Exception e) {
                releaseMediaPlayer();
                init();
                e.printStackTrace();
            }
        }
    }

    private void verifyItemResult(int testItemId, boolean result) {
        releaseMediaPlayer();
        // Restore the initial volume
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mCurrentVolume, 0);

        String itemName = TEST_ITEM_NAME[testItemId];
        TestItem testItem = getTestItemByName(itemName);
        testItem.verify(new DetectionVerifiedInfo(result));
    }

    @Override
    public void resume() {
        if (LOCAL_LOG)
            Log.d(TAG, "resume");
        super.resume();
    }

    @Override
    public void pause() {
        if (LOCAL_LOG)
            Log.d(TAG, "pause");
        releaseMediaPlayer();
        super.pause();
    }

}
