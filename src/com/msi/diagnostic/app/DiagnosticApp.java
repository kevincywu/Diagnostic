package com.msi.diagnostic.app;

import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DeviceConfig;
import com.msi.diagnostic.data.DiagnoseModel;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.IVerifierVisitor;
import com.msi.diagnostic.data.SQLiteDatabaseSource;
import com.msi.diagnostic.data.VerifierVisitorImp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class DiagnosticApp extends Application implements IDiagnosticApp {

    private static final String DIAG_PRIVATE_PREFERENCE = "diagnostic_preference";

    private static DeviceConfig mDeviceConfig = null;

    private static DiagnoseModel mModel = null;

    private static DiagnosticSensorManager mSensorManager = null;

    private static HardwareSensorManager mHardwareSensorManager = null;

    private static SimulatorSensorManager mSimulatorSensorManager = null;

    @Override
    public DiagnosticSensorManager getSensorManager() {

        String[] supportedDevices = getResources().getStringArray(R.array.supported_devices);
        String targetBoardName = mDeviceConfig.getDeviceName();
        IPatformSensorManager mPatformSensorManager = null;

        // supportedDevices[0] = "generic" <!--The fixed device for testing in the emulator -->
        if (targetBoardName.equals(supportedDevices[0]) && mSimulatorSensorManager == null) {
            mPatformSensorManager = new SimulatorSensorManager(this);
        } else {
            if (mHardwareSensorManager == null)
                mPatformSensorManager = new HardwareSensorManager(this);
        }

        if (mSensorManager == null) {
            mSensorManager = new DiagnosticSensorManager(mPatformSensorManager);
            mPatformSensorManager.setSensorListener(mSensorManager);
        }

        return mSensorManager;
    }

    @Override
    public DeviceConfig getDeviceConfig() {
        if (mDeviceConfig == null) {
            mDeviceConfig = new DeviceConfig(getAppContext());
        }
        return mDeviceConfig;
    }

    @Override
    public synchronized IDiagnoseModel getDiagnoseModel() {
        if (mModel == null) {
            SQLiteDatabaseSource source = new SQLiteDatabaseSource(this);
            mModel = new DiagnoseModel(this, source);
        }
        return mModel;
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @Override
    public synchronized IVerifierVisitor getVerifierVisitor() {
        return VerifierVisitorImp.getInstance();
    }

    @Override
    public SharedPreferences getPrivatePreferences() {
        return getSharedPreferences(DIAG_PRIVATE_PREFERENCE, MODE_PRIVATE);
    }

}
