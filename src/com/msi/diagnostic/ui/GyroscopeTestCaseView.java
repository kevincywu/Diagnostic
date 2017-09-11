package com.msi.diagnostic.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.GyroscopePagePresenter;

public class GyroscopeTestCaseView extends AbstractTestCaseView {

    private View mView;

    public TextView mTextX, mTextY, mTextZ;
    private GyroscopePagePresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.gyroscope_testcase, container, false);

        mTextX = (TextView) mView.findViewById(R.id.x);
        mTextY = (TextView) mView.findViewById(R.id.y);
        mTextZ = (TextView) mView.findViewById(R.id.z);

        mPresenter = new GyroscopePagePresenter(this);

        return mView;
    }

    public String getGyroscopeSensorName() {
        return getString(R.string.gyroscope_sensor_name);
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
