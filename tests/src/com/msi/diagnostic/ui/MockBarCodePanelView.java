package com.msi.diagnostic.ui;

import android.content.Context;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockBarCodePanelView extends BarCodeTestCaseView {

    private IDiagnosticApp mApp;

    private String mBarCodeNumber;
    private String mStatusText;

    public MockBarCodePanelView(IDiagnosticApp app) {
        mApp = app;
    }

    @Override
    public String getLevelName() {
        return null;
    }

    @Override
    public IDiagnosticApp getDiagnosticApp() {
        return mApp;
    }

    @Override
    public void setBarCodeNumberText(String barCodeNumber) {
        mBarCodeNumber = barCodeNumber;
    }

    @Override
    public String getBarCodeNumberText() {
        return mBarCodeNumber;
    }

    @Override
    public void setStatusText(String text) {
        mStatusText = text;
    }

    @Override
    public void setStatusText(int resId) {
        Context context = mApp.getAppContext();
        mStatusText = context.getString(resId);
    }

    @Override
    public String getStatusText() {
        return mStatusText;
    }

    @Override
    public void finishTestCase() {
    }

}
