package com.msi.diagnostic.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.SDcardPagePresenter;

public class SDcardTestCaseView extends AbstractTestCaseView {

    private Activity mLevelPanel;
    private View mView;

    public TextView mTextView;
    private Button mTestButton, mSkipButton;

    private SDcardPagePresenter mPresenter;
    private ButtonClickListener mButtonClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mLevelPanel = getActivity();
        mView = inflater.inflate(R.layout.sdcard_testcase, container, false);

        mPresenter = new SDcardPagePresenter(this);
        mButtonClickListener = new ButtonClickListener(mPresenter);

        mSkipButton = (Button) mView.findViewById(R.id.skip_button);
        mSkipButton.setOnClickListener(mButtonClickListener);

        mTextView = (TextView) mView.findViewById(R.id.textview);
        mTextView.setText(R.string.note_insert);

        return mView;
    }

    public void setSdText(String mText) {
        mTextView.setText(mText);
    }

    public void setSdText(int mText) {
        mTextView.setText(mText);
    }

    public String getSdViewString(int resId) {
        return getString(resId);
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
