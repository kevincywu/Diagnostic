package com.msi.diagnostic.app;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DefinitionVerifiedInfo;
import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.DeviceConfig;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.ui.BarCodeTestCaseView;

public class BarCodePagePresenter extends AbstractTestCasePresenter {
    private static final String TAG = "BarCodePagePresenter";
    private static final boolean LOCAL_LOG = false;

    private BarCodeTestCaseView mView;
    private IDiagnosticApp mApp;

    private String mBarcodeNumber;

    private static class TestItemId {
        public static final int IS_NUMBEREXISTED = 0;
    }

    private static final String[] TEST_ITEM_NAME = {
        "isNumberExisted"
    };

    public BarCodePagePresenter(BarCodeTestCaseView view) {
        super(view);
        mView = view;
        mApp = mView.getDiagnosticApp();
    }

    private TestResult isNumberExisted(boolean r) {
        String itemName = TEST_ITEM_NAME[TestItemId.IS_NUMBEREXISTED];
        TestItem item = getTestItemByName(itemName);
        TestResult result = item.verify(new DetectionVerifiedInfo(r));
        return result;
    }

    private void setLogFileName(String fileName) {
        SharedPreferences prefs = mApp.getPrivatePreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(AbstractLevelPagePresenter.KEY_LOG_FILENAME, fileName);
        editor.commit();
    }

    private void setBarcodeNumber(String barCode) {
        SharedPreferences prefs = mApp.getPrivatePreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(AbstractLevelPagePresenter.KEY_BARCODE_NUMBER, barCode);
        editor.commit();
    }

    @Override
    public void OnButtonClick(int buttonId) {
        switch (buttonId) {
        case R.id.submit:
            DeviceConfig deviceConfigSummit = mApp.getDeviceConfig();
            String checkNameSummit = deviceConfigSummit.getDeviceName();
            String textViewBarcodeSummit = mView.getBarCodeNumberText();
            boolean checkBarcodeSummit = checkBarcode(textViewBarcodeSummit,
                    checkNameSummit);
            if (checkBarcodeSummit) {
                mView.finishTestCase();
            }
        case KeyEvent.KEYCODE_ENTER:
            try {
                mBarcodeNumber = mView.getBarCodeNumberText();
                setBarcodeNumber(mBarcodeNumber);
                String itemName = TEST_ITEM_NAME[TestItemId.IS_NUMBEREXISTED];
                TestItem item = getTestItemByName(itemName);
                String checkNameEnter = item.getInfoFileName();
                String textViewBarcodeEnter = mView.getBarCodeNumberText();
                boolean checkBarcodeEnter = checkBarcode(textViewBarcodeEnter,
                        checkNameEnter);
                if (!checkBarcodeEnter) {
                    TestResult result = isNumberExisted(false);
                    Toast.makeText(mApp.getAppContext(),
                            "No " + checkNameEnter + " string, scan again!",
                            Toast.LENGTH_LONG).show();
                    mView.setBarCodeNumberText("");
                } else {
                    TestResult result = isNumberExisted(true);
                    setLogFileName(mBarcodeNumber);
                    mView.finishTestCase();
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            break;

        default:
            break;
        }
    }

    private boolean checkBarcode(String textViewBarcode, String checkName) {
        if (textViewBarcode.contains(checkName))
            return true;
        else
            return false;
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

}
