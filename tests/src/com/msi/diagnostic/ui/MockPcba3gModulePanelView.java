package com.msi.diagnostic.ui;

import android.content.Context;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockPcba3gModulePanelView extends Pcba3gModuleTestCaseView
{
    private IDiagnosticApp mApp;

    Context context;

    public MockPcba3gModulePanelView(IDiagnosticApp app) {
        mApp = app;
        context = mApp.getAppContext();
    }

    @Override
    public void showResultToast(int resId)
    {
    }

    @Override
    public String getLevelName()
    {
        return null;
    }

    @Override
    public IDiagnosticApp getDiagnosticApp()
    {
        return mApp;
    }

    @Override
    public void finishTestCase() {
    }

}
