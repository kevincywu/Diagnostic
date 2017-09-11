package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.IDiagnosticApp;
import android.content.Context;

public class MockHDMIPanelView extends HDMITestCaseView{

    private IDiagnosticApp mApp;
    private String mTextViewInfo = null;

    public MockHDMIPanelView(IDiagnosticApp app) {
        mApp = app;
    }

    public String getTextView() {
        return mTextViewInfo;
    }

    public Context getContext() {
        return mApp.getAppContext();
    }

    @Override
    public void setTextView(int key) {
        mTextViewInfo = getContext().getString(key);
    }

    @Override
    public void onPause() {
        return;
    }

    @Override
    public void onResume() {
        return;
    }

    @Override
    public String getLevelName() {
        return null;
    }

    @Override
    public IDiagnosticApp getDiagnosticApp() {
        return mApp;
    }

    public void finishTestCase() {
        return;
    }

}
