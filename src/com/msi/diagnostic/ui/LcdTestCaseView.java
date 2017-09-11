/**
 * LcdTest.java
 * 
 * Written by : Qiuchen Jiang Written for: N0J1 MES Date : Dec 21, 2011 Version
 * : 1.0
 * 
 * Modified by: Qiuchen Jiang Written for: N0J1 MES Date : Jan 19, 2012 Version
 * : 1.1
 * 
 * Modified by: Qiuchen Jiang Written for: N0J1 MES Date : Feb 20, 2012 Version
 * : 1.2
 */

package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.LcdPagePresenter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Rect;

/**
 * RGB black white test && single touch test. Produce a random buttons in each
 * color, if there's no click event on random button in 15s, the test will be
 * regarded failed.
 * 
 * Version 1.1: Change next activity from MultiTouchTest to ScreenAroundTest.
 * Version 1.2: Prevent execute onCreate again when connect miniUSB devices.
 * 
 * @Author Qiuchen Jiang, Jimyang.
 * @Version 1.2, 02/20/2012
 *
 * TODO: onKeyEvent
 */
@SuppressWarnings("deprecation")
public class LcdTestCaseView extends AbstractTestCaseView {

    private Activity mLevelPanel;
    private View mTestView;
    private View mJundgeView;

    private int mScreenWidth;
    private int mScreenHeight;
    private Button mClickButton;
    private AbsoluteLayout mAl;
    private AbsoluteLayout.LayoutParams mVg;
    private String mResidue;
    private String mSecond;
    private String mResidueTime;
    private LcdPagePresenter mLcdPagePresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mLevelPanel = getActivity();
        mTestView = inflater.inflate(R.layout.lcd_testcase, container, false);
        mJundgeView = inflater.inflate(R.layout.lcdjudge, container, false);
        mLcdPagePresenter = new LcdPagePresenter(this);
        return mJundgeView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mResidueTime = getString(R.string.lcd_countdownTime);
        mResidue = getString(R.string.lcd_residue);
        mSecond = getString(R.string.lcd_second);
        getWindowSize();
        requestFullScreen(mTestView);

        mClickButton = (Button) mTestView.findViewById(R.id.click_button);
        mAl = (AbsoluteLayout) mTestView.findViewById(R.id.absolute_layout);
        mVg = (AbsoluteLayout.LayoutParams) mClickButton.getLayoutParams();
        mClickButton.setText(mResidueTime);
        mClickButton.setOnClickListener(
                new ButtonClickListener(mLcdPagePresenter));
        mLcdPagePresenter.setBackground();
    }

    private int getCurrentTitleBarHeight() {
        Rect rect = new Rect();
        Window win = mLevelPanel.getWindow();
        win.getDecorView().getWindowVisibleDisplayFrame(rect);

        int contentViewTop = win.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int statusBarHeight = rect.top;
        int titleBarHeight = contentViewTop - statusBarHeight;
        return titleBarHeight;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void getWindowSize() {
        DisplayMetrics dm = new DisplayMetrics();
        mLevelPanel.getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        mScreenHeight -= getCurrentTitleBarHeight();
    }

    public void setClickButtonText(long time) {
        mClickButton.setText(mResidue + time + mSecond);
    }

    /**
     * Create new button position, change the backgroud color
     */
    public void setBackground(int colorIndex) {
        mVg.x = getRandom(mScreenWidth, mClickButton.getWidth());
        mVg.y = getRandom(mScreenHeight, mClickButton.getHeight());
        mAl.setBackgroundResource(colorIndex);
        mClickButton.setLayoutParams(mVg);
    }

    /**
     * Get random position of the button
     * 
     * @param range
     *            The maximum random number.
     * @param buttonSize
     *            Random button size.
     * @return pos Random number, less than range
     */
    private int getRandom(int range, int buttonSize) {
        if (buttonSize == 0) {
            buttonSize = 150;
        }
        int pos = (int) Math.round(Math.random() * range);
        if (pos + buttonSize > range - getCurrentTitleBarHeight()) {
            pos = range - (buttonSize + getCurrentTitleBarHeight() / 2);
        }
        return pos;
    }

    /**
     * Pass or Fail judgment
     */
    public void jumpToJudge() {
        exitFullScreen();
        Button passButton = (Button) mJundgeView.findViewById(R.id.lcd_pass_button);
        Button failButton = (Button) mJundgeView.findViewById(R.id.lcd_fail_button);
        passButton.setOnClickListener(
                new ButtonClickListener(mLcdPagePresenter));
        failButton.setOnClickListener(
                new ButtonClickListener(mLcdPagePresenter));
    }

    @Override
    public void onPause() {
        super.onPause();
        mLcdPagePresenter.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mLcdPagePresenter.resume();
    }
}
