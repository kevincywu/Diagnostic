
package com.msi.diagnostic.ui;

import android.content.Context;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockSpeakerFrontTestCaseView extends SpeakerFrontTestCaseView
{
    private IDiagnosticApp mApp;
    private Context context;

    public MockSpeakerFrontTestCaseView(IDiagnosticApp app) {
        mApp = app;
        context = mApp.getAppContext();
    }

    @Override
    public void setTextView(int key)
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
    public void finishTestCase()
    {

    }

}
