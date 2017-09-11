
package com.msi.diagnostic.ui;

import android.content.Context;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockSDcardPanelView extends SDcardTestCaseView
{
    private IDiagnosticApp mApp;

    Context context;

    public MockSDcardPanelView(IDiagnosticApp app) {
        mApp = app;
        context = mApp.getAppContext();
    }

    @Override
    public String getSdViewString(int resId)
    {
        return context.getString(resId);
    }

    @Override
    public void setSdText(String mText)
    {
        return;
    }

    @Override
    public void setSdText(int mText) {
        return;
    }

    @Override
    public String getLevelName()
    {
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
