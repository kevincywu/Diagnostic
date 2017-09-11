
package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.SpecialBatteryPagePresenter;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

public class SpecialBatteryTestCaseView extends AbstractTestCaseView {

    private View mView;
    private SpecialBatteryPagePresenter mPresenter;

    private ButtonClickListener mButtonClickListener;

    private TextView mCurrentValueView;
    private TextView mCurrentTestResultView;

    private TextView mTimePrompt;
    private TextView mTimePromptBase;
    private Button mSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.special_battery_testcase, container, false);

        mCurrentValueView = (TextView) mView.findViewById(R.id.currentValue);
        mCurrentTestResultView = (TextView) mView.findViewById(R.id.currentTestResult);

        mTimePrompt = (TextView) mView.findViewById(R.id.timePrompt);
        mTimePromptBase = (TextView) mView.findViewById(R.id.timePromptBase);

        mSubmit = (Button) mView.findViewById(R.id.submit);

        mPresenter = new SpecialBatteryPagePresenter(this);
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

    public void setSubmitButtonVisibility(int visibility) {
        mSubmit.setVisibility(visibility);
    }

    public void startActivity(String dragonfirePackage, String dragonfireClass) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(
                dragonfirePackage,dragonfirePackage + dragonfireClass));
        startActivity(intent);
    }
    /** A broadcast to obtain the battery information */
    private BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            mPresenter.onBatteryStatusChanged(context, intent);
        }

    };

}
