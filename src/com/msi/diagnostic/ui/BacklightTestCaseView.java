/**
 * BacklightTest.java
 * 
 * Written by : Qiuchen Jiang Written for: N0J1 MES Date : Dec 21, 2011 Version
 * : 1.0
 * 
 * Modified by: Qiuchen Jiang Written for: N0J1 MES Date : Feb 20, 2012 Version
 * : 1.1
 */

package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.BacklightPagePresenter;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;

/**
 * Use seek bar to control back light of screen, press pass or fail button to
 * judge whether screen brightness changes or not.
 * 
 * Version 1.1: Prevent execute onCreate again when connect miniUSB devices.
 * 
 * @Author Qiuchen Jiang, Jimyang.
 * @Version 1.1, 02/20/2012
 *
 * TODO: KeyEvent Listener
 */
public class BacklightTestCaseView extends AbstractTestCaseView {

    private View mView;
    private Activity mLevelPanel;

    private Button mPassButton;
    private Button mFailButton;
    private SeekBar mBrightnessControl;
    private BacklightPagePresenter mPresenter;
    private static boolean sIsNotTest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mLevelPanel = getActivity();
        mView = inflater.inflate(R.layout.backlight_testcase, container, false);
        sIsNotTest = true;
        return mView;
    }

    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new BacklightPagePresenter(this);
        mBrightnessControl = (SeekBar) mView.findViewById(R.id.seek);
        mPassButton = (Button) mView.findViewById(R.id.backlight_pass);
        mFailButton = (Button) mView.findViewById(R.id.backlight_fail);
        mPassButton.setOnClickListener(new ButtonClickListener(mPresenter));
        mFailButton.setOnClickListener(new ButtonClickListener(mPresenter));
        mBrightnessControl.setOnSeekBarChangeListener(
                new ButtonClickListener(mPresenter));

        // Reset the brightness of back-light responding with
        // the default location of progress bar
        setBacklightBrightness( (float)(50f/100f));

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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

    public void setBacklightBrightness(float value) {
        Window panelWindow = mLevelPanel.getWindow();
        WindowManager.LayoutParams layoutParams = panelWindow.getAttributes();
        layoutParams.screenBrightness = value;
        panelWindow.setAttributes(layoutParams);
    }

    public boolean getTestFlag() {
        return sIsNotTest;
    }
    private static final class ButtonClickListener implements
            View.OnClickListener, SeekBar.OnSeekBarChangeListener  {

        private BacklightPagePresenter mBacklightPagePresenter;

        public ButtonClickListener(BacklightPagePresenter presenter) {
            mBacklightPagePresenter = presenter;
        }

        @Override
        public synchronized void onClick(View v) {
            final int viewId = v.getId();
            mBacklightPagePresenter.OnButtonClick(viewId);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                boolean fromUser) {
            mBacklightPagePresenter.setBrightness(progress);
            sIsNotTest = false;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }
}
