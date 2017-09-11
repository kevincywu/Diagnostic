package com.msi.diagnostic.ui;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockRuninWlanTestCaseView extends RuninWlanTestCaseView {
    private IDiagnosticApp mApp;

    public MockRuninWlanTestCaseView(IDiagnosticApp mApp) {
        super();
        this.mApp = mApp;
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

    @Override
    public void setWifiStateInfo(String str) {

    }

    @Override
    public void setWifiStateInfo(int resId) {

    }

    @Override
    public void setApInfo(String str) {

    }

    @Override
    public void setRssiInfo(String str) {
    }

    @Override
    public void setResultInfo(String str) {
    }

    @Override
    public void setApInfoTextColor(int color) {
    }

    @Override
    public void setRssiInfoTextColor(int color) {

    }

    @Override
    public void setFinishButton(int visibility) {
    }

    @Override
    public XmlResourceParser getXmlResourceParser(int resourceId) {
        return null;
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

}
