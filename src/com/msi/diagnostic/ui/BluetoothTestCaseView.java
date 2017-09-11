package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.BluetoothPagePresenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BluetoothTestCaseView extends AbstractTestCaseView {

    private Activity mLevelPanel;
    private View mView;

    private BluetoothPagePresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mLevelPanel = getActivity();
        mView = inflater.inflate(R.layout.bluetooth_testcase, container, false);
        mPresenter = new BluetoothPagePresenter(this);
        return mView;
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
