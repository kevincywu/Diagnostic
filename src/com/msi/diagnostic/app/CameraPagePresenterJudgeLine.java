
package com.msi.diagnostic.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.data.ThresholdVerifiedInfo;
import com.msi.diagnostic.ui.CameraTestCaseViewJudgeLine;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class CameraPagePresenterJudgeLine extends AbstractTestCasePresenter implements
        SurfaceHolder.Callback
{

    private static final String TAG = "Camera";
    private static final boolean LOCAL_LOGD = true;
    private boolean mIsPreview = false;
    private int mNum;
    private int mResult;
    private boolean mIsCameraBackTakePicture = false;
    private boolean mIsCameraRearTakePicture = false;
    private int mChageTime;
    private ThresholdVerifiedInfo mThresholdInfo;
    private static final int RESULT_PASS = TestResult.PASS;
    private static final int RESULT_FAIL = TestResult.FAIL;
    private CameraTestCaseViewJudgeLine mView;
    private Camera mCamera;
    private String mCaptureFilePath = null;
    private static class TestItemId
    {
        public static final int CameraBackCaptured = 0;
        public static final int CameraFrontCaptured = 1;
    }

    private static final String[] TEST_ITEM_NAME = {
            "isCamera1Captured", "isCamera2Captured"
    };

    public CameraPagePresenterJudgeLine(CameraTestCaseViewJudgeLine view) {
        super(view);
        mView = view;
        this.mCamera = mView.getCamera();
        this.mCaptureFilePath = Environment.getExternalStorageDirectory() + "/cameratest.jpg";
        this.mNum = Camera.CameraInfo.CAMERA_FACING_BACK;
        mView.getSurfaceHolder().addCallback(CameraPagePresenterJudgeLine.this);
        mIsCameraBackTakePicture = false;
        mIsCameraRearTakePicture = false;
    }

    private TestResult isCaptured(int parameter, boolean isCaptured)
    {

        String itemName = TEST_ITEM_NAME[parameter];
        TestItem item = getTestItemByName(itemName);
        TestResult result = item.verify(new DetectionVerifiedInfo(isCaptured));
        return result;

    }

    private String getmCaptureFilePath()
    {

        return mCaptureFilePath;

    }

    @Override
    public void OnButtonClick(int buttonId)
    {

        switch (buttonId) {

            case R.id.takePic:

                /** A method is used to take picture */
                if (checkSDCard()) {
                    mView.setTakePicInvisible();
                    takePicture();
                    if (mNum == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        mIsCameraBackTakePicture = true;
                    } else if (mNum == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        mIsCameraRearTakePicture = true;
                    }
                    mView.setCameraSwitchInvisible();
                } else {
                    Log.e(TAG, "no sdcard");
                    mView.setWarningText(R.string.camera_str_err_nosd);
                }
                mView.toastShowMessage(R.string.camera_str_warn_judge);
                mView.setCameraSwitchClickable(false);
                CountDownTimerPlay play = new CountDownTimerPlay(2000, 1000);
                play.start();

                break;

            case R.id.cameraPass:

                /** A method for write camera pass into log */
                // mView.toastShowMessageString(Integer.toString(chageTime));
                try {
                    mView.setFailClickable(false);
                    mView.setPassClickable(false);
                    if (mNum == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        if (mIsCameraBackTakePicture) {
                            isCaptured(TestItemId.CameraBackCaptured, true);
                            mView.setCameraSwitchVisible();
                            mView.toastShowMessage(R.string.camera_back_pass);
                        } else {
                            mView.toastShowMessage(R.string.camera_str_warn_takepic);
                            if (LOCAL_LOGD) {
                                Log.i(TAG, "Take picture by Rear first");
                            }
                        }
                    } else if (mNum == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        if (mIsCameraRearTakePicture) {
                            isCaptured(TestItemId.CameraFrontCaptured, true);
                            mView.setCameraSwitchVisible();
                            mView.toastShowMessage(R.string.camera_front_pass);
                        } else {
                            mView.toastShowMessage(R.string.camera_str_warn_takepic);
                            if (LOCAL_LOGD) {
                                Log.i(TAG, "Take picture by Front first");
                            }
                        }
                    }
                    String backCameraTestItem = TEST_ITEM_NAME[TestItemId.CameraBackCaptured];
                    String frontCameraTestItem = TEST_ITEM_NAME[TestItemId.CameraFrontCaptured];
                    int backCameraResult = getTestItemByName(backCameraTestItem).getResult();
                    int frontCameraResult = getTestItemByName(frontCameraTestItem).getResult();
                    if (backCameraResult != TestResult.NONE && frontCameraResult != TestResult.NONE) {
                        mView.setFailClickable(false);
                        mView.setPassClickable(false);
                        mCamera.stopPreview();
                        mCamera.release();
                        mView.finishTestCase();
                    } else {
                        mView.setFailClickable(true);
                        mView.setPassClickable(true);
                        mView.setCameraSwitchClickable(true);
                    }
                } catch (Exception e) {
                    if (LOCAL_LOGD) {
                        Log.v(TAG, e.toString());
                        mView.finishTestCase();
                    }
                }
                break;

            case R.id.cameraFail:

                /** A method for write camera fail into log file */
                try {
                    mView.setFailClickable(false);
                    mView.setPassClickable(false);
                    if (mNum == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        if (mIsCameraBackTakePicture) {
                            isCaptured(TestItemId.CameraBackCaptured, false);
                            mView.setCameraSwitchVisible();
                            mView.toastShowMessage(R.string.camera_back_fail);
                        } else {
                            mView.toastShowMessage(R.string.camera_str_warn_takepic);
                            if (LOCAL_LOGD) {
                                Log.i(TAG, "Take picture by Rear first");
                            }
                        }
                    } else if (mNum == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        if (mIsCameraRearTakePicture) {
                            isCaptured(TestItemId.CameraFrontCaptured, false);
                            mView.setCameraSwitchVisible();
                            mView.toastShowMessage(R.string.camera_front_fail);
                        } else {
                            mView.toastShowMessage(R.string.camera_str_warn_takepic);
                            if (LOCAL_LOGD) {
                                Log.i(TAG, "Take picture by Front first");
                            }
                        }
                    }
                    String backCameraTestItem = TEST_ITEM_NAME[TestItemId.CameraBackCaptured];
                    String frontCameraTestItem = TEST_ITEM_NAME[TestItemId.CameraFrontCaptured];
                    int backCameraResult = getTestItemByName(backCameraTestItem).getResult();
                    int frontCameraResult = getTestItemByName(frontCameraTestItem).getResult();
                    if (backCameraResult != TestResult.NONE && frontCameraResult != TestResult.NONE) {
                        mView.setFailClickable(false);
                        mView.setPassClickable(false);
                        mCamera.stopPreview();
                        mCamera.release();
                        mView.finishTestCase();
                    } else {
                        mView.setFailClickable(true);
                        mView.setPassClickable(true);
                        mView.setCameraSwitchClickable(true);
                    }
                } catch (Exception e) {
                    if (LOCAL_LOGD) {
                        Log.e(TAG, e.toString());
                        mView.finishTestCase();
                    }
                }
                break;

            case R.id.cameraSwitch:

                /** main and second camera change **/
                mView.setCameraSwitchInvisible();
                try {
                    if (mNum == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        mNum = Camera.CameraInfo.CAMERA_FACING_FRONT;
                        resetCamera();
                        mView.setTakePicText(R.string.camera_second);
                        if (mCamera != null) {
                            mCamera.release();
                        }
                        initCamera(mNum);
                        mView.setCameraFailVisible();
                        mView.setCameraPassVisible();
                        setDisplayOrientation(mNum);

                        if (LOCAL_LOGD) {
                            Log.v(TAG, "the second camera");
                        }

                    } else if (mNum == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        mNum = Camera.CameraInfo.CAMERA_FACING_BACK;
                        mView.setCameraFailInvisible();
                        mView.setCameraPassInvisible();
                        mView.setTakePicText(R.string.camera_main);
                        resetCamera();
                        if (mCamera != null) {
                            mCamera.release();
                        }
                        initCamera(mNum);

                        if (LOCAL_LOGD) {
                            Log.v(TAG, "the main camera");
                        }
                    }
                } catch (Exception e) {
                    if (LOCAL_LOGD) {
                        Log.e(TAG, e.toString());
                        Log.e(TAG, "Switch error");
                    }
                    isCaptured(mNum, false);
                    mView.toastShowMessage(R.string.camera_switch_error);
                    mView.finishTestCase();
                }

                mView.setTakePicVisible();
                mView.setTakePicClickable(false);
                CountDownTimerWitch witch = new CountDownTimerWitch(2000, 1000);
                witch.start();

                break;

        }
    }

    @Override
    public void resume()
    {
        super.resume();

    }

    @Override
    public void pause()
    {
        try {
            resetCamera();
        } catch (Exception e) {
            e.printStackTrace();
            if (LOCAL_LOGD) {
                Log.v(TAG, e.toString());
            }
        }
        super.pause();

    }

    /**
     * A method for open camera
     * 
     * @param a is used to mark back camera or front camera
     */
    private void openCamera(int a)
    {
        if (!mIsPreview) {
            try {

                mCamera = Camera.open(a);

            } catch (Exception e) {
                String excetption = e.toString();
                if (LOCAL_LOGD) {
                    Log.e(TAG, e.getMessage());
                }
                if (a == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    isCaptured(TestItemId.CameraBackCaptured, false);
                } else if (a == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    isCaptured(TestItemId.CameraFrontCaptured, false);
                }
                if (excetption.contains("Fail to connect to camera service")) {
                    if (a == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        mView.setCameraErrorDialog(a);
                    } else if (a == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        mView.setCameraErrorDialog(a);
                    }
                }
            }
        }
    }

    /** A method for set camera preview */
    private void setPreview()
    {

        if (mCamera != null && !mIsPreview) {
            try {
                if (LOCAL_LOGD) {
                    Log.v(TAG, "inside the camera");
                }
                mCamera.setPreviewDisplay(mView.getSurfaceHolder());
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_DAYLIGHT);
                List<Camera.Size> s = parameters.getSupportedPreviewSizes();

                if (s != null) {
                    for (int i = 0; i < s.size(); i++) {
                        if (LOCAL_LOGD) {
                            Log.v(TAG, "" + (((Camera.Size) s.get(i)).width) + "/"
                                    + (((Camera.Size) s.get(i)).height));
                        }
                    }
                }

                if (((Camera.Size) s.get(0)).width > ((Camera.Size) s.get(s.size() - 1)).width) {
                    parameters.setPreviewSize(((Camera.Size) s.get(s.size() - 1)).width,
                            ((Camera.Size) s.get(s.size() - 1)).height);
                } else {
                    parameters.setPreviewSize(((Camera.Size) s.get(0)).width,
                            ((Camera.Size) s.get(0)).height);
                }

                s = parameters.getSupportedPictureSizes();
                if (s != null) {
                    for (int i = 0; i < s.size(); i++) {
                        if (LOCAL_LOGD) {
                            Log.v(TAG, "" + (((Camera.Size) s.get(i)).width) + "/"
                                    + (((Camera.Size) s.get(i)).height));
                        }
                    }
                }

                if (((Camera.Size) s.get(0)).width > ((Camera.Size) s.get(s.size() - 1)).width) {
                    parameters.setPictureSize(((Camera.Size) s.get(s.size() - 1)).width,
                            ((Camera.Size) s.get(s.size() - 1)).height);
                } else {
                    parameters.setPictureSize(((Camera.Size) s.get(0)).width,
                            ((Camera.Size) s.get(0)).height);
                }

                mCamera.setParameters(parameters);
                mCamera.setPreviewDisplay(mView.getSurfaceHolder());
                mCamera.startPreview();
                mIsPreview = true;

                if (LOCAL_LOGD) {
                    Log.v(TAG, "startPreview");
                }

            } catch (Exception e) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
                if (LOCAL_LOGD) {
                    Log.e(TAG, e.toString());
                }
                e.printStackTrace();
            }
        }
    }

    /**
     * A method for initialize camera settings
     * 
     * @param a is used to mark back camera or front camera
     */
    private void initCamera(int a)
    {
        openCamera(a);
        setPreview();
        mView.setCameraFailInvisible();
        mView.setCameraPassInvisible();
        mThresholdInfo = new ThresholdVerifiedInfo(0);
    }

    /** A method is used to take a picture */
    private void takePicture()
    {

        if (mCamera != null && mIsPreview) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPictureSize(1280, 960);
            mCamera.setParameters(parameters);
            mCamera.autoFocus(autoFacusCallback);
            mCamera.takePicture(null, null, jpegCallback);
        }
    }

    /** A method for reset camera */
    private void resetCamera()
    {

        if (mCamera != null && mIsPreview) {
            mCamera.stopPreview();
            if (LOCAL_LOGD) {
                Log.v(TAG, "stopPreview");
            }
            mIsPreview = false;
        }
    }

    private PictureCallback jpegCallback = new PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera)
        {
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            File myCaptureFile = new File(mCaptureFilePath);
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(
                        myCaptureFile));
                bm.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                if (mNum == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(mResult);
                    Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
                            matrix, true);
                    Log.v("Image processing diff",
                            Integer.toString(bm.getWidth()) + ","
                                    + Integer.toString(bm.getHeight()));
                    mView.setImageViewImageBitmap(bitmap);
                    judgeImage(bitmap);
                } else if (mNum == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(mResult);
                    Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
                            matrix, true);
                    mView.setImageViewImageBitmap(bitmap);
                    mIsCameraRearTakePicture = true;
                }
                bos.flush();
                bos.close();
                resetCamera();
                mCamera.startPreview();
                mView.setTakePicClickable(false);
            } catch (Exception e) {
                if (mNum == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    isCaptured(TestItemId.CameraBackCaptured, false);
                } else if (mNum == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    isCaptured(TestItemId.CameraFrontCaptured, false);
                } else {
                    if (LOCAL_LOGD) {
                        Log.v(TAG, "Error id of Cameras");
                    }
                }
                if (LOCAL_LOGD) {
                    Log.e(TAG, "" + e.toString());
                    e.printStackTrace();
                }
                mView.finishTestCase();
            }
        }

        private void judgeImage(Bitmap bitmap)
        {
            if (!OpenCVLoader.initDebug()) {
                // Handle initialization error
            }
            Mat mat = new Mat();
            Mat edges = new Mat();
            Bitmap bitmapOriginal = bitmap;
            Bitmap bitmapProcessing = bitmap;
            Utils.bitmapToMat(bitmapOriginal, mat);
            org.opencv.imgproc.Imgproc.Canny(mat, edges, 50, 100);
            Utils.matToBitmap(edges, bitmapProcessing);

            int imageHeight = bitmapProcessing.getHeight();
            int imageWidth = bitmapProcessing.getWidth();
            int frameStart = (imageWidth / 4) * 1;
            int frameEnd = (imageWidth / 4) * 3;
            int[] pixelArray = new int[imageWidth];
            int getLine = 0;
            int countLine = 4 ;
            mChageTime = 0;
            TestResult testResult;
            int result;
            for (int i = 0; i < imageWidth; i++) {
                int realPixelNumber = bitmapProcessing.getPixel(i, imageHeight / 2);
                int realBluePixel = Color.blue(realPixelNumber);
                pixelArray[i] = realBluePixel;
                Log.v("Image processing diff", imageWidth + "," + imageHeight);
                Log.v("Image processing diff", Integer.toString(i) + "," + imageHeight / 2 + "--"
                        + Integer.toString(realBluePixel));
            }
            for (int i = frameStart; i < frameEnd - 1; i++) {
                int left = pixelArray[i];
                int right = pixelArray[i + 1];
                if (left != right)
                    mChageTime++;
            }
            getLine = mChageTime / countLine;
            Log.v("KevinCamera", Integer.toString(getLine));
            testResult = verifyCamera(getLine);
            result = testResult.getResult();

            Log.v("Image processing change", Integer.toString(mChageTime));
            if (result == RESULT_PASS) {
                Log.v("Image processing", "Image Judge OK");
                mView.toastShowMessage(R.string.camera_back_pass);
                mView.setCameraSwitchVisible();
            } else if (result == RESULT_FAIL) {
                Log.v("Image processing", "Image Judge Fail");
                mView.toastShowMessage(R.string.camera_back_fail);
                mCamera.stopPreview();
                mCamera.release();
                mView.finishTestCase();
            }
        }
    };

    AutoFocusCallback autoFacusCallback = new AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera)
        {
            Log.i("Foucus", "onAutoFocus:" + success);
        }
    };

    /** A class for set wait time */
    class CountDownTimerWitch extends CountDownTimer
    {
        public CountDownTimerWitch(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish()
        {
            mView.setTakePicClickable(true);
        }

        @Override
        public void onTick(long arg0)
        {
        }
    }

    class CountDownTimerPlay extends CountDownTimer
    {
        public CountDownTimerPlay(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onFinish()
        {
            mView.setCameraSwitchClickable(true);
        }

        @Override
        public void onTick(long arg0)
        {
        }
    }

    private boolean checkSDCard()
    {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
    {

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0)
    {
        initCamera(mNum);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0)
    {
        try {
            delFile(getmCaptureFilePath());
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            if (LOCAL_LOGD) {
                Log.v(TAG, "Surface Destroyed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (LOCAL_LOGD) {
                Log.e(TAG, e.toString());
            }
        }
    }

    private void delFile(String strFileName)
    {
        try {
            File myFile = new File(strFileName);
            if (myFile.exists()) {
                myFile.delete();
            }
        } catch (Exception e) {
            if (LOCAL_LOGD) {
                Log.e(TAG, e.toString());
            }
        }
    }

    /**
     * set display orientation for front camera preview
     * 
     * @param cameraId be used to mark which camera , back one or front one
     ***/
    public void setDisplayOrientation(int cameraId)
    {
        CameraInfo info = new CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = mView.getDisplayRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int orientation = info.orientation;
        int temp;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            temp = orientation + degrees;
            mResult = temp % 360;
            temp = 360 - mResult;
            mResult = temp % 360; // compensate the mirror
        } else {
            // back-facing
            temp = orientation - degrees + 360;
            mResult = temp % 360;
        }
        mCamera.setDisplayOrientation(mResult);
    }

    private TestResult verifyCamera(float camera_value)
    {
        mThresholdInfo.mInfo = camera_value;
        String itemName = TEST_ITEM_NAME[TestItemId.CameraBackCaptured];
        TestItem item = getTestItemByName(itemName);
        TestResult result = item.verify(mThresholdInfo);
        return result;
    }
}