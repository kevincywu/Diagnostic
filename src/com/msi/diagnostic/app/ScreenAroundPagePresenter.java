package com.msi.diagnostic.app;

import java.util.ArrayList;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.ui.ScreenAroundTestCaseView;

public class ScreenAroundPagePresenter extends AbstractTestCasePresenter {

    private ScreenAroundTestCaseView mVIew;

    private int mTouchUpTime;
    private int mLocationIndex;
    private String mAllpass;
    private String mRetest;
    private String mTestfail;
    private final int MAX_FAIL_TIME = 15;
    private final int RADIUS = 32;
    private boolean mPassFlag = true;
    private boolean mFailFlag = true;
    private static String TEST_ITEM_NAME = "isScreenAroundPass";

    public ScreenAroundPagePresenter(ScreenAroundTestCaseView view) {
        super(view);
        mVIew = view;
        mTouchUpTime = 0;

        mAllpass = mVIew.getDiagnosticApp().getAppContext()
                .getString(R.string.screen_around_allpass);
        mRetest = mVIew.getDiagnosticApp().getAppContext()
                .getString(R.string.screen_around_retest);
        mTestfail = mVIew.getDiagnosticApp().getAppContext()
                .getString(R.string.screen_around_test_fail);
    }

    @Override
    public void OnButtonClick(int buttonId) {
    }

    public void touchEvent(MotionEvent event) {
        /**
         * When your finger up from the screen, maybe it means you have left out
         * dots in this line, then touchUpTime++. The maximum finger up action
         * is 10, when touchUpTime==10, we'll treat this line touch fails.
         */
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mTouchUpTime++;
        }
        ArrayList<Integer> x = mVIew.getArrayX();
        /** Only one point touch is recognized. */
        if (event.getPointerCount() == 1) {
            /** Dots in one line are all touched */
            if (x.size() == 0) {
                handleLineDone();
            } else {
                if (mTouchUpTime >= MAX_FAIL_TIME) {
                    handleLineFail(event);
                } else {
                    handleNormalTouch(event);
                }
            }
        }
    }

    public boolean onKeyEventHandler(int keyCode, KeyEvent event) {

        // Skip the BACK event during the test
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }

    /**
     * When one all dots in one line are disappeared, draw a new line or if all
     * 4 lines are done, return PASS result.
     */
    private void handleLineDone() {
        if (mLocationIndex++ <= 5) {
            mTouchUpTime = 0;
            mVIew.refreshLine(mLocationIndex);
            mVIew.invalidate();
        } else if (mPassFlag) {
            mPassFlag = false;
            mVIew.setNote(mAllpass);
            mVIew.invalidate();
            writeToDB(true);
        }
    }

    private void handleNormalTouch(MotionEvent event) {
        ArrayList<Integer> x = mVIew.getArrayX();
        ArrayList<Integer> y = mVIew.getArrayY();

        for (int i = 0; i < x.size(); ++i) {
            if (event.getX() <= (x.get(i) + RADIUS)
                    && event.getX() >= (x.get(i) - RADIUS)
                    && event.getY() <= (y.get(i) + RADIUS)
                    && event.getY() >= (y.get(i) - RADIUS)) {
                mVIew.deleteDot(i);
            }
        }
    }

    /**
     * TouchUpTime equals to MAX_FAIL_TIME(5), that means this line touch fails,
     * you can choose whether to retry or give up.
     * 
     * @param event
     *            Touch event from onTouchEvent.
     */
    private void handleLineFail(MotionEvent event) {
        int screenHeight = mVIew.getWindowHeight();
        int screenWidth = mVIew.getWindowWidth();
        mVIew.setNote(mRetest);
        mVIew.setColor(Color.BLUE, Color.RED);
        mVIew.invalidate();
        // Click retry text
        if (event.getX() >= (screenWidth / 4 - 250)
                && event.getX() <= (screenWidth / 4 - 200 + 150)
                && event.getY() >= (screenHeight / 4 + 100 - 15)
                && event.getY() <= (screenHeight / 4 + 100 + 15)) {
            mTouchUpTime = 0;
            mVIew.setColor(Color.BLUE, Color.BLUE);
            mVIew.refreshLine(mLocationIndex);
            mVIew.invalidate();
        }
        // Click finish text
        else if (event.getX() >= (screenWidth / 4 + 50)
                && event.getX() <= (screenWidth / 4 + 50 + 150)
                && event.getY() >= (screenHeight / 4 + 100 - 15)
                && event.getY() <= (screenHeight / 4 + 100 + 15) && mFailFlag) {
            mFailFlag = false;
            mVIew.setNote(mTestfail);
            mVIew.invalidate();
            writeToDB(false);
        }
    }

    private void writeToDB(Boolean result) {
        TestItem item = getTestItemByName(TEST_ITEM_NAME);
        item.verify(new DetectionVerifiedInfo(result));
        // End of this TestCase
        mVIew.finishTestCase();
    }
}
