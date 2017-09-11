package com.msi.diagnostic.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.PhoneCallPagePresenter;

public class PhoneCallTestView extends AbstractTestCaseView {
    private Button mCallButton;
    private Button mPassButton;
    private Button mFailButton;
    private View mView;
    private PhoneCallPagePresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.phone_call_testcase, container, false);
        mCallButton = (Button) mView.findViewById(R.id.phone_call_call_button);
        mPassButton = (Button) mView.findViewById(R.id.phone_call_pass);
        mFailButton = (Button) mView.findViewById(R.id.phone_call_fail);

        mPresenter = new PhoneCallPagePresenter(this);

        mCallButton.setOnClickListener(new ButtonClickListener(mPresenter));
        mPassButton.setOnClickListener(new ButtonClickListener(mPresenter));
        mFailButton.setOnClickListener(new ButtonClickListener(mPresenter));
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
}
