
package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.BatteryPagePresenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

public class BatteryTestCaseView extends AbstractTestCaseView {

    private View mView;
    private BatteryPagePresenter mPresenter;

    private ButtonClickListener mButtonClickListener;

    private TextView mHealthStateView;
    private TextView mHealthTestResultView;

    private TextView mStatusValueView;
    private TextView mPluggedStateView;

    private TextView mVoltageValueView;
    private TextView mVoltageTestResultView;

    private TextView mLevelValueView;
    private TextView mLevelTestResultView;

    private TextView mCurrentValueView;
    private TextView mCurrentTestResultView;

    private TextView mTemperatureValueView;
    private TextView mTemperatureTestResultView;

    private TextView mTimePrompt;
    private TextView mTimePromptBase;
    private TextView mWarningPrompt;
    private Button mSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.battery_testcase, container, false);

        mHealthStateView = (TextView) mView.findViewById(R.id.healthState);
        mHealthTestResultView = (TextView) mView.findViewById(R.id.healthTestResult);

        mStatusValueView = (TextView) mView.findViewById(R.id.statusValue);
        mPluggedStateView = (TextView) mView.findViewById(R.id.pluggedState);

        mVoltageValueView = (TextView) mView.findViewById(R.id.voltageValue);
        mVoltageTestResultView = (TextView) mView.findViewById(R.id.voltageTestResult);

        mLevelValueView = (TextView) mView.findViewById(R.id.levelValue);
        mLevelTestResultView = (TextView) mView.findViewById(R.id.levelTestResult);

        mCurrentValueView = (TextView) mView.findViewById(R.id.currentValue);
        mCurrentTestResultView = (TextView) mView.findViewById(R.id.currentTestResult);

        mTemperatureValueView = (TextView) mView.findViewById(R.id.temperatureValue);
        mTemperatureTestResultView = (TextView) mView.findViewById(R.id.temperatureTestResult);

        mTimePrompt = (TextView) mView.findViewById(R.id.timePrompt);
        mTimePromptBase = (TextView) mView.findViewById(R.id.timePromptBase);

        mWarningPrompt = (TextView) mView.findViewById(R.id.warningPrompt);
        mSubmit = (Button) mView.findViewById(R.id.submit);

        mPresenter = new BatteryPagePresenter(this);
        mButtonClickListener = new ButtonClickListener(mPresenter);
        mSubmit.setOnClickListener(mButtonClickListener);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // move the register steps of BroadcastReceiver to PanelView
        getActivity().registerReceiver(mBatteryInfoReceiver, mPresenter.mFilter);
        mPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();

        // move the register steps of BroadcastReceiver to PanelView
        getActivity().unregisterReceiver(mBatteryInfoReceiver);
        mPresenter.pause();
    }

    public void setHealthState(int resId) {
        mHealthStateView.setText(resId);
    }

    public void setHealthTestResult(String result) {
        mHealthTestResultView.setText(result);
    }

    public void setStatusValue(int resId) {
        mStatusValueView.setText(resId);
    }

    public void setPluggedState(int resId) {
        mPluggedStateView.setText(resId);
    }

    public void setVoltageValue(float value) {
        String v = String.valueOf(value);
        mVoltageValueView.setText(v);
    }

    public void setVoltageTestResult(String result) {
        mVoltageTestResultView.setText(result);
    }

    public void setLevelValue(int value) {
        String v = String.valueOf(value);
        mLevelValueView.setText(v);
    }

    public void setLevelTestResult(String result) {
        mLevelTestResultView.setText(result);
    }

    public void setCurrentValue(int value) {
        String v = String.valueOf(value);
        mCurrentValueView.setText(v);
    }

    public void setCurrentValueNull(int value) {
        mCurrentValueView.setText(value);
    }

    public void setCurrentTestResult(String result) {
        mCurrentTestResultView.setText(result);
    }

    public void setTemperatureValue(float value) {
        String v = String.valueOf(value);
        mTemperatureValueView.setText(v);
    }

    public void setTemperatureTestResult(String result) {
        mTemperatureTestResultView.setText(result);
    }

    /* Time Prompt */
    public void setTimePromptVisibility(int visibility) {
        mTimePrompt.setVisibility(visibility);
    }

    public void setTimePromptColor(int color) {
        mTimePrompt.setTextColor(color);
        mTimePromptBase.setTextColor(color);
    }

    public void setTimePrompt(float time) {
        String _time = String.valueOf(time);
        mTimePrompt.setText(_time);
    }

    /* Warning Prompt */
    public void setWarningPromptVisibility(int visibility) {
        mWarningPrompt.setVisibility(visibility);
    }

    public void setWarningPromptColor(int color) {
        mWarningPrompt.setTextColor(color);
    }

    public void setWarningPrompt(int resId) {
        mWarningPrompt.setText(resId);
    }

    public void setWarningPrompt(String text) {
        mWarningPrompt.setText(text);
    }

    public void setSubmitButtonVisibility(int visibility) {
        mSubmit.setVisibility(visibility);
    }

    /** A broadcast to obtain the battery information */
    private BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            mPresenter.onBatteryStatusChanged(context, intent);
        }

    };

}
