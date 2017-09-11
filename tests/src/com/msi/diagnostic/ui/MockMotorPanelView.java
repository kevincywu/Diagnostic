
package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.IDiagnosticApp;
import com.msi.diagnostic.ui.MotorTestCaseView;

public class MockMotorPanelView extends MotorTestCaseView
{

    private IDiagnosticApp mApp;

    public MockMotorPanelView(IDiagnosticApp app) {
        mApp = app;
    }

    @Override
    public void finishTestCase() {

    }

    @Override
    public String getLevelName()
    {
        return null;
    }

    @Override
    public void setVibrate()
    {
    }

    @Override
    public void cancelVibrate()
    {
    }

    @Override
    public boolean getToggleButtonStatus()
    {
        return true;
    }

    @Override
    public IDiagnosticApp getDiagnosticApp()
    {
        return mApp;
    }

}
