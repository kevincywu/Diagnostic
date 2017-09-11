package com.msi.diagnostic.ui;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.AudioStereoMICPagePresenter;

public class AudioStereoMicTestCaseView extends AbstractTestCaseView {

    public static final String TEST_DEBUG_TAG = "AudioStereoMicTestCaseView";

    public static final int[] RIGHT_KEYS_RESOURCE_ARRAY = new int[] {
            R.id.audio_stereo_right_key_C, R.id.audio_stereo_right_key_O,
            R.id.audio_stereo_right_key_P, R.id.audio_stereo_right_key_Y
    };
    public static final int[] LEFT_KEYS_RESOURCE_ARRAY = new int[] {
            R.id.audio_stereo_left_key_F, R.id.audio_stereo_left_key_J,
            R.id.audio_stereo_left_key_K, R.id.audio_stereo_left_key_L
    };

    private Activity mLevelPanel;
    private View mView;
    private AudioManager mAudioManager;

    private AudioStereoMICPagePresenter mPresenter;
    private ButtonClickListener mButtonClickListener;

    private Button mPlayAudioBtn;
    private Button mPauseAudioBtn;
    private Button mSoundFailBtn;
    private Button[] mRightKeysBtnArray = new Button[RIGHT_KEYS_RESOURCE_ARRAY.length];
    private Button[] mLeftKeysBtnArray = new Button[LEFT_KEYS_RESOURCE_ARRAY.length];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mLevelPanel = getActivity();
        mView = inflater.inflate(R.layout.audio_stereomic_testcase, container,
                false);

        mAudioManager = (AudioManager) mLevelPanel
                .getSystemService(Context.AUDIO_SERVICE);
        mPresenter = new AudioStereoMICPagePresenter(this, mAudioManager);
        mButtonClickListener = new ButtonClickListener(mPresenter);

        initPlayAudioBtn();
        initPauseAudioBtn();
        initSoundFailBtn();
        initAllKeyBtn();

        return mView;
    }

    private void initPlayAudioBtn() {
        mPlayAudioBtn = (Button) mView.findViewById(R.id.audio_stereo_play);
        mPlayAudioBtn.setOnClickListener(mButtonClickListener);
    }

    private void initPauseAudioBtn() {
        mPauseAudioBtn = (Button) mView.findViewById(R.id.audio_stereo_pause);
        mPauseAudioBtn.setOnClickListener(mButtonClickListener);
    }

    private void initSoundFailBtn() {
        mSoundFailBtn = (Button) mView
                .findViewById(R.id.audio_stereo_soundfail);
        mSoundFailBtn.setOnClickListener(mButtonClickListener);
    }

    private void initAllKeyBtn() {
        int rightKeyArraySize = RIGHT_KEYS_RESOURCE_ARRAY.length;
        for (int i = 0; i < rightKeyArraySize; ++i) {
            mRightKeysBtnArray[i] = (Button) mView
                    .findViewById(RIGHT_KEYS_RESOURCE_ARRAY[i]);
            mRightKeysBtnArray[i].setOnClickListener(mButtonClickListener);
        }

        int leftKeyArraySize = LEFT_KEYS_RESOURCE_ARRAY.length;
        for (int i = 0; i < leftKeyArraySize; ++i) {
            mLeftKeysBtnArray[i] = (Button) mView
                    .findViewById(LEFT_KEYS_RESOURCE_ARRAY[i]);
            mLeftKeysBtnArray[i].setOnClickListener(mButtonClickListener);
        }

        setAllKeyTag(AudioStereoMICPagePresenter.KEY_INVALID);
    }

    public Button[] getRightButtonArray() {
        return mRightKeysBtnArray;
    }

    public Button[] getLeftButtonArray() {
        return mLeftKeysBtnArray;
    }

    public void setAllKeyTag(boolean tag) {
        int rightKeyArraySize = RIGHT_KEYS_RESOURCE_ARRAY.length;
        for (int i = 0; i < rightKeyArraySize; ++i) {
            mRightKeysBtnArray[i].setTag(tag);
        }

        int leftKeyArraySize = LEFT_KEYS_RESOURCE_ARRAY.length;
        for (int i = 0; i < leftKeyArraySize; ++i) {
            mLeftKeysBtnArray[i].setTag(tag);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.resume();
    }
}