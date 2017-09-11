package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockGPSPanelView extends GPSTestCaseView {

    private IDiagnosticApp mApp;

    public MockGPSPanelView(IDiagnosticApp app) {
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
    public void showCN(String CN_Str) {
    }

    @Override
    public void setCNTextColor(int color) {
    }

    @Override
    public void setFailButtonVisiable(int visiable_code) {
    }

    @Override
    public void setCountDownVisiable(int visiable_code) {
    }

    @Override
    public void setCountDownText(String count_down_text) {
    }
}
