package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.AudioFrontMICPagePresenter;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AudioFrontMicTestCaseView extends AbstractTestCaseView {

    private Activity mLevelPanel;
    private View mView;
    private AudioManager mAudioManager;

    private AudioFrontMICPagePresenter mPresenter;
    private ButtonClickListener mButtonClickListener;

    private Button mStartRecordBtn;
    private Button mStopRecordBtn;

    private Button mPlayAudioBtn;
    private Button mRecordPassBtn;

    private Button mRecordFailBtn;
    private TextView mMessageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mLevelPanel = getActivity();
        mView = inflater.inflate(R.layout.audio_frontmic_testcase, container, false);

        mStartRecordBtn = (Button) mView.findViewById(R.id.audio_start);
        mStopRecordBtn = (Button) mView.findViewById(R.id.audio_stop);

        mPlayAudioBtn = (Button) mView.findViewById(R.id.audio_play);
        mRecordPassBtn = (Button) mView.findViewById(R.id.audio_pass);

        mRecordFailBtn = (Button) mView.findViewById(R.id.audio_fail);
        mMessageView = (TextView) mView.findViewById(R.id.audio_show_view);

        mAudioManager = (AudioManager) mLevelPanel.getSystemService(Context.AUDIO_SERVICE);
        mPresenter = new AudioFrontMICPagePresenter(this, mAudioManager);
        mButtonClickListener = new ButtonClickListener(mPresenter);

        mStartRecordBtn.setOnClickListener(mButtonClickListener);

        mStopRecordBtn.setOnClickListener(mButtonClickListener);
        mPlayAudioBtn.setOnClickListener(mButtonClickListener);

        mRecordPassBtn.setOnClickListener(mButtonClickListener);
        mRecordFailBtn.setOnClickListener(mButtonClickListener);

        
        return mView;
    }

    public void setmMessageViewText(int mMessage) {
        mMessageView.setText(mMessage);
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
