package com.msi.diagnostic.ui;

import android.content.Context;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockAudioFrontMicTestCaseView extends AudioFrontMicTestCaseView {

    private IDiagnosticApp mApp;
    private String mMessage = null;

    public MockAudioFrontMicTestCaseView(IDiagnosticApp mApp) {
        this.mApp = mApp;
    }

    @Override
    public void finishTestCase() {

    }

    public IDiagnosticApp getmApp() {
        return mApp;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    @Override
    public void setmMessageViewText(int mMessage) {
    }

    public Context getContext() {
        return mApp.getAppContext();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public IDiagnosticApp getDiagnosticApp() {
        return (IDiagnosticApp)mApp;
    }

    @Override
    public String getLevelName() {
        return null;
    }

}
