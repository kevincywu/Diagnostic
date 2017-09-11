/**
 * HdmiTest.java Written by : jasonshe Written for: EMS N0J1 Hdmi test Date :
 * Jan 11, 2012 Version : 1.0 Modified by: jasonshe Written for: code review
 * Date : Feb 05, 2012 Version : 1.0
 */

package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.HDMIPagePresenter;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * The class for MES HDMI test. Play a short audio file, visual whether the
 * image and sounds are converted to the display.
 * 
 * @Author jack
 * @Version 1.0, 2012/01/11
 */
public class HDMITestCaseView extends AbstractTestCaseView {

    private View mView;
    private Button mPlayBtn;
    private Button mStopBtn;
    private Button mPassBtn;
    private Button mVFailBtn;
    private Button mPFailBtn;
    private TextView mText;
    private HDMIPagePresenter mHdmiPanelPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.hdmi_testcase, container, false);

        mPlayBtn = (Button) mView.findViewById(R.id.play);
        mStopBtn = (Button) mView.findViewById(R.id.stop);
        mPassBtn = (Button) mView.findViewById(R.id.pass);
        mVFailBtn = (Button) mView.findViewById(R.id.vfail);
        mPFailBtn = (Button) mView.findViewById(R.id.pfail);
        mText = (TextView) mView.findViewById(R.id.textView1);

        mHdmiPanelPresenter = new HDMIPagePresenter(this);

        mPlayBtn.setOnClickListener(new ButtonClickListener(mHdmiPanelPresenter));
        mStopBtn.setOnClickListener(new ButtonClickListener(mHdmiPanelPresenter));
        mPassBtn.setOnClickListener(new ButtonClickListener(mHdmiPanelPresenter));
        mVFailBtn.setOnClickListener(new ButtonClickListener(
                mHdmiPanelPresenter));
        mPFailBtn.setOnClickListener(new ButtonClickListener(
                mHdmiPanelPresenter));

        return mView;
    }

    public void setTextView(int key) {
        mText.setText(getString(key));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHdmiPanelPresenter.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mHdmiPanelPresenter.resume();
    }

    private static final class ButtonClickListener implements
            View.OnClickListener {

        private HDMIPagePresenter mHdmiPanelPresenter;

        public ButtonClickListener(HDMIPagePresenter presenter) {
            mHdmiPanelPresenter = presenter;
        }

        @Override
        public synchronized void onClick(View v) {
            final int viewId = v.getId();
            mHdmiPanelPresenter.OnButtonClick(viewId);
        }
    }
}
