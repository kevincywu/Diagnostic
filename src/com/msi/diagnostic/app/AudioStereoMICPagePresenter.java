package com.msi.diagnostic.app;

import java.util.Random;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.Button;

import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.ui.AudioStereoMicTestCaseView;

public class AudioStereoMICPagePresenter extends AbstractTestCasePresenter {

    private static final int[] RIGHT_SOUNDS_RESOURCE_ARRAY = new int[] {
            R.raw.c_r, R.raw.o_r,
            R.raw.p_r, R.raw.y_r
    };
    private static final int[] LEFT_SOUNDS_RESOURCE_ARRAY = new int[] {
            R.raw.f_l, R.raw.j_l,
            R.raw.k_l, R.raw.l_l
    };
    public static final boolean KEY_INVALID = false;
    public static final boolean KEY_VALID = true;
    private static final int PASS_NUM_STANDARD = 2;
    private int mRightKeyValidIndex = -1;
    private int mLeftKeyValidIndex = -1;

    private AudioStereoMicTestCaseView mAudioStereoMicTestCaseView;
    private AudioManager mAudioManager;
    private MediaPlayer mLeftSoundPlayer;
    private MediaPlayer mRightSoundPlayer;
    private int mVolumeInTest;
    private int mVolumeBeforeTest;
    private Context mContext;
    private int mCountPassNum;

    private static class TestItemId {
        public static final int STEREO_SOUND = 0;
    }

    private static final String[] TEST_ITEM_NAME = {
        "isValidStereoSound"
    };

    public AudioStereoMICPagePresenter(AudioStereoMicTestCaseView view,
            AudioManager audioManager) {

        super(view);
        mAudioStereoMicTestCaseView = view;
        mContext = mView.getDiagnosticApp().getAppContext();
        mAudioManager = audioManager;

        mRightSoundPlayer = new MediaPlayer();
        mLeftSoundPlayer = new MediaPlayer();

        getVolumebeforeTest();
        setVolumeMaximum();
    }

    private void getVolumebeforeTest() {
        mVolumeBeforeTest = mAudioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    private void setVolumeMaximum() {
        mVolumeInTest = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mVolumeInTest,
                0);
    }

    private void createRightPlayer() {
        mRightSoundPlayer = MediaPlayer.create(mContext,
                RIGHT_SOUNDS_RESOURCE_ARRAY[mRightKeyValidIndex]);
        mRightSoundPlayer
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mLeftSoundPlayer.start();
                    }
                });
    }

    private void createLeftPlayer() {
        mLeftSoundPlayer = MediaPlayer.create(mContext,
                LEFT_SOUNDS_RESOURCE_ARRAY[mLeftKeyValidIndex]);
        mLeftSoundPlayer
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mRightSoundPlayer.start();
                    }
                });
    }

    private void setRightButtonValid() {
        int rightBtnArraySize = mAudioStereoMicTestCaseView
                .getRightButtonArray().length;
        for (int i = 0; i < rightBtnArraySize; ++i) {
            if (i != mRightKeyValidIndex) {
                mAudioStereoMicTestCaseView.getRightButtonArray()[i]
                        .setTag(KEY_INVALID);
            } else {
                mAudioStereoMicTestCaseView.getRightButtonArray()[i]
                        .setTag(KEY_VALID);
            }
        }
    }

    private void setLeftButtonValid() {
        int leftBtnArraySize = mAudioStereoMicTestCaseView.getLeftButtonArray().length;
        for (int i = 0; i < leftBtnArraySize; ++i) {
            if (i != mLeftKeyValidIndex) {
                mAudioStereoMicTestCaseView.getLeftButtonArray()[i]
                        .setTag(KEY_INVALID);
            } else {
                mAudioStereoMicTestCaseView.getLeftButtonArray()[i]
                        .setTag(KEY_VALID);
            }
        }
    }

    private void startPlaySounds() {
        pickKeyValid();
        createRightPlayer();
        createLeftPlayer();
        setRightButtonValid();
        setLeftButtonValid();
        mRightSoundPlayer.start();
    }

    private void pickKeyValid() {
        Random r = new Random();
        mRightKeyValidIndex = r.nextInt(RIGHT_SOUNDS_RESOURCE_ARRAY.length);
        mLeftKeyValidIndex = r.nextInt(LEFT_SOUNDS_RESOURCE_ARRAY.length);
    }

    private boolean checkRightKeyAndSoundValid(int keyId) {
        boolean isValid = false;
        int size = mAudioStereoMicTestCaseView.RIGHT_KEYS_RESOURCE_ARRAY.length;
        for (int i = 0; i < size; ++i) {
            if (keyId == mAudioStereoMicTestCaseView.RIGHT_KEYS_RESOURCE_ARRAY[i]) {
                Button button = mAudioStereoMicTestCaseView
                        .getRightButtonArray()[i];
                if (button.getTag().equals(KEY_INVALID)) {
                    setItemResultFail();
                } else if (button.getTag().equals(KEY_VALID)) {
                    button.setTag(KEY_INVALID);
                    mCountPassNum++;
                    if (mCountPassNum == PASS_NUM_STANDARD) {
                        setItemResultPass();
                    }
                }
                isValid = (Boolean) button.getTag();
                break;
            }
        }
        return isValid;
    }

    private boolean checkLeftKeyAndSoundValid(int keyId) {
        boolean isValid = false;
        int size = mAudioStereoMicTestCaseView.LEFT_KEYS_RESOURCE_ARRAY.length;
        for (int i = 0; i < size; ++i) {
            if (keyId == mAudioStereoMicTestCaseView.LEFT_KEYS_RESOURCE_ARRAY[i]) {
                Button button = mAudioStereoMicTestCaseView
                        .getLeftButtonArray()[i];
                if (button.getTag().equals(KEY_INVALID)) {
                    setItemResultFail();
                } else if (button.getTag().equals(KEY_VALID)) {
                    button.setTag(KEY_INVALID);
                    mCountPassNum++;
                    if (mCountPassNum == PASS_NUM_STANDARD) {
                        setItemResultPass();
                    }
                }
                isValid = (Boolean) button.getTag();
                break;
            }
        }
        return isValid;
    }

    private TestResult verifyItemResult(int parameter, boolean isRecorded) {
        releaseMediaPlayer();
        setVolumebeforeTest();

        String itemName = TEST_ITEM_NAME[parameter];
        TestItem item = getTestItemByName(itemName);
        TestResult result = item.verify(new DetectionVerifiedInfo(isRecorded));
        return result;
    }

    private void setVolumebeforeTest() {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                mVolumeBeforeTest, 0);
    }

    private void setItemResultFail() {
        verifyItemResult(TestItemId.STEREO_SOUND, false);
        mView.finishTestCase();
    }

    private void setItemResultPass() {
        verifyItemResult(TestItemId.STEREO_SOUND, true);
        mView.finishTestCase();
    }

    private void releaseMediaPlayer() {
        if (mLeftSoundPlayer != null) {
            mLeftSoundPlayer.stop();
            mLeftSoundPlayer.release();
            mLeftSoundPlayer = null;
        }
        if (mRightSoundPlayer != null) {
            mRightSoundPlayer.stop();
            mRightSoundPlayer.release();
            mRightSoundPlayer = null;
        }
    }

    private void resetTestCase() {
        mCountPassNum = 0;
        if (mLeftSoundPlayer != null) {
            mLeftSoundPlayer.stop();
        }
        if (mRightSoundPlayer != null) {
            mRightSoundPlayer.stop();
        }
        mAudioStereoMicTestCaseView.setAllKeyTag(false);
    }

    @Override
    public void OnButtonClick(int buttonId) {
        switch (buttonId) {
        case R.id.audio_stereo_play:
            if (!mLeftSoundPlayer.isPlaying() && !mRightSoundPlayer.isPlaying()) {
                startPlaySounds();
            }
            break;
        case R.id.audio_stereo_right_key_C:
        case R.id.audio_stereo_right_key_O:
        case R.id.audio_stereo_right_key_P:
        case R.id.audio_stereo_right_key_Y:
            if (mLeftSoundPlayer.isPlaying() || mRightSoundPlayer.isPlaying()) {
                checkRightKeyAndSoundValid(buttonId);
            }
            break;
        case R.id.audio_stereo_left_key_F:
        case R.id.audio_stereo_left_key_J:
        case R.id.audio_stereo_left_key_K:
        case R.id.audio_stereo_left_key_L:
            if (mLeftSoundPlayer.isPlaying() || mRightSoundPlayer.isPlaying()) {
                checkLeftKeyAndSoundValid(buttonId);
            }
            break;
        case R.id.audio_stereo_pause:
            resetTestCase();
            break;
        case R.id.audio_stereo_soundfail:
            break;
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
}
