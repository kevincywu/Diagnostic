
package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.IDiagnosticApp;

import android.os.Bundle;

public class MockHotkeyPanel extends HotKeyTestCaseView {

    private String mVolumnUpResultString;
    private String mVolumnDownResultString;

    private IDiagnosticApp mDiagAppStub;
    
    public MockHotkeyPanel(IDiagnosticApp app) {
        mDiagAppStub = app;
    }    

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
    }

    @Override
    public void onResume() {
        return;
    }

    @Override
    public void onPause() {
        return;
    }

    @Override
    public IDiagnosticApp getDiagnosticApp() {
        return (IDiagnosticApp) mDiagAppStub;

    } 
    
    @Override
    public String getLevelName() {
        return null;
    }
    
    @Override
    public void setVolumnUpResult(String testResult) {
        mVolumnUpResultString = testResult;
    }

    @Override
    public void setVolumnDownResult(String testResult) {
        mVolumnDownResultString = testResult;
    }
    @Override
    public void setVolumnUpResultTextColor(int color) {
    }
    @Override
    public void setVolumnDownResultTextColor(int color) {
    }

    public String getVolumeUpResult() {
        return mVolumnUpResultString;
    }
    public String getVolumeDownResult() {
        return mVolumnDownResultString;
    }
    
}