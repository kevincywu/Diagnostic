package com.msi.diagnostic.ui;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.Assy3gModulePagePresenter;

public class Assy3gModuleTestCaseView extends AbstractTestCaseView implements
        IAssy3gModuleTestCaseView {

    private Activity mLevelPanel;
    private View mView;

    private TextView mStatusText;
    private Assy3gModulePagePresenter mPresenter;
    private WifiManager mWifiManager;
    private TelephonyManager mTelephonyManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mLevelPanel = getActivity();
        mView = inflater.inflate(R.layout.assy_3g_module_testcase, container, false);

        mStatusText = (TextView) mView.findViewById(R.id.text);

        mWifiManager = (WifiManager)
                mLevelPanel.getSystemService(Context.WIFI_SERVICE);
        mTelephonyManager = (TelephonyManager)
                mLevelPanel.getSystemService(Context.TELEPHONY_SERVICE);
        mPresenter = new Assy3gModulePagePresenter(this, mWifiManager,
                mTelephonyManager, true, 0);

        return mView;
    }

    @Override
    public void onDestroy() {
        mPresenter.setWifi(true);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.setTelphonyListenNone();
        mPresenter.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.setTelphonyListenSignal();
        mPresenter.resume();
    }

    @Override
    public void setStatusText(String status_text) {
        mStatusText.setText(status_text);
    }
}
