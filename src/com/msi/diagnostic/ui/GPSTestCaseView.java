package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.GPSPagePresenter;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class GPSTestCaseView extends AbstractTestCaseView
        implements IGPSTestCaseView {

    private Button mFailButton;
    private TextView mCNText;
    private TextView mRemainText;
    private TextView mCounDownText;
    private TextView mSecond;
    private View mView;

    private GPSPagePresenter mPresenter;
    private ButtonClickListener mButtonClickListener;
    private LocationManager mLocationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.gps_testcase, container, false);
        mFailButton = (Button) mView.findViewById(R.id.failbutton);
        mCNText = (TextView) mView.findViewById(R.id.cn_value);
        mRemainText = (TextView) mView.findViewById(R.id.seconds_remaining_text);
        mCounDownText = (TextView) mView.findViewById(R.id.count_down_second);
        mSecond = (TextView) mView.findViewById(R.id.second_text);

        mLocationManager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        mPresenter = new GPSPagePresenter(this, mLocationManager);
        mButtonClickListener = new ButtonClickListener(mPresenter);
        mFailButton.setOnClickListener(new ButtonClickListener(mPresenter));
        String settingSecureString = Settings.Secure.getString(
                getActivity().getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        mPresenter.setSettingSecureString(settingSecureString);
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

    @Override
    public void showCN(String CN_Str) {
        mCNText.setText(CN_Str);
    }

    @Override
    public void setCNTextColor(int color) {
        mCNText.setTextColor(color);
    }

    @Override
    public void setFailButtonVisiable(int visiable_code) {
        mFailButton.setVisibility(visiable_code);
    }

    @Override
    public void setCountDownVisiable(int visiable_code) {
        mRemainText.setVisibility(visiable_code);
        mCounDownText.setVisibility(visiable_code);
        mSecond.setVisibility(visiable_code);
    }

    @Override
    public void setCountDownText(String count_down_text) {
        mCounDownText.setText(count_down_text);
    }
}
