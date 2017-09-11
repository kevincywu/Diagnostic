package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.RuninEndPagePresenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RuninEndTestCaseView extends AbstractTestCaseView {

    private Activity mLevelPanel;
    private View mView;

    private TextView mLevelValueView;
    private TextView mLevelResultView;
    private TextView mWarningPrompt;
    private RuninEndPagePresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.runin_end_testcase, container, false);
        mLevelPanel = (AbstractLevelPanelView) getActivity();

        mLevelValueView = (TextView) mView.findViewById(R.id.levelValueView);
        mLevelResultView = (TextView) mView.findViewById(R.id.levelResultView);

        mWarningPrompt = (TextView) mView.findViewById(R.id.warningPromptView);
        mPresenter = new RuninEndPagePresenter(this);

        return mView;
    }

    public Context getAppContext() {
        return mLevelPanel.getApplicationContext();
    }

    public void setLevelValueViewText(String str){
        mLevelValueView.setText(str);
    }
    public void setLevelValueViewTextColor(int color){
        mLevelValueView.setTextColor(color);
    }

    public void setLevelResultViewText(String str){
        mLevelResultView.setText(str);
    }

    public void setWarningPromptText(int resId){
        mWarningPrompt.setText(resId);
    }

    public void setWarningPromptText(String str){
        mWarningPrompt.setText(str);
    }

    public void setWarningPromptVisibility(int visibility){
        mWarningPrompt.setVisibility(visibility);
    }

    public void setWarningPromptTextColor(int color){
        mWarningPrompt.setTextColor(color);
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
