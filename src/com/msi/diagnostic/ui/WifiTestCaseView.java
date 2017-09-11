package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.WifiPagePresenter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WifiTestCaseView extends AbstractTestCaseView {

    private AbstractLevelPanelView mLevelPanel;
    private View mView;

    private WifiPagePresenter mWifiPagePresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mLevelPanel = (AbstractLevelPanelView) getActivity();
        mView = inflater.inflate(R.layout.wifi_testcase, container, false);
        mWifiPagePresenter = new WifiPagePresenter(this);

        return mView;
    }

    public Context getAppContext() {
        return mLevelPanel.getApplicationContext();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWifiPagePresenter.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWifiPagePresenter.resume();
    }
}
