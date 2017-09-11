package com.msi.diagnostic.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.util.Log;

import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.ui.SDcardTestCaseViewN915;
import com.msi.diagnostic.utils.CountDownWaitingTimer;
import com.msi.diagnostic.utils.ICountDownListener;

public class SDcardPagePresenterN915 extends AbstractTestCasePresenter
        implements ICountDownListener {

    private static final String TAG = "SDcardPagePresenter";
    private static final boolean LOCAL_LOG = false;

    private static final boolean F_INVALID = false;

    private SDcardTestCaseViewN915 mView;
    private IDiagnosticApp mApp;

    private CountDownWaitingTimer mWait;
    private DetectionVerifiedInfo mSdVerifiedInfo;

    private TestItem mItem;
    private TestResult mResult;

    private String mSdPath, mFolderPath, mFilePath;
    private String mNoteStart, mNoteEnd;
    private byte[] mTestStr;

    private boolean mIsCounting = false;

    final int CRITERIA = 10;
    private int times = 0;

    private static class TestItemId {
        public static final int IS_SDFUNCTIONS = 0;
    }

    private static final String[] TEST_ITEM_NAME = {
        "isSdFunctions"
    };

    public SDcardPagePresenterN915(SDcardTestCaseViewN915 view) {
        super(view);
        mView = view;
        mApp = mView.getDiagnosticApp();
    }

    public void initSdItem() {
        mTestStr = mView.getSdViewString(R.string.sdcard_test_string)
                .getBytes();
        mNoteStart = mView.getSdViewString(R.string.text_start);
        mNoteEnd = mView.getSdViewString(R.string.text_end);

        String itemName = TEST_ITEM_NAME[TestItemId.IS_SDFUNCTIONS];
        mItem = getTestItemByName(itemName);
        mSdPath = mItem.getInfoFileName();
        mSdVerifiedInfo = new DetectionVerifiedInfo(F_INVALID);
        mResult = TestResult.RESULT_NONE;

        String sInfoFormat = mView.getSdViewString(R.string.sdcard_folder_path);
        mFolderPath = String.format(sInfoFormat, mSdPath);

        sInfoFormat = mView.getSdViewString(R.string.sdcard_file_path);
        mFilePath = String.format(sInfoFormat, mFolderPath);
        setTimer();
    }

    private boolean getSdcardState() {
        try {
            File mSDCard = new File(mSdPath);
            if (mSDCard.getTotalSpace() > 0.0)
                return true;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void beginRW() {

        File folder = new File(mFolderPath);
        File targetFile = new File(mFilePath);
        try {
            if (!folder.exists()) {
                folder.mkdirs();
            }
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
            fileDelete(folder, targetFile);
            returnState(false);
        }
        write(folder, targetFile);
        read(folder, targetFile);
    }

    /**
     * Read file 'SDcardTest.txt' from SDCard.
     * 
     * @param dir
     *            Folder contains the test file.
     * @param path
     *            Test file absolute path.
     * @throws FileNotFoundException
     * @throws SecurityException
     * @throws IOException
     */
    private void read(File dir, File path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            fis.close();
            /*
             * If the content read from the file matches what you write, delete
             * the file and return PASS state.
             */
            if (sb.toString().equals(byte2String(mTestStr))) {
                fileDelete(dir, path);
                returnState(true);

            }
        } catch (Exception e) {
            fileDelete(dir, path);
            returnState(false);
        }
    }

    /**
     * Write 'sdcard test' to file 'SDcardTest.txt' in SDCard. If failed just
     * remove the test file and return FAIL state.
     * 
     * @param dir
     *            Folder contains the test file.
     * @param path
     *            Test file absolute path.
     * @throws FileNotFoundException
     * @throws SecurityException
     * @throws IOException
     */
    private void write(File dir, File path) {
        try {
            FileOutputStream fos = new FileOutputStream(path, false);
            fos.write(mTestStr);
            fos.close();
        } catch (Exception e) {
            fileDelete(dir, path);
            returnState(false);
        }
    }

    private void fileDelete(File dirPath, File filePath) {
        filePath.delete();
        dirPath.delete();
    }

    /**
     * Change the char in byte array to string.
     */
    private String byte2String(byte[] buffer) {
        int bufferSize, index;
        String str = "";

        bufferSize = buffer.length;
        for (index = 0; index < bufferSize; index++) {
            str += (char) buffer[index];
        }
        return str;
    }

    @Override
    public void OnButtonClick(int buttonId) {

        switch (buttonId) {
        case R.id.skip_button:
            mView.finishTestCase();
            break;
        }
    }

    private void returnState(boolean mExisted) {

        if (mExisted)
            mSdVerifiedInfo.mInfo = true;
        else
            mSdVerifiedInfo.mInfo = false;

        mResult = mItem.verify(mSdVerifiedInfo);
        mWait.cancel();
        mView.finishTestCase();
    }

    private void setTimer() {
        if (mIsCounting) {
            mWait.cancel();
        }
        mWait = new CountDownWaitingTimer(1000, 1000, this);
        mWait.start();

        mIsCounting = true;
    }

    @Override
    public void onCountDownFinish() {
        mIsCounting = false;
        if (!getSdcardState()) {
            if (times > CRITERIA) {
                mView.setSdText(R.string.note_no_sdcard);
                times = 0;
                return;
            } else {
                mView.setSdText(R.string.note_insert);
                setTimer();
            }
        } else {
            mView.setSdText(R.string.note_testing);
            returnState(true);
            //beginRW();
            times = 0;
        }

    }

    @Override
    public void onCountDownTick(long millisUntilFinished) {
        mView.setSdText(mNoteStart + (CRITERIA - times) + mNoteEnd);

    }

    @Override
    public void resume() {
        if (LOCAL_LOG)
            Log.d(TAG, "resume");
        super.resume();

        initSdItem();
    }

    @Override
    public void pause() {
        if (LOCAL_LOG)
            Log.d(TAG, "pause");
        super.pause();
    }

}