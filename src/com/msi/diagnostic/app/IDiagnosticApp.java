package com.msi.diagnostic.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.msi.diagnostic.data.DeviceConfig;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.IVerifierVisitor;

public interface IDiagnosticApp {
    public Context getAppContext();

    public DeviceConfig getDeviceConfig();

    public IDiagnoseModel getDiagnoseModel();

    public IVerifierVisitor getVerifierVisitor();

    public SharedPreferences getPrivatePreferences();

    public DiagnosticSensorManager getSensorManager();
}
