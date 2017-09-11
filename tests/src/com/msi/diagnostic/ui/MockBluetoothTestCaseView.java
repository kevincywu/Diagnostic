package com.msi.diagnostic.ui;

import android.content.Context;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockBluetoothTestCaseView extends BluetoothTestCaseView {

    private IDiagnosticApp mApp;

    public MockBluetoothTestCaseView(IDiagnosticApp app) {
        mApp = app;
    }

    @Override
    public Context getAppContext() {
        return mApp.getAppContext();
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
