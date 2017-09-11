package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockAssy3gModulePanelView extends Assy3gModuleTestCaseView {
    private IDiagnosticApp mApp;

    public MockAssy3gModulePanelView(IDiagnosticApp app) {
        mApp = app;
    }

    @Override
    public void setStatusText(String status_text) {
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
