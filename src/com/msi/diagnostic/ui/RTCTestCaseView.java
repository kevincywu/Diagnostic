package com.msi.diagnostic.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.RTCPagePresenter;

public class RTCTestCaseView extends AbstractTestCaseView {

    private View mView;
    private RTCPagePresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.rtc_testcase, container, false);
        mPresenter = new RTCPagePresenter(this);
        return mView;
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
