package com.msi.diagnostic.app;

import android.util.Log;

import com.msi.diagnostic.ui.WifiTestCaseView;

public class WifiPagePresenter extends AbstractTestCasePresenter {
    private static final String TAG = "WifiPagePresenter";
    private static final boolean LOCAL_LOG = true;

    private WifiTest mWifiTest;
    public WifiTestCaseView mView;

    public WifiPagePresenter(WifiTestCaseView view) {
        super(view);
        mView = view;
        mWifiTest = new WifiTest(mView.getAppContext(), this);
        mWifiTest.execute();
        mView.finishTestCase();
    }

    @Override
    public void OnButtonClick(int buttonId) {
    }

    @Override
    public void resume() {
        if (LOCAL_LOG)
            Log.d(TAG, "resume");
        super.resume();
    }

    @Override
    public void pause() {
        if (LOCAL_LOG)
            Log.d(TAG, "pause");
        super.pause(false);
    }
}
