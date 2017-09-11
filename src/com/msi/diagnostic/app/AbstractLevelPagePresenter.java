package com.msi.diagnostic.app;

import java.io.File;
import java.util.ArrayList;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;

import com.msi.diagnostic.app.logger.LogManager;
import com.msi.diagnostic.data.IDataChangedListener;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.ui.ILevelPanelView;

public abstract class AbstractLevelPagePresenter implements
        ILevelPagePresenter, IDataChangedListener {

    public static final String KEY_LOG_FILENAME = "log_file_name";
    public static final String DEFAULT_LOG_FILENAME = "diagnostic_log_file";
    public static final String KEY_BARCODE_NUMBER = "barcode_key";
    public static final String DEFAULT_BARCODE_NUMBER = "null";
    public static final String WIFI_TESTCASE_NAME = "com.msi.diagnostic.ui.WifiTestCaseView";
    public static final String BLUETOOTH_TESTCASE_NAME = "com.msi.diagnostic.ui.BluetoothTestCaseView";
    private static final String PROJECT_NAME = "DIAGNOSTIC";
    private static final int INDEX_NONE_SELECT_ITEM = -1;
    private String levelPanelFolderName;
    private LogManager mLogMgr;
    private int mListPosition;
    protected IDiagnosticApp mApp;
    protected ILevelPanelView mPanelView;
    protected IDiagnoseModel mModel;
    protected TestCaseManager mTestManager;
    protected String mKeyLocation;

    public AbstractLevelPagePresenter(IDiagnosticApp app, ILevelPanelView view,
            String name) {
        mApp = app;
        mPanelView = view;
        mModel = app.getDiagnoseModel();
        mTestManager = new TestCaseManager(mModel, name);
        mKeyLocation = name;
        mLogMgr = new LogManager(mTestManager);
        mModel.setDataChangedListener(this);
    }

    private String extractFolderName(String levelPanelName) {
        String[] stringNames = levelPanelName.split("\\.");
        String[] folderNames = stringNames[4].split("PanelView");
        if (folderNames.length == 0) {
            return new String();
        } else {
            folderNames[0] = folderNames[0].toUpperCase();
            return folderNames[0];
        }
    }

    protected boolean saveLogToFile() {
        SharedPreferences prefs = mApp.getPrivatePreferences();
        String fileName = prefs.getString(KEY_LOG_FILENAME,
                DEFAULT_LOG_FILENAME);

        levelPanelFolderName = extractFolderName(mKeyLocation);

        String theFullPath = PROJECT_NAME + "/" + levelPanelFolderName;
        SharedPreferences prefsBarcode = mApp.getPrivatePreferences();
        String barCode = prefsBarcode.getString(KEY_BARCODE_NUMBER,
                DEFAULT_BARCODE_NUMBER);
        /** Delete File in Level Folder **/
        String FILE_PATH = Environment.getExternalStorageDirectory().toString();
        String folderPATH = FILE_PATH + "/" + theFullPath;
        File logFile = new File(folderPATH);
        File[] logFiles = logFile.listFiles();
        if (logFiles != null) {
            for (File f : logFiles) {
                f.delete();
            }
        }
        /***********************************/
        return mLogMgr.saveToFile(theFullPath + "/" + fileName, barCode);
    }


    protected void navigateToTargetTest(TestCase testCase) {
        int location = mTestManager.getLocation();
        mListPosition = location;
        mPanelView.setSelectItem(mListPosition);
        mPanelView.updateList();
        Class<?> targetClass = testCase.getTargetClass();
        mPanelView.navigate(targetClass);
    }

    @Override
    public void onSaveState(Bundle savedInstanceState) {
        savedInstanceState.putInt(mKeyLocation, mTestManager.getLocation());
    }

    @Override
    public void clearTestData() {
        ArrayList<TestCase> testCases = mTestManager.getList();
        for(TestCase testcase : testCases) {
            if(testcase.getResult() != TestResult.NONE) {
                testcase.setResult(TestResult.NONE);
                mModel.updateTestCase(testcase);
                ArrayList<TestItem> testItems = testcase.getTestItems();
                for(TestItem testItem : testItems) {
                    testItem.setResult(TestResult.NONE);
                    mModel.updateTestItem(testItem);
                }
            }
        }
    }

    @Override
    public void onRestoreState(Bundle outState) {
        mTestManager.setLocation(outState.getInt(mKeyLocation));
    }

    @Override
    public void OnButtonClick(int buttonId) {
        final int testCaseButtonCount = mPanelView.getCountOfTestCaseButtons();
        if (buttonId < testCaseButtonCount) {
            TestCase testCase = mPanelView.getData(buttonId);
            mTestManager.setLocationByTestCase(testCase);
            navigateToTargetTest(testCase);
        }
    }

    @Override
    public void onOptionsItemExitSelected() {
        this.onOptionsItemStopSelected();
        mPanelView.exitConfirmDialog();
        saveLogToFile();
    }

    @Override
    public void onOptionsItemStopSelected() {
        mPanelView.cancelCurrentTestCase();
        mPanelView.setListStatus(true);
        mPanelView.setSelectItem(INDEX_NONE_SELECT_ITEM);
    }

    @Override
    public void onOptionsItemLogSelected() {
        SharedPreferences prefsBarcode = mApp.getPrivatePreferences();
        String barCode = prefsBarcode.getString(KEY_BARCODE_NUMBER,
                DEFAULT_BARCODE_NUMBER);
        mPanelView.openLog(mLogMgr.getLog(barCode));
    }

    private void setTestCasesForPanelView() {
        ArrayList<TestCase> testCases = mTestManager.getList();
        mPanelView.setData(testCases);
    }

    @Override
    public void resume() {
        setTestCasesForPanelView();

        TestCase currentTestCase = mTestManager.current();
        if (currentTestCase == null) {
            navigateToTargetTest(mTestManager.next());
        }
    }

    @Override
    public void onFinishTestCase() {
        setTestCasesForPanelView();

        TestCase currentTestCase = mTestManager.current();
        String mCurrentTestCaseName = currentTestCase.getName();

        if (currentTestCase == null
                || mCurrentTestCaseName.equals(WIFI_TESTCASE_NAME)
                || mCurrentTestCaseName.equals(BLUETOOTH_TESTCASE_NAME)) {
            navigateToTargetTest(mTestManager.next());

        } else {
            boolean isCurrentPass = (currentTestCase.getResult() == TestResult.PASS);
            boolean isLastTestCase = mTestManager.isLast();
            TestCase nextTextCase = mTestManager.next();
            mTestManager.previous();
            boolean isNextNone = (nextTextCase.getResult() == TestResult.NONE);
            if (isCurrentPass && !isLastTestCase && isNextNone) {
                navigateToTargetTest(mTestManager.next());
            } else {
                mPanelView.setListStatus(true);
                mPanelView.setSelectItem(INDEX_NONE_SELECT_ITEM);

                // the special condition for the automatic running
                if (mPanelView.isExtraScreenExisted() == true) {
                    mPanelView.cancelCurrentTestCase();
                }

            }
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void notifyDataChanged(long id) {
        mTestManager.reloadTestCase(id);
        setTestCasesForPanelView();
    }
}
