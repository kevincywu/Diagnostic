package com.msi.diagnostic.app;

import java.io.File;
import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.ui.AudioFrontMicTestCaseView;

public class AudioFrontMICPagePresenter extends AbstractTestCasePresenter {

    private static final String RECORD_FILE_PATH = "RecordTest.amr";
    private static final File EXTERNAL_DIR_PATH = Environment.getExternalStorageDirectory();

    private AudioFrontMicTestCaseView mView;

    private MediaRecorder mMediaRecorder;
    private MediaPlayer mPlayer;
    private AudioManager mAudioManager;

    private int mMaxVolume;
    private File mRecordFileObj;
    private String mRecordFile;

    private boolean mRecordstop = true;
    private boolean mIsHasRecordStart = false;
    private boolean mIsHasRecordStop = false;
    private boolean mIsHasPlay = false;

    private static class TestItemId {
        public static final int FrontMicRecorded = 0;
    }

    private static final String[] TEST_ITEM_NAME = {
        "isFrontMicRecorded"
    };

    public AudioFrontMICPagePresenter(
            AudioFrontMicTestCaseView view,
            AudioManager audioManager) {

        super(view);

        mView = view;
        mAudioManager = audioManager;
        mRecordFileObj = new File(EXTERNAL_DIR_PATH, RECORD_FILE_PATH);
        mRecordFile = mRecordFileObj.getAbsolutePath();

        mPlayer = new MediaPlayer();
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        /** Set the sound to the maximum **/
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mMaxVolume, 0);
    }

    private TestResult isRecorded(int parameter, boolean isRecorded) {
        String itemName = TEST_ITEM_NAME[parameter];
        TestItem item = getTestItemByName(itemName);
        TestResult result = item.verify(new DetectionVerifiedInfo(isRecorded));
        return result;
    }

    private void audioPlay() {

        if (mRecordstop) {
            try {

                mView.setmMessageViewText(R.string.audio_back_audio_play);
                mPlayer.reset();
                mPlayer.setDataSource(mRecordFile);
                mPlayer.prepare();
                mPlayer.setLooping(true);
                mPlayer.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer arg0) {

                    try {

                        mPlayer.reset();
                        mPlayer.setDataSource(mRecordFile);
                        mPlayer.prepare();
                        mPlayer.setLooping(true);
                        mPlayer.start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {
            mView.setmMessageViewText(R.string.audio_back_not_file);
        }
    }

    private void recordStart() {

        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }

        if (mRecordstop) {

            mView.setmMessageViewText(R.string.audio_back_record_start);
            try {
                mMediaRecorder = new MediaRecorder();

                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                mMediaRecorder.setAudioEncodingBitRate(64000);
                mMediaRecorder.setAudioSamplingRate(44100);

                mMediaRecorder.setOutputFile(mRecordFile);
                mMediaRecorder.prepare();
                mMediaRecorder.start();

            } catch (IOException e) {
                isRecorded(TestItemId.FrontMicRecorded, false);
                e.printStackTrace();
            }

            mRecordstop = false;

        }
    }

    private void recordStop() {

        if (!mRecordstop) {

            mMediaRecorder.stop();
            mView.setmMessageViewText(R.string.audio_back_record_stop);
            mMediaRecorder.reset();
            mRecordstop = true;

        }
    }

    private void releaseMediaPlayer()
    {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void OnButtonClick(int buttonId) {
        switch (buttonId) {

        case R.id.audio_play:
            mIsHasPlay = true;
            audioPlay();
            break;

        case R.id.audio_start:
            mIsHasRecordStart = true;
            recordStart();
            break;

        case R.id.audio_stop:
            mIsHasRecordStop = true;
            recordStop();
            break;

        case R.id.audio_pass:
            if (!isNotTest()) {
                isRecorded(TestItemId.FrontMicRecorded, true);
            }
            if (!mRecordstop) {
                mMediaRecorder.stop();
            }
            if (mRecordFileObj.exists()) {
                mRecordFileObj.delete();
            }
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mView.finishTestCase();
            break;

        case R.id.audio_fail:
            if (!isNotTest()) {
                isRecorded(TestItemId.FrontMicRecorded, false);
            }
            if (!mRecordstop) {
                mMediaRecorder.stop();
            }
            if (mRecordFileObj.exists()) {
                mRecordFileObj.delete();
            }
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mView.finishTestCase();
            break;
        }
    }

    public boolean isNotTest() {
        if (mIsHasRecordStart && mIsHasRecordStop && mIsHasPlay) {
            return false;
        }
        return true;
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

}
