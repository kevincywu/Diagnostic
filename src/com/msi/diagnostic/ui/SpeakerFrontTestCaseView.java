package com.msi.diagnostic.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.SpeakerFrontPagePresenter;

public class SpeakerFrontTestCaseView extends AbstractTestCaseView
{
    private View mView;
    private Button mPlayBtn;
    private Button mStopBtn;
    private Button mFailBtn;
    private Button mCBtn;
    private Button mOBtn;
    private Button mPBtn;
    private Button mYBtn;
    private Button mFBtn;
    private Button mJBtn;
    private Button mKBtn;
    private Button mLBtn;
    private TextView mText;
    private SpeakerFrontPagePresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.speaker_front_testcase, container, false);
        mPlayBtn = (Button) mView.findViewById(R.id.speaker_front_play);
        mStopBtn = (Button) mView.findViewById(R.id.speaker_front_stop);
        mFailBtn = (Button) mView.findViewById(R.id.speaker_front_fail);
        mCBtn = (Button) mView.findViewById(R.id.speaker_front_c);
        mOBtn = (Button) mView.findViewById(R.id.speaker_front_o);
        mPBtn = (Button) mView.findViewById(R.id.speaker_front_p);
        mYBtn = (Button) mView.findViewById(R.id.speaker_front_y);
        mFBtn = (Button) mView.findViewById(R.id.speaker_front_f);
        mJBtn = (Button) mView.findViewById(R.id.speaker_front_j);
        mKBtn = (Button) mView.findViewById(R.id.speaker_front_k);
        mLBtn = (Button) mView.findViewById(R.id.speaker_front_l);
        mText = (TextView) mView.findViewById(R.id.speaker_front_text);

        mPresenter = new SpeakerFrontPagePresenter(this);
        mPlayBtn.setOnClickListener(new ButtonClickListener(mPresenter));
        mStopBtn.setOnClickListener(new ButtonClickListener(mPresenter));
        mFailBtn.setOnClickListener(new ButtonClickListener(mPresenter));
        mCBtn.setOnClickListener(new ButtonClickListener(mPresenter));
        mOBtn.setOnClickListener(new ButtonClickListener(mPresenter));
        mPBtn.setOnClickListener(new ButtonClickListener(mPresenter));
        mYBtn.setOnClickListener(new ButtonClickListener(mPresenter));
        mFBtn.setOnClickListener(new ButtonClickListener(mPresenter));
        mJBtn.setOnClickListener(new ButtonClickListener(mPresenter));
        mKBtn.setOnClickListener(new ButtonClickListener(mPresenter));
        mLBtn.setOnClickListener(new ButtonClickListener(mPresenter));
        return mView;
    }

    public void setTextView(int key)
    {
        mText.setText(getString(key));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mPresenter.pause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPresenter.resume();
    }

    private static final class ButtonClickListener implements View.OnClickListener
    {

        private SpeakerFrontPagePresenter mPresenter;

        public ButtonClickListener(SpeakerFrontPagePresenter presenter) {
            mPresenter = presenter;
        }

        @Override
        public synchronized void onClick(View v)
        {
            final int viewId = v.getId();
            mPresenter.OnButtonClick(viewId);
        }
    }
}