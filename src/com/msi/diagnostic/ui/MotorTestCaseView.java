package com.msi.diagnostic.ui;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.MotorPagePresenter;

public class MotorTestCaseView extends AbstractTestCaseView {

    private Activity mLevelPanel;
    private View mView;

    private Button mVib_PassBtn;
    private Button mVib_FailBtn;
    private MotorPagePresenter mPresenter;
    private ButtonClickListener mButtonClickListener;

    public Vibrator mVibrator;
    public ToggleButton mToggleBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mLevelPanel = getActivity();
        mView = inflater.inflate(R.layout.motor_testcase, container, false);

        mPresenter = new MotorPagePresenter(this);
        mButtonClickListener = new ButtonClickListener(mPresenter);

        mToggleBtn = (ToggleButton) mView.findViewById(R.id.toggle);
        mToggleBtn.setOnClickListener(mButtonClickListener);

        mVib_PassBtn = (Button) mView.findViewById(R.id.pass);
        mVib_PassBtn.setOnClickListener(mButtonClickListener);

        mVib_FailBtn = (Button) mView.findViewById(R.id.fail);
        mVib_FailBtn.setOnClickListener(mButtonClickListener);

        mVibrator = (Vibrator) mLevelPanel.getApplication().getSystemService(
                Service.VIBRATOR_SERVICE);

        return mView;
    }

    public void setVibrate() {
        mVibrator.vibrate(new long[] {
                100, 100, 100, 1000
        }, 0);
        Toast.makeText(mLevelPanel, getString(R.string.motor_str_ok),
                Toast.LENGTH_SHORT).show();
    }

    public void cancelVibrate() {
        mVibrator.cancel();
        Toast.makeText(mLevelPanel, getString(R.string.motor_str_end),
                Toast.LENGTH_SHORT).show();
    }

    public boolean getToggleButtonStatus() {
        return mToggleBtn.isChecked();
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
