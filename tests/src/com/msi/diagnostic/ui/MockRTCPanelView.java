package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockRTCPanelView extends RTCTestCaseView{


    private IDiagnosticApp mApp;

    public MockRTCPanelView(IDiagnosticApp app) {
        super();
        mApp = app;
    }

    @Override
    public IDiagnosticApp getDiagnosticApp() {
        return mApp;
    }

    public String getLevelName() {
        return null;
    }

    @Override
    public void finishTestCase() {
    }

}
