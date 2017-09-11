package com.msi.diagnostic.ui;

import android.content.Context;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockGSensorTestCaseView extends GSensorTestCaseView {
    private IDiagnosticApp mApp;

    private String mTestLevelName;

    private Context context;

    public MockGSensorTestCaseView(IDiagnosticApp app) {
        mApp = app;
        context = mApp.getAppContext();
    }

    public void setLevelName(String mName) {
        mTestLevelName = mName;
    }

    @Override
    public String getLevelName() {
        return mTestLevelName;
    }

    @Override
    public void setTextX(float value) {
    }

    @Override
    public void setTextY(float value) {
    }

    @Override
    public void setTextZ(float value) {
    }

    @Override
    public IDiagnosticApp getDiagnosticApp() {
        return mApp;
    }

}
