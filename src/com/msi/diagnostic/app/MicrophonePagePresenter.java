package com.msi.diagnostic.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.data.ThresholdVerifiedInfo;
import com.msi.diagnostic.ui.MicrophoneTestCaseView;
import com.msi.diagnostic.utils.CountDownWaitingTimer;
import com.msi.diagnostic.utils.ICountDownListener;

public class MicrophonePagePresenter extends AbstractTestCasePresenter
        implements ICountDownListener {

    private static final String TAG = "MicrophonePagePresenter";
    private static final String RECORD_FILE_PATH = "RecordTest.wav";
    private static final int AUDIO_ENCODING_BIT_RATE = 64000;
    private static final int AUDIO_SAMPLING_RATE = 44100;
    private static final File EXTERNAL_DIR_PATH = Environment
            .getExternalStorageDirectory();
    private static final int RECORD_TEST_TIME = 4000; // 3s
    private static final int DOWN_COUNT_INTERVAL = 1000;

    private MicrophoneTestCaseView mView;
    private CountDownWaitingTimer mWait;

    private MediaRecorder mMediaRecorder;
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    private int mMaxVolume;
    private File mRecordFile;
    private String mRecordFilePath;

    private boolean mRecordStop = true;
    private boolean mIsHasRecordStart = false;
    private boolean mIsHasRecordStop = false;
    private boolean mIsHasPlay = false;

    private SoundAmplitude mSoundAmplitude;

    private static class TestItemId {
        public static final int MicrophoneRecorded = 0;
        public static final int IsInRange = 1;
    }

    private static final String[] TEST_ITEM_NAME = {
            "isMicrophoneRecorded", "isInRange"
    };

    public MicrophonePagePresenter(MicrophoneTestCaseView view,
            AudioManager audioManager) {

        super(view);
        mView = view;
        mAudioManager = audioManager;
        mRecordFile = new File(EXTERNAL_DIR_PATH, RECORD_FILE_PATH);
        mRecordFilePath = mRecordFile.getAbsolutePath();
        mMediaPlayer = new MediaPlayer();
        setVolumeToMax();
        mSoundAmplitude = new SoundAmplitude();
    }

    private void setVolumeToMax() {
        mMaxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        /** Set the sound to the maximum **/
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mMaxVolume, 0);

    }

    private TestResult isRecorded(boolean isRecorded) {
        String itemName = TEST_ITEM_NAME[TestItemId.MicrophoneRecorded];
        TestItem item = getTestItemByName(itemName);
        TestResult result = item.verify(new DetectionVerifiedInfo(isRecorded));
        return result;
    }

    private TestResult isInRange(float avgAmplitudeNum) {
        String itemName = TEST_ITEM_NAME[TestItemId.IsInRange];
        TestItem item = getTestItemByName(itemName);
        float db = 10 * (float) Math.log(avgAmplitudeNum);
        Log.d(TAG, "db------:" + db);
        TestResult result = item.verify(new ThresholdVerifiedInfo(db));
        return result;
    }

    private void audioPlay() {
        if (mRecordStop) {
            mView.setmMessageViewText(R.string.microphone_audio_play);
            resetPlayerAndPlay();
            mMediaPlayer
                    .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            resetPlayerAndPlay();
                        }
                    });
        } else {
            mView.setmMessageViewText(R.string.microphone_not_file);
        }
    }

    private void resetPlayerAndPlay() {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mRecordFilePath);
            mMediaPlayer.prepare();
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();

        } catch (IOException e) {
            Log.d(TAG, "exception in resetPlayerAndPlay()");
        }
    }

    private void recordStart() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }

        if (mRecordStop) {
            mView.setmMessageViewText(R.string.microphone_record_start);
            initAndStartRecordSound();
            mRecordStop = false;

        }
    }

    private void initAndStartRecordSound() {
        try {
            mMediaRecorder = new MediaRecorder();

            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mMediaRecorder.setAudioEncodingBitRate(AUDIO_ENCODING_BIT_RATE);
            mMediaRecorder.setAudioSamplingRate(AUDIO_SAMPLING_RATE);

            mMediaRecorder.setOutputFile(mRecordFilePath);
            mMediaRecorder.prepare();

            mMediaRecorder.start();

        } catch (IOException e) {
            isRecorded(false);
            Log.d(TAG, "exception in initAndStartRecordSound");
        }
    }

    private void initAndGetSoundAmplitude() {
        mSoundAmplitude.startGetAmplitude();
    }

    private void recordStop() {
        if (!mRecordStop) {
            mMediaRecorder.stop();
            mView.setmMessageViewText(R.string.microphone_record_stop);
            mMediaRecorder.reset();
            mRecordStop = true;
        }
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void OnButtonClick(int buttonId) {
        switch (buttonId) {
        case R.id.microphone_play:
            if (mRecordStop) {
                mIsHasPlay = true;
                audioPlay();

                if (!checkIsNoMic() && !checkSoundLow()) {

                    if (!isNotTest()) {
                        mView.setRecordPassBtnVisible(View.VISIBLE);
                    }
                }
            } else {
                mSoundAmplitude.resetAmplitudeData();
                mView.setmMessageViewText(R.string.microphone_not_file);
            }
            break;

        case R.id.microphone_start:
            mView.setRecordPassBtnVisible(View.INVISIBLE);
            mIsHasRecordStart = true;
            recordStart();
            initAndGetSoundAmplitude();
            mWait = new CountDownWaitingTimer(RECORD_TEST_TIME, DOWN_COUNT_INTERVAL, this);
            mWait.start();
            mWait.onTick(RECORD_TEST_TIME);
            break;

        case R.id.microphone_pass:
            if (!isNotTest()) {
                isRecorded(true);
            }

            stopMediaRecord();
            deleteRecordFile();
            stopPlayer();
            mView.finishTestCase();
            break;

        case R.id.microphone_fail:
            if (!isNotTest()) {
                isRecorded(false);
            }

            stopMediaRecord();
            deleteRecordFile();
            stopPlayer();
            mView.finishTestCase();
            break;
        }
    }

    private boolean checkSoundLow() {
        int avgSoundAmplitudeNum = mSoundAmplitude.getAvgAmplitudeNum();
        if (avgSoundAmplitudeNum <= SoundAmplitude.RECORD_VOLUME_PASS_CRITERIA) {
            mView.getMeggageViewText().setText(
                    R.string.microphone_volume_too_low);
            return true;
        }
        mView.getMeggageViewText()
                .setText(R.string.microphone_volume_is_normal);
        isInRange(avgSoundAmplitudeNum);
        return false;
    }

    private boolean checkIsNoMic() {
        return mSoundAmplitude.getIsNoMic();
    }

    public boolean isNotTest() {
        if (mIsHasRecordStart && mIsHasRecordStop && mIsHasPlay) {
            return false;
        }
        return true;
    }

    private void stopMediaRecord() {
        if (!mRecordStop) {
            mMediaRecorder.stop();
        }
    }

    private void deleteRecordFile() {
        if (mRecordFile.exists()) {
            mRecordFile.delete();
        }
    }

    private void stopPlayer() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void pause() {
        releaseMediaPlayer();
        super.pause();
    }

    private class SoundAmplitude implements Runnable {
        private static final int ZERO_NUM = 0;

        // set the time interval to obtain amplitude,it can't be 0s or the value
        // of the sound amplitude getting from MediaRecorder always be zero,
        // so set getting sound amplitude per 0.1s.
        private static final int INTERVAL_DELAY_TIME = 100;
        private static final int START_DELAY_TIME = 0;
        private static final int RECORD_VOLUME_PASS_CRITERIA = 1;
        private int mAvgAmplitudeNum;
        private boolean mIsNoMic = true;
        private List<Integer> mAmplitudeDataList = new ArrayList<Integer>();
        private Handler mHandler = new Handler();

        private void startGetAmplitude() {
            if (mAmplitudeDataList.isEmpty()) {
                mHandler.postDelayed(this, START_DELAY_TIME);
            }
        }

        private void stopGetAmplitude() {
            if (!mAmplitudeDataList.isEmpty()) {
                mHandler.removeCallbacks(this);
                mAvgAmplitudeNum = countAvgAmplituteNum();
                mIsNoMic = checkIsDataListAllZero();
                mAmplitudeDataList.clear();
            }
        }

        public void resetAmplitudeData() {
            if (!mAmplitudeDataList.isEmpty()) {
                mHandler.removeCallbacks(this);
                mAmplitudeDataList.clear();
            }
        }

        private int countAvgAmplituteNum() {
            int result = 0;
            for (int i : mAmplitudeDataList) {
                result = result + i;
            }
            result = result / mAmplitudeDataList.size();

            return result;
        }

        private boolean checkIsDataListAllZero() {
            Iterator<Integer> it = mAmplitudeDataList.iterator();
            while (it.hasNext()) {
                if (!it.next().equals(ZERO_NUM)) {
                    return false;
                }
            }

            return true;
        }

        public int getAvgAmplitudeNum() {
            return mAvgAmplitudeNum;
        }

        public boolean getIsNoMic() {
            return mIsNoMic;
        }

        @Override
        public void run() {
            mHandler.postDelayed(this, INTERVAL_DELAY_TIME);
            int tempMaxAmplitude = mMediaRecorder.getMaxAmplitude();
            if (tempMaxAmplitude > 0) {
                mAmplitudeDataList.add(tempMaxAmplitude);
            }
        }
    }

    @Override
    public void onCountDownFinish() {
        mView.setStopDownCountViewText("");
        mIsHasRecordStop = true;
        recordStop();
        mSoundAmplitude.stopGetAmplitude();
    }

    @Override
    public void onCountDownTick(long millisUntilFinished) {
        int tick = (int) (millisUntilFinished / 1000);
        String tickStr = Integer.toString(tick);
        mView.setStopDownCountViewText(tickStr);
    }
}
