package com.msi.diagnostic.ui;

import android.os.Bundle;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockSpecialBatteryTestCaseView extends SpecialBatteryTestCaseView {
    private String mCurrentTestResult;
    private IDiagnosticApp mApp;

    public MockSpecialBatteryTestCaseView(IDiagnosticApp app) {
        mApp = app;
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
    }

    @Override
    public void onPause() {
    }

    @Override
    public IDiagnosticApp getDiagnosticApp() {
        return (IDiagnosticApp) mApp;
    }

    @Override
    public void setCurrentValue(int value) {
    }

    @Override
    public void setCurrentValueNull(int value) {
        mCurrentTestResult = Integer.toString(value);
    }

    @Override
    public void setCurrentTestResult(String result) {
        mCurrentTestResult = result;
    }

    @Override
    public void setTimePromptVisibility(int visibility) {
    }

    @Override
    public void setTimePromptColor(int color) {
    }

    @Override
    public String getLevelName() {
        return null;
    }

    @Override
    public void finishTestCase() {
    }

    @Override
    public void setTimePrompt(float time) {
    }

    @Override
    public void startActivity(String dragonfirePackage, String dragonfireClass) {
    }

    @Override
    public void setSubmitButtonVisibility(int visibility) {
    }

    public String getCurrentTestResult() {
        return mCurrentTestResult;
    }
}
