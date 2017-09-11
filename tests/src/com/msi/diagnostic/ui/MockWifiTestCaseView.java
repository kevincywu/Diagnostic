package com.msi.diagnostic.ui;

import android.content.Context;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockWifiTestCaseView extends WifiTestCaseView {


    private IDiagnosticApp mApp;

    public MockWifiTestCaseView(IDiagnosticApp mApp) {
        super();
        this.mApp = mApp;
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

    @Override
    public Context getAppContext() {
        return mApp.getAppContext();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }
}
