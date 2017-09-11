/**
 * RuninWlanTestActivity.java
 * 
 * Written by : River Fan Written for: N0J1 MES Date : April 14, 2012 Version :
 * 1.0
 */

package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.RuninWlanPagePresenter;

import android.app.Activity;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * this class is used for N0J1 runin wlan test , it will download a file from
 * server through wlan
 * 
 * @author river, jim.
 * @version 1.1
 */
public class RuninWlanTestCaseView extends AbstractTestCaseView {

    private Activity mLevelPanel;
    private View mView;

    private TextView mWifiState;
    private TextView mApInfo;
    private TextView mRssiInfo;
    private TextView mResultInfo;
    private Button mFinishButton;

    public static final String TAG = "RininWlan";
    public static final boolean LOCAL_LOGD = true;
    public final String SETTING_INFO = "setInfo";

    private RuninWlanPagePresenter mRuninWlanPagePresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mLevelPanel = getActivity();
        mView = inflater
                .inflate(R.layout.runin_wlan_testcase, container, false);

        mWifiState = (TextView) mView.findViewById(R.id.textView1);
        mApInfo = (TextView) mView.findViewById(R.id.textView2);
        mRssiInfo = (TextView) mView.findViewById(R.id.textView3);
        mResultInfo = (TextView) mView.findViewById(R.id.textView4);
        mWifiState.setTextColor(Color.GREEN);
        mFinishButton = (Button) mView.findViewById(R.id.button1);
        mFinishButton.setVisibility(View.GONE);

        mRuninWlanPagePresenter = new RuninWlanPagePresenter(this);
        mFinishButton.setOnClickListener(new ButtonClickListener(
                mRuninWlanPagePresenter));

        return mView;
    }

    public void setWifiStateInfo(String str) {
        mWifiState.setText(str);
    }

    public void setWifiStateInfo(int resId) {
        mWifiState.setText(resId);
    }

    public void setApInfo(String str) {
        mApInfo.setText(str);
    }

    public void setRssiInfo(String str) {
        mRssiInfo.setText(str);
    }

    public void setResultInfo(String str) {
        mResultInfo.setText(str);
    }

    public void setApInfoTextColor(int color) {
        mApInfo.setTextColor(color);
    }

    public void setRssiInfoTextColor(int color) {
        mRssiInfo.setTextColor(color);
    }

    public void setFinishButton(int visibility) {
        mFinishButton.setVisibility(visibility);
    }

    public XmlResourceParser getXmlResourceParser(int resourceId) {
        return mLevelPanel.getApplicationContext().getResources().getXml(resourceId);
    }

    public Context getAppContext() {
        return mLevelPanel.getApplicationContext();
    }

    @Override
    public void onPause() {
        super.onPause();
        mRuninWlanPagePresenter.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mRuninWlanPagePresenter.resume();
    }
}
