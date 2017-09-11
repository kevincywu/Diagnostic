package com.msi.diagnostic.ui;

import android.content.Context;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockRuninEndTestCaseView extends RuninEndTestCaseView {
    private IDiagnosticApp mApp;


    public MockRuninEndTestCaseView(IDiagnosticApp mApp) {
        super();
        this.mApp = mApp;
    }

    @Override
    public Context getAppContext() {
        return mApp.getAppContext();
    }

    @Override
    public void setLevelValueViewText(String str) {

    }

    @Override
    public void setLevelValueViewTextColor(int color) {

    }

    @Override
    public void setLevelResultViewText(String str) {

    }

    @Override
    public void setWarningPromptText(int resId) {

    }

    @Override
    public void setWarningPromptText(String str) {

    }

    @Override
    public void setWarningPromptVisibility(int visibility) {

    }

    @Override
    public void setWarningPromptTextColor(int color) {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public String getLevelName() {
        return null;
    }

    @Override
    public IDiagnosticApp getDiagnosticApp() {

        return (IDiagnosticApp)mApp;
    }

    @Override
    public void finishTestCase() {

    }

}
