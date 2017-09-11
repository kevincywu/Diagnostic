package com.msi.diagnostic.ui;

import android.content.Context;

import com.msi.diagnostic.app.IDiagnosticApp;
import com.msi.diagnostic.app.SensorEventInfo;

public class MockCompassTestCaseView extends CompassTestCaseView {
    private IDiagnosticApp mApp;

    private MockCompassView mCompassView;

    private Context mContext;

    public MockCompassTestCaseView(IDiagnosticApp app) {
        mApp = app;
        mContext = mApp.getAppContext();
        mCompassView = new MockCompassView(mContext);
    }

    @Override
    public void updateCompassDirection(SensorEventInfo sensorEventInfo) {
        mCompassView.updateDirection(sensorEventInfo);
    }

    @Override
    public void finishTestCase() {

    }

    @Override
    public void setCompassInfo() {
    }

    @Override
    public String getLevelName() {
        return null;
    }

    @Override
    public IDiagnosticApp getDiagnosticApp() {
        return mApp;
    }

    public float getDirection() {
        return mCompassView.mDirection;
    }
}
