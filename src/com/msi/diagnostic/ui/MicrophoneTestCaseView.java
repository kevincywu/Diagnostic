package com.msi.diagnostic.ui;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.MicrophonePagePresenter;

public class MicrophoneTestCaseView extends AbstractTestCaseView {

    private Activity mLevelPanel;
    private View mView;
    private AudioManager mAudioManager;

    private MicrophonePagePresenter mPresenter;
    private ButtonClickListener mButtonClickListener;

    private Button mStartRecordBtn;
    private Button mPlayAudioBtn;
    private Button mRecordPassBtn;

    private Button mRecordFailBtn;
    private TextView mMessageView;
    private TextView mStopDownCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mLevelPanel = getActivity();
        mView = inflater
                .inflate(R.layout.microphone_testcase, container, false);

        initAudioManager();
        initMicrophonePagePresenter();
        initButtonClickListener();
        initStartRecordBtn();
        initStopDownCountText();
        initPlayAudioBtn();
        initRecordPassBtn();
        initRecordFailBtn();
        initMessageView();

        return mView;
    }

    private void initStartRecordBtn() {
        mStartRecordBtn = (Button) mView.findViewById(R.id.microphone_start);
        mStartRecordBtn.setOnClickListener(mButtonClickListener);
    }

    private void initStopDownCountText() {
        mStopDownCount = (TextView) mView.findViewById(R.id.microphone_stop_downcount);
    }

    private void initPlayAudioBtn() {
        mPlayAudioBtn = (Button) mView.findViewById(R.id.microphone_play);
        mPlayAudioBtn.setOnClickListener(mButtonClickListener);
    }

    private void initRecordPassBtn() {
        mRecordPassBtn = (Button) mView.findViewById(R.id.microphone_pass);
        mRecordPassBtn.setOnClickListener(mButtonClickListener);
    }

    private void initRecordFailBtn() {
        mRecordFailBtn = (Button) mView.findViewById(R.id.microphone_fail);
        mRecordFailBtn.setOnClickListener(mButtonClickListener);
        setRecordPassBtnVisible(View.INVISIBLE);
    }

    private void initMessageView() {
        mMessageView = (TextView) mView.findViewById(R.id.microphone_show_view);
    }

    private void initAudioManager() {
        mAudioManager = (AudioManager) mLevelPanel
                .getSystemService(Context.AUDIO_SERVICE);
    }

    private void initMicrophonePagePresenter() {
        mPresenter = new MicrophonePagePresenter(this, mAudioManager);
    }

    private void initButtonClickListener() {
        mButtonClickListener = new ButtonClickListener(mPresenter);
    }

    public void setmMessageViewText(int mMessage) {
        mMessageView.setText(mMessage);
    }

    public TextView getMeggageViewText() {
        return mMessageView;
    }

    public void setStopDownCountViewText(String text) {
        mStopDownCount.setText(text);
    }

    public TextView getStopDownCountViewText() {
        return mStopDownCount;
    }

    public void setRecordPassBtnVisible(int visible) {
        mRecordPassBtn.setVisibility(visible);
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