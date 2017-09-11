
package com.msi.diagnostic.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.msi.diagnostic.app.IDiagnosticApp;

public class MockCameraTestCaseView extends CameraTestCaseView
{
    private IDiagnosticApp mApp;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    public MockCameraTestCaseView(IDiagnosticApp mApp) {
        super();
        this.mApp = mApp;
        mSurfaceView = new SurfaceView(mApp.getAppContext());
        mSurfaceHolder = mSurfaceView.getHolder();
    }

    @Override
    public String getLevelName()
    {
        return null;
    }

    @Override
    public IDiagnosticApp getDiagnosticApp()
    {
        return (IDiagnosticApp) mApp;
    }

    @Override
    public void finishTestCase()
    {

    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public SurfaceHolder getSurfaceHolder()
    {
        return mSurfaceHolder;
    }

    @Override
    public Camera getCamera()
    {
        return null;
    }

    @Override
    public void setTakePicText(int textRes)
    {

    }

    @Override
    public void setTakePicVisible()
    {

    }

    @Override
    public void setTakePicInvisible()
    {

    }

    @Override
    public void setTakePicClickable(boolean clickable)
    {

    }

    @Override
    public void setPassClickable(boolean clickable)
    {

    }

    @Override
    public void setFailClickable(boolean clickable)
    {

    }

    @Override
    public void setCameraSwitchVisible()
    {

    }

    @Override
    public void setCameraSwitchInvisible()
    {

    }

    @Override
    public void setCameraSwitchClickable(boolean clickable)
    {

    }

    @Override
    public void setPassVisibility(int visiable)
    {

    }

    @Override
    public void toastShowMessage(int message)
    {

    }

    @Override
    public void onAttach(Activity activity)
    {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {

    }

    @Override
    public void setImageViewImageBitmap(Bitmap bmp)
    {

    }

    @Override
    public void setWarningText(int textRes)
    {

    }

}
