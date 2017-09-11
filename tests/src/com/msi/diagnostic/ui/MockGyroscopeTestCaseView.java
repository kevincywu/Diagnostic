package com.msi.diagnostic.ui;

import java.io.IOException;

import android.content.Context;

import com.msi.diagnostic.app.IDiagnosticApp;
import com.msi.diagnostic.utils.Utils;

public class MockGyroscopeTestCaseView extends GyroscopeTestCaseView {
    private IDiagnosticApp mApp;

    private String mGyroscopeSensorName;

    private String mTestLevelName;

    Context context;

    public MockGyroscopeTestCaseView(IDiagnosticApp app) {
        mApp = app;
        context = mApp.getAppContext();
    }

    public void setLevelName(String mName) {
        mTestLevelName = mName;
    }

    @Override
    public String getLevelName() {
        return mTestLevelName;
    }

    @Override
    public String getGyroscopeSensorName() {
        try {
            mGyroscopeSensorName = Utils.readContentFromFile("/ueventd.rc");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mGyroscopeSensorName;
    }

    @Override
    public void finishTestCase() {

    }

    @Override
    public void setTextX(float value) {
    }

    @Override
    public void setTextY(float value) {
    }

    @Override
    public void setTextZ(float value) {
    }

    @Override
    public IDiagnosticApp getDiagnosticApp() {
        return mApp;
    }
}
