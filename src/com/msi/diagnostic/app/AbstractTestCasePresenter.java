package com.msi.diagnostic.app;

import java.util.ArrayList;

import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.utils.Utils;
import com.msi.diagnostic.ui.AbstractLevelPanelView;
import com.msi.diagnostic.ui.AbstractTestCaseView;

public abstract class AbstractTestCasePresenter implements IPresenter {

    protected AbstractTestCaseView mView;
    private AbstractLevelPanelView mLevelPanel;

    protected IDiagnosticApp mApp;
    protected IDiagnoseModel mModel;
    protected TestCase mTestCase;

    protected static final String RESULT_PASS = TestResult.RESULT_PASS
            .getResultAsString();
    protected static final String RESULT_FAIL = TestResult.RESULT_FAIL
            .getResultAsString();
    protected static final String RESULT_NONE = TestResult.RESULT_NONE
            .getResultAsString();

    protected String mTestLevelName;
    protected String mTestCaseClassName;

    public AbstractTestCasePresenter(AbstractTestCaseView view) {

        mView = view;
        mApp = view.getDiagnosticApp();
        mModel = mApp.getDiagnoseModel();

        mTestLevelName = mView.getLevelName();
        mTestCaseClassName = mView.getClass().getName();
        mTestCase = mModel.loadTestCase(mTestLevelName, mTestCaseClassName);
        mLevelPanel = (AbstractLevelPanelView) mView.getActivity();
    }

    private void setTestTime() {
        long testTime = Utils.getCurrentTime();
        mTestCase.setTime(testTime);
    }

    protected TestItem getTestItemByName(String itemName) {
        return mTestCase.getTestItemByName(itemName);
    }

    @Override
    public abstract void OnButtonClick(int buttonId);

    public void resume() {
        mTestCase.reload();
        setTestTime();
        mTestCase.initTestItems();
        String testCaseName = mTestCase.getName();
        if(testCaseName.equals(AbstractLevelPagePresenter.WIFI_TESTCASE_NAME)||
                testCaseName.endsWith(AbstractLevelPagePresenter.BLUETOOTH_TESTCASE_NAME)){
            mTestCase.setResult(TestResult.TESTING);
            mModel.updateTestCase(mTestCase);
        }

    }

    public void pause() {
        mTestCase.updateContent();
        mLevelPanel.onTestCaseDone();
    }

    public void pause(boolean mTestCaseDone) {
        if (mTestCaseDone) {
            mTestCase.updateContent();
            if (mTestCase.getResult() == TestResult.FAIL) {
                mLevelPanel.setListStatus(true);
            }
        } else {
            mLevelPanel.onTestCaseDone();
        }
    }
}
