package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockLcdTestCaseView extends LcdTestCaseView {

    private IDiagnosticApp mApp;

    public MockLcdTestCaseView(IDiagnosticApp app) {
        mApp = app;
    }

    @Override
    public void setBackground(int colorIndex) {
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
    public void finishTestCase() {
    }
}
