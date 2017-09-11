package com.msi.diagnostic.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.LightSensorPagePresenter;

public class LightSensorTestCaseView extends AbstractTestCaseView {


    private View mView;

    private LightSensorPagePresenter mPresenter;
    private Button mSubmit;
    private TextView mCurrentVauleTextView;
    private TextView mDarkValueTextView;
    private TextView mLightValueTextView;
    private ButtonClickListener mButtonClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.lightsensor_testcase, container, false);

        mPresenter = new LightSensorPagePresenter(this);
        mButtonClickListener = new ButtonClickListener(mPresenter);

        mCurrentVauleTextView = (TextView) mView.findViewById(R.id.currentVaule);
        mDarkValueTextView = (TextView) mView.findViewById(R.id.detectedDarkValue);
        mLightValueTextView = (TextView) mView.findViewById(R.id.detectedLightValue);
        mSubmit = (Button) mView.findViewById(R.id.submit);
        mSubmit.setOnClickListener(mButtonClickListener);

        return mView;
    }

    public void setCurrentValue(float value) {
        String valueStr = String.valueOf(value);
        mCurrentVauleTextView.setText(valueStr);
    }

    public void setDarkTestResult(String result) {
        mDarkValueTextView.setText(result);
    }

    public void setLightTestResult(String result) {
        mLightValueTextView.setText(result);
    }

    public void setButton(boolean clickable) {
        mSubmit.setClickable(clickable);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.pause();
    }
}
