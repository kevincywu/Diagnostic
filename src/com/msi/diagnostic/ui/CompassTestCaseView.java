package com.msi.diagnostic.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.CompassPagePresenter;
import com.msi.diagnostic.app.CompassView;
import com.msi.diagnostic.app.SensorEventInfo;

public class CompassTestCaseView extends AbstractTestCaseView {

    private View mView;

    private Button mPassBtn;
    private Button mFailBtn;
    private CompassPagePresenter mPresenter;
    private ButtonClickListener mButtonClickListener;
    public CompassView mCompassView;
    public TextView mInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.compass_testcase, container, false);

        mPresenter = new CompassPagePresenter(this);
        mButtonClickListener = new ButtonClickListener(mPresenter);

        mPassBtn = (Button) mView.findViewById(R.id.pass);
        mPassBtn.setOnClickListener(mButtonClickListener);

        mFailBtn = (Button) mView.findViewById(R.id.fail);
        mFailBtn.setOnClickListener(mButtonClickListener);

        mInfo = (TextView) mView.findViewById(R.id.compassinfo);

        mCompassView = (CompassView) mView.findViewById(R.id.mycompassview);

        return mView;
    }

    public void setNotCompassInfo() {
        mInfo.setText(R.string.nocompass);
    }

    public void setCompassInfo() {
        mInfo.setText(R.string.ecompass);
    }

    public void updateCompassDirection(SensorEventInfo sensorEventInfo) {
        mCompassView.updateDirection(sensorEventInfo);
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
