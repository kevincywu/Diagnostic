package com.msi.diagnostic.ui;

import android.widget.MediaController;
import android.widget.VideoView;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockRuninVideoTestCaseView extends RuninVideoTestCaseView {
    private IDiagnosticApp mApp;

    public MockRuninVideoTestCaseView(IDiagnosticApp mApp) {
        super();
        this.mApp = mApp;
    }

    @Override
    public void videoViewStart() {

    }

    @Override
    public void videoViewRequestFocus() {

    }

    @Override
    public void setmShowTimeViewText(String str) {

    }

    @Override
    public VideoView getmVideoView() {
        return null;
    }

    @Override
    public void setmMediaController() {

    }

    @Override
    public MediaController getmMediaController() {

        return super.getmMediaController();
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
