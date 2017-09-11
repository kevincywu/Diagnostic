package com.msi.diagnostic.app;

import android.util.Log;

import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.ui.MotorTestCaseView;

public class MotorPagePresenter extends AbstractTestCasePresenter {

    private static final String TAG = "MotorPagePresenter";

    private static final boolean LOCAL_LOG = false;

    private MotorTestCaseView mView;

    private static class TestItemId {
        public static final int IS_VIBRATED = 0;
    }

    private static final String[] TEST_ITEM_NAME = {
        "isVibrated"
    };

    public MotorPagePresenter(MotorTestCaseView view) {
        super(view);
        mView = view;
        mApp = mView.getDiagnosticApp();
    }

    private TestResult isVibrated(boolean mVibrated) {
        if (mView.getToggleButtonStatus())
            mView.cancelVibrate();
        String itemName = TEST_ITEM_NAME[TestItemId.IS_VIBRATED];
        TestItem item = getTestItemByName(itemName);
        TestResult result = item.verify(new DetectionVerifiedInfo(mVibrated));
        return result;
    }

    @Override
    public void resume() {
        if (LOCAL_LOG)
            Log.d(TAG, "resume");
        super.resume();
    }

    @Override
    public void pause() {
        if (LOCAL_LOG)
            Log.d(TAG, "pause");
        super.pause();
    }

    private boolean isToggled = false;
    @Override
    public void OnButtonClick(int buttonId) {
        switch (buttonId) {
        case R.id.toggle:
            isToggled = true;
            if (mView.getToggleButtonStatus())
                mView.setVibrate();
            else
                mView.cancelVibrate();

            break;

        case R.id.pass:
            if(isToggled){
                isVibrated(true);
            }else{
                String itemName = TEST_ITEM_NAME[TestItemId.IS_VIBRATED];
                TestItem item = getTestItemByName(itemName);
                item.setResult(TestResult.NONE);
                mView.getDiagnosticApp().getDiagnoseModel().updateTestItem(item);
            }

            mView.finishTestCase();
            break;

        case R.id.fail:
            if(isToggled){
                isVibrated(false);
            }else{
                String itemName = TEST_ITEM_NAME[TestItemId.IS_VIBRATED];
                TestItem item = getTestItemByName(itemName);
                item.setResult(TestResult.NONE);
                mView.getDiagnosticApp().getDiagnoseModel().updateTestItem(item);
            }

            mView.finishTestCase();
            break;

        default:
            break;
        }
    }
}
