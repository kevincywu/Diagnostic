
package com.msi.diagnostic.ui;

import android.os.Bundle;
import android.util.Log;

import com.msi.diagnostic.app.IDiagnosticApp;

public class MockBatteryTestCaseViewN728 extends BatteryTestCaseViewN728
{
    private String mHealthTestResult;
    private String mLevelTestResult;
    private String mVoltageTestResult;
    private String mTemperatureTestResult;
    private String mCurrentTestResult;
    private IDiagnosticApp mApp;

    public MockBatteryTestCaseViewN728(IDiagnosticApp app) {
        mApp = app;
    }

    @Override
    public String getLevelName()
    {
        Log.i("TAG_BatteryPanelTester", "enter the getLevelName");
        return null;

    }

    @Override
    public void onCreate(Bundle state)
    {
        super.onCreate(state);
    }

    @Override
    public void onResume()
    {
        return;
    }

    @Override
    public void onPause()
    {
        return;
    }

    @Override
    public IDiagnosticApp getDiagnosticApp()
    {
        return (IDiagnosticApp) mApp;
    }

    @Override
    public void setTimePromptColor(int color)
    {
        return;
    }

    @Override
    public void setWarningPromptColor(int color)
    {
        return;
    }

    @Override
    public void setStatusValue(int resId)
    {

    }

    @Override
    public void setPluggedState(int resId)
    {

    }

    @Override
    public void setWarningPromptVisibility(int visibility)
    {

    }

    @Override
    public void setHealthTestResult(String result)
    {
        Log.i("TAG_BatteryHealthyTester", "result:" + result);
        mHealthTestResult = result;
    }

    @Override
    public void setHealthState(int resId)
    {

    }

    @Override
    public void setTemperatureValue(float value)
    {

    }

    @Override
    public void setTemperatureTestResult(String result)
    {
        Log.i("TAG_BatteryTemperatureTester", "result:" + result);
        mTemperatureTestResult = result;
    }

    @Override
    public void setCurrentValueNull(int value)
    {
        mCurrentTestResult = Integer.toString(value);
    }

    @Override
    public void setLevelValue(int value)
    {

    }

    @Override
    public void setLevelTestResult(String result)
    {
        Log.i("TAG_BatteryLevelTester", "result:" + result);
        mLevelTestResult = result;
    }

    @Override
    public void setVoltageValue(float value)
    {

    }

    @Override
    public void setVoltageTestResult(String result)
    {
        Log.i("TAG_BatteryVoltageTester", "result:" + result);
        mVoltageTestResult = result;
    }

    @Override
    public void setCurrentValue(int value)
    {

    }

    @Override
    public void setCurrentTestResult(String result)
    {
        Log.i("TAG_BatteryCurrentTester", "result:" + result);
        mCurrentTestResult = result;
    }

    public String getHealthTestResult()
    {
        return mHealthTestResult;
    }

    public String getLevelTestResult()
    {
        return mLevelTestResult;
    }

    public String getVoltageTestResult()
    {
        return mVoltageTestResult;
    }

    public String getTemperatureTestResult()
    {
        return mTemperatureTestResult;
    }

    public String getCurrentTestResult()
    {
        return mCurrentTestResult;
    }

}
