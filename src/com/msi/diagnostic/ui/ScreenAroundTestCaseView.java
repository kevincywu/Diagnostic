/**
 * ScreenAroundTest.java
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

import java.util.ArrayList;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.DrawView;
import com.msi.diagnostic.app.ScreenAroundPagePresenter;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Screen around test. It will produce 4 corners of dots around screen, when
 * touch the dot, it will disappear. When all dots disappear, the test will be
 * regarded PASS. If you touch one corner over 5 times and the dots in the
 * corner still remain, you can test again or just treat it as FAIL.
 * 
 * Version 1.1: Change square test to four corners' test, each corner has nine
 * dots, and the retry time changes to five for each corner. Version 1.2:
 * Prevent execute onCreate again when connect miniUSB devices.
 * 
 * @Author Qiuchen Jiang, qiuchenjiang@msi.com
 * @Version 1.2, 02/20/2012
 */
public class ScreenAroundTestCaseView extends AbstractTestCaseView {

    private Activity mLevelPanel;
    private View mView;

    private int mScreenWidth;
    private int mScreenHeight;
    private ArrayList<Integer> x;
    private ArrayList<Integer> y;
    private DrawView mDraw;
    private final int RADIUS = 16;
    private static final int INIT_LOCATION = 0;
    private ScreenAroundPagePresenter mScreenAroundPagePresenter;
    private String mResidue;
    private String mPoint;

    private ScreenAroundInputEventListener mScreenAroundInputEventListener;

    private static class Location {
        private static final int SCREEN_TOP = 0;
        private static final int SCREEN_RIGHT = 1;
        private static final int SCREEN_BOTTOM = 2;
        private static final int SCREEN_LEFT = 3;
        private static final int SCREEN_MIDDLE_HORIZONTAL = 4;
        private static final int SCREEN_MIDDLE_VERTICAL = 5;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mLevelPanel = getActivity();
        mView = inflater.inflate(R.layout.blank_testcase, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mScreenAroundPagePresenter = new ScreenAroundPagePresenter(this);

        getWindowSize();
        mResidue = this.getString(R.string.screen_around_residue);
        mPoint = this.getString(R.string.screen_around_point);

        x = new ArrayList<Integer>();
        y = new ArrayList<Integer>();
        getCoordinate(INIT_LOCATION);

        mDraw = new DrawView(mLevelPanel);
        requestFullScreen(mDraw);

        mDraw.post(new Runnable() {
            @Override
            public void run() {
                // Because of the new layout including menu items in title,
                // decrease the height of the drawing area
                mScreenHeight -= getCurrentTitleBarHeight();
                mDraw.setWindowScale(mScreenWidth, mScreenHeight);
            }
        });

        mDraw.setPosition(x, y);
        mDraw.setNote(mResidue + x.size() + mPoint);
        mScreenAroundInputEventListener = new ScreenAroundInputEventListener();
        mDraw.setOnTouchListener(mScreenAroundInputEventListener);
        mDraw.setOnKeyListener(mScreenAroundInputEventListener);
        mDraw.setFocusable(true);
        mDraw.setFocusableInTouchMode(true);
        mDraw.requestFocus();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Redraw the line and note.
     */
    public void refreshLine(int locationIndex) {
        x.clear();
        y.clear();
        getCoordinate(locationIndex);
        mDraw.setPosition(x, y);
        mDraw.setNote(mResidue + x.size() + mPoint);
    }

    private void getWindowSize() {
        DisplayMetrics dm;
        dm = new DisplayMetrics();
        mLevelPanel.getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
    }

    public int getWindowWidth() {
        return mScreenWidth;
    }

    public int getWindowHeight() {
        return mScreenHeight;
    }

    public ArrayList<Integer> getArrayX() {
        return x;
    }

    public ArrayList<Integer> getArrayY() {
        return y;
    }

    public void invalidate() {
        mDraw.invalidate();
    }

    public void setNote(String str) {
        mDraw.setNote(str);
    }

    public void setColor(int dColor, int tColor) {
        mDraw.setColor(dColor, tColor);
    }

    /**
     * Get the coordinate of dots in one line at the specific orientation
     * 
     * @param orient
     *            Specific orientation(bottom, top, left or right of the screen)
     *            of dots of one line
     */
    private void getCoordinate(int orient) {
        int xdotAmount = mScreenWidth / (2 * RADIUS);
        int ydotAmount = mScreenHeight / (2 * RADIUS);
        int xdotMiddle = xdotAmount / 2;
        int ydotMiddle = ydotAmount / 2;
        int navigationBarHeight = getNavigationBarHeight();
        switch (orient) {
        case Location.SCREEN_TOP:
            for (int i = 0; i < xdotAmount; ++i) {
                x.add(RADIUS * (2 * i + 1));
                y.add(RADIUS);
            }
            break;

        case Location.SCREEN_RIGHT:
            for (int i = 0; i < ydotAmount; ++i) {
                x.add(RADIUS * (2 * (xdotAmount - 1) + 1));
                y.add(RADIUS * (2 * i + 1));
            }
            break;

        case Location.SCREEN_BOTTOM:
            for (int i = 0; i < xdotAmount; ++i) {
                x.add(RADIUS * (2 * i + 1));
                y.add(mScreenHeight + RADIUS - navigationBarHeight);
            }
            break;

        case Location.SCREEN_LEFT:
            for (int i = 0; i < ydotAmount; ++i) {
                x.add(RADIUS);
                y.add(RADIUS * (2 * i + 1));
            }
            break;
        case Location.SCREEN_MIDDLE_HORIZONTAL:
            for (int i = 0; i < xdotAmount; ++i) {
                x.add(RADIUS * (2 * i + 1));
                y.add(RADIUS * (2 * (ydotMiddle - 1) + 1));
            }
            break;
        case Location.SCREEN_MIDDLE_VERTICAL:
            for (int i = 0; i < ydotAmount; ++i) {
                x.add(RADIUS * (2 * (xdotMiddle - 1) + 1));
                y.add(RADIUS * (2 * i + 1));
            }
            break;
        default:
            break;
        }
    }

    private int getNavigationBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
      }

    /**
     * Delete the dot which has been touched, actually just delete it's
     * coordinate from the ArrayList, and redraw the remains.
     * 
     * @param index
     *            the index of the dot in ArrayList
     */
    public void deleteDot(int index) {
        x.remove(index);
        y.remove(index);
        mDraw.setPosition(x, y);
        mDraw.setNote(mResidue + x.size() + mPoint);
        mDraw.invalidate();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScreenAroundPagePresenter.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScreenAroundPagePresenter.resume();
    }

    
    @Override
    public void finishTestCase() {
        exitFullScreen();
        super.finishTestCase();
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

    private class ScreenAroundInputEventListener implements View.OnTouchListener,
            View.OnKeyListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Log.i("ScreenAroundTCV","receive the motion event");
            mScreenAroundPagePresenter.touchEvent(event);
            return true;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            mScreenAroundPagePresenter.onKeyEventHandler(keyCode, event);
            return true;
        }
    }
}
