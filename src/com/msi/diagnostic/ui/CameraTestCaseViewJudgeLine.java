
package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.CameraPagePresenterJudgeLine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CameraTestCaseViewJudgeLine extends AbstractTestCaseView
{

    private Activity mLevelPanel;
    private View mView;
    private Camera mCamera;
    private Button mTakePic, mPass, mFail, mCameraSwitch;
    private ImageView mImageView;
    private TextView mWarningTextView;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private CameraPagePresenterJudgeLine mPresenter;
    private ButtonClickListener mButtonClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        mLevelPanel = getActivity();
        mView = inflater.inflate(R.layout.camera_testcase_judge_line, container, false);

        mWarningTextView = (TextView) mView.findViewById(R.id.warningTextView);
        mImageView = (ImageView) mView.findViewById(R.id.imageView);
        mSurfaceView = (SurfaceView) mView.findViewById(R.id.surfaceView);

        mSurfaceHolder = mSurfaceView.getHolder();
        mTakePic = (Button) mView.findViewById(R.id.takePic);
        mPass = (Button) mView.findViewById(R.id.cameraPass);

        mFail = (Button) mView.findViewById(R.id.cameraFail);
        mCameraSwitch = (Button) mView.findViewById(R.id.cameraSwitch);

        mPresenter = new CameraPagePresenterJudgeLine(this);
        mButtonClickListener = new ButtonClickListener(mPresenter);

        mTakePic.setOnClickListener(mButtonClickListener);
        mPass.setOnClickListener(mButtonClickListener);

        mFail.setOnClickListener(mButtonClickListener);
        mCameraSwitch.setOnClickListener(mButtonClickListener);

        return mView;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mPresenter.pause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPresenter.resume();
    }

    public Camera getCamera()
    {
        return mCamera;
    }

    public void setTakePicText(int textRes)
    {
        mTakePic.setText(textRes);
    }

    public void setTakePicVisible()
    {
        mTakePic.setVisibility(View.VISIBLE);
    }

    public void setTakePicInvisible()
    {
        mTakePic.setVisibility(View.INVISIBLE);
    }

    public void setTakePicClickable(boolean clickable)
    {
        mTakePic.setClickable(clickable);
    }

    public void setPassClickable(boolean clickable)
    {
        mPass.setClickable(clickable);
    }

    public void setFailClickable(boolean clickable)
    {
        mFail.setClickable(clickable);
    }

    public void setCameraSwitchVisible()
    {
        mCameraSwitch.setVisibility(View.VISIBLE);
    }

    public void setCameraSwitchInvisible()
    {
        mCameraSwitch.setVisibility(View.INVISIBLE);
    }

    public void setCameraPassVisible()
    {
        mPass.setVisibility(View.VISIBLE);
    }

    public void setCameraPassInvisible()
    {
        mPass.setVisibility(View.INVISIBLE);
    }

    public void setCameraFailVisible()
    {
        mFail.setVisibility(View.VISIBLE);
    }

    public void setCameraFailInvisible()
    {
        mFail.setVisibility(View.INVISIBLE);
    }

    public void setCameraSwitchClickable(boolean clickable)
    {
        mCameraSwitch.setClickable(clickable);
    }

    public void setImageViewImageBitmap(Bitmap bmp)
    {
        mImageView.setImageBitmap(bmp);
    }

    public void setWarningText(int textRes)
    {
        mWarningTextView.setText(textRes);
    }

    public SurfaceHolder getSurfaceHolder()
    {
        return mSurfaceHolder;
    }

    public int getDisplayRotation()
    {
        return mLevelPanel.getWindowManager().getDefaultDisplay().getRotation();
    }

    public void setPassVisibility(int visiable)
    {
        mPass.setVisibility(visiable);
    }

    public void toastShowMessage(int message)
    {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void setCameraErrorDialog(int id)
    {
        String idString = null;
        if (id == Camera.CameraInfo.CAMERA_FACING_BACK)
            idString = "Back";
        else if (id == Camera.CameraInfo.CAMERA_FACING_FRONT)
            idString = "Rear";
        AlertDialog.Builder builder = new Builder(mView.getContext());
        builder.setTitle("Camera error");
        builder.setMessage("Can not detect " + idString + " camera");
        builder.setPositiveButton("Fail", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                finishTestCase();
            }
        });
        builder.create().show();
    }
}