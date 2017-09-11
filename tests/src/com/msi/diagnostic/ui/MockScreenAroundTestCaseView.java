
package com.msi.diagnostic.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.IDiagnosticApp;

public class MockScreenAroundTestCaseView extends ScreenAroundTestCaseView {

    private IDiagnosticApp mDiagAppStub;

    protected final int RADIUS = 16;
    private ArrayList<Integer> x;
    private ArrayList<Integer> y;

    private String mTestResult;
    private String mAllpass;
    private String mRetest;
    private String mTestfail;

    // Fake condition
    protected int mScreenWidth = 1280;
    protected int mScreenHeight = 800;

    protected static class Location {
        protected static final int SCREEN_TOP = 0;
        protected static final int SCREEN_RIGHT = 1;
        protected static final int SCREEN_BOTTOM = 2;
        protected static final int SCREEN_LEFT = 3;
        protected static final int SCREEN_MIDDLE_HORIZONTAL = 4;
        protected static final int SCREEN_MIDDLE_VERTICAL = 5;
    }

    public MockScreenAroundTestCaseView(IDiagnosticApp app) {
        mDiagAppStub = app;

        x = new ArrayList<Integer>();
        y = new ArrayList<Integer>();

        mAllpass = getDiagnosticApp().getAppContext()
                .getString(R.string.screen_around_allpass);
        mRetest = getDiagnosticApp().getAppContext()
                .getString(R.string.screen_around_retest);
        mTestfail = getDiagnosticApp().getAppContext()
                .getString(R.string.screen_around_test_fail);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
    }

    @Override
    public void onResume() {
        return;
    }

    @Override
    public void onPause() {
        return;
    }

    public void getCoordinate(int orient) {
        int xdotAmount = mScreenWidth / (2 * RADIUS);
        int ydotAmount = mScreenHeight / (2 * RADIUS);
        int xdotMiddle = xdotAmount / 2;
        int ydotMiddle = ydotAmount / 2;
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
                    y.add(RADIUS * (2 * (ydotAmount - 1) + 1));

                }
                break;

            case Location.SCREEN_LEFT:
                for (int i = 0; i < ydotAmount; ++i) {
                    x.add(RADIUS * (2 * i + 1));
                    y.add(RADIUS);
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
            default:
                break;
        }
    }

    @Override
    public ArrayList<Integer> getArrayX() {
        return x;
    }

    @Override
    public ArrayList<Integer> getArrayY() {
        return y;
    }

    public String getTestResult() {
        if (mTestResult.equals(mAllpass)) {
            return "ALLPASS";
        }
        else if (mTestResult.equals(mRetest)) {
            return "RETEST";
        }
        else if (mTestResult.equals(mTestfail)) {
            return "TESTFAIL";
        }
        else {
            return mTestResult;
        }

    }

    @Override
    public void deleteDot(int index) {
        Log.i("MockSATCV", "deleteDot: x=" + x.get(index) + " y=" + y.get(index));

        x.remove(index);
        y.remove(index);
        mTestResult = "NORMAL_HIT_TOUCH";
    }

    @Override
    public void setNote(String str) {
        mTestResult = str;
        return;
    }

    @Override
    public void setColor(int dColor, int tColor) {
        return;
    }

    @Override
    public void refreshLine(int locationIndex) {
        x.clear();
        y.clear();

        mTestResult = mRetest;
        getCoordinate(locationIndex);
    }

    @Override
    public void invalidate() {
        return;
    }

    @Override
    public int getWindowHeight() {
        return mScreenHeight;
    }

    @Override
    public int getWindowWidth() {
        return mScreenWidth;
    }

    @Override
    public void finishTestCase() {
        return;
    }

    @Override
    public IDiagnosticApp getDiagnosticApp() {
        return (IDiagnosticApp) mDiagAppStub;

    }

    @Override
    public String getLevelName() {
        return "com.msi.diagnostic.ui.ASSYPanelView";
    }

}
