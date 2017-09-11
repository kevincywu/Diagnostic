package com.msi.diagnostic.ui;

import android.content.Intent;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockPhoneCallTestView extends PhoneCallTestView {
    private IDiagnosticApp mApp;

    public MockPhoneCallTestView(IDiagnosticApp app) {
        mApp = app;
    }

    @Override
    public String getLevelName() {
        return "com.msi.diagnostic.ui.ASSYPanelView";
    }

    @Override
    public IDiagnosticApp getDiagnosticApp() {
        return (IDiagnosticApp) mApp;
    }

    @Override
    public void startActivity(Intent intent) {
    }

    @Override
    public void finishTestCase() {
    }
}
