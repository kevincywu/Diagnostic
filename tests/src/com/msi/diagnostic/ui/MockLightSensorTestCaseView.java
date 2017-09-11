package com.msi.diagnostic.ui;

import android.content.Context;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockLightSensorTestCaseView extends LightSensorTestCaseView {
    private IDiagnosticApp mApp;

    private String mTestLevelName;

    Context context;

    public MockLightSensorTestCaseView(IDiagnosticApp app) {
        mApp = app;
        context = mApp.getAppContext();
    }

    public void setCurrentValue(float value) {

    }

    public void setButton(boolean clickable) {

    }

    public void setLevelName(String mName) {
        mTestLevelName = mName;
    }

    @Override
    public String getLevelName() {
        return mTestLevelName;
    }

    @Override
    public void finishTestCase() {

    }

    @Override
    public IDiagnosticApp getDiagnosticApp() {
        return mApp;
    }
}
