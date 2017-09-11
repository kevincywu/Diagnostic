package com.msi.diagnostic.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.msi.diagnostic.data.DeviceConfig;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.IVerifierVisitor;
import com.msi.diagnostic.data.MockDeviceConfig;
import com.msi.diagnostic.data.MockDiagnoseModel;
import com.msi.diagnostic.data.VerifierVisitorImp;

public class MockDiagnosticApp implements IDiagnosticApp {

    private static final String DIAG_PRIVATE_PREFERENCE = "diagnostic_preference";

    private Context mContext;

    private static DeviceConfig mDeviceConfig = null;

    private static MockDiagnosticSensorManager mSensorManager = null;

    private static HardwareSensorManager mHardwareSensorManager = null;

    private MockDiagnoseModel mModel = new MockDiagnoseModel();

    public MockDiagnosticApp(Context context) {
        mContext = context;
    }

    @Override
    public Context getAppContext() {
        return mContext;
    }

    @Override
    public DeviceConfig getDeviceConfig() {
        if (mDeviceConfig == null) {
            mDeviceConfig = new MockDeviceConfig(getAppContext());
        }
        return mDeviceConfig;
    }

    @Override
    public IDiagnoseModel getDiagnoseModel() {
        return mModel;
    }

    @Override
    public IVerifierVisitor getVerifierVisitor() {
        return VerifierVisitorImp.getInstance();
    }

    @Override
    public SharedPreferences getPrivatePreferences() {
        return mContext.getSharedPreferences(DIAG_PRIVATE_PREFERENCE, mContext.MODE_PRIVATE);
    }

    @Override
    public DiagnosticSensorManager getSensorManager() {

        IPatformSensorManager mPatformSensorManager = null;

        if (mHardwareSensorManager == null)
            mPatformSensorManager = new MockHardwareSensorManager(this.mContext);

        if (mSensorManager == null) {
            mSensorManager = new MockDiagnosticSensorManager(mPatformSensorManager);
            mPatformSensorManager.setSensorListener(mSensorManager);
        }

        return mSensorManager;

    }

}
