package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.RuninVideoPagePresenter;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class RuninVideoTestCaseView extends AbstractTestCaseView {

    private Activity mLevelPanel;
    private View mView;
    private View mFullView;

    private RuninVideoPagePresenter mPresenter;
    private MediaController mMediaController;

    private VideoView mVideoView;
    private TextView mShowTimeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mLevelPanel = getActivity();
        mView = inflater.inflate(R.layout.blank_testcase, container, false);
        mFullView = inflater.inflate(R.layout.runin_video_testcase, container, false);
        mPresenter = new RuninVideoPagePresenter(this);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mVideoView = (VideoView) mFullView.findViewById(R.id.videoView);
        mShowTimeView = (TextView) mFullView.findViewById(R.id.showTimeView);
        mFullView.setBackgroundResource(R.color.white);
        requestFullScreen(mFullView);
    }

    public void videoViewStart() {
        mVideoView.start();
    }

    public void videoViewRequestFocus() {
        mVideoView.requestFocus();
    }

    public void setmShowTimeViewText(String str) {
        mShowTimeView.setText(str);
    }

    public VideoView getmVideoView() {
        return mVideoView;
    }

    public void setmMediaController() {
        mMediaController = new MediaController(mLevelPanel);
    }

    public MediaController getmMediaController() {
        return mMediaController;
    }

    public Context getAppContext() {
        return mLevelPanel.getApplicationContext();
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
