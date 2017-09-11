package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.GSensorPagePresenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GSensorTestCaseView extends AbstractTestCaseView {

    private View mView;

    private GSensorPagePresenter mPresenter;
    public TextView mTextX, mTextY, mTextZ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.gsensor_testcase, container, false);

        mTextX = (TextView) mView.findViewById(R.id.x);
        mTextY = (TextView) mView.findViewById(R.id.y);
        mTextZ = (TextView) mView.findViewById(R.id.z);

        mPresenter = new GSensorPagePresenter(this);

        return mView;
    }

    public void setTextX(float value) {
        String valueStr = String.valueOf(value);
        mTextX.setText(valueStr);
    }

    public void setTextY(float value) {
        String valueStr = String.valueOf(value);
        mTextY.setText(valueStr);
    }

    public void setTextZ(float value) {
        String valueStr = String.valueOf(value);
        mTextZ.setText(valueStr);
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
