package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockBacklightTestCaseView extends BacklightTestCaseView {

    private IDiagnosticApp mApp;

    public MockBacklightTestCaseView(IDiagnosticApp app) {
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
    public void setBacklightBrightness(float value) {
    }

    @Override
    public void finishTestCase() {
    }
}
