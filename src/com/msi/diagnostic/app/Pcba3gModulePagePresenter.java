package com.msi.diagnostic.app;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.util.Log;

import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.DeviceConfig;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.ui.Pcba3gModuleTestCaseView;
import com.msi.diagnostic.utils.Utils;

public class Pcba3gModulePagePresenter extends AbstractTestCasePresenter {

    private Pcba3gModuleTestCaseView mView;
    private static final String TAG = "TGModulePagePresenter";
    private static final boolean LOCAL_LOG = false;

    private static final boolean F_INVALID = false;

    private DeviceConfig mDeviceConfig;

    private TestItem mItem;
    private DetectionVerifiedInfo mVerifiedInfo;
    private TestResult mResult;

    private static class TestItemId {
        public static final int IS_3GVALID = 0;
    }

    private static final String[] TEST_ITEM_NAME = {
        "is3gValid"
    };

    public void initItem() {
        String itemName = TEST_ITEM_NAME[TestItemId.IS_3GVALID];
        mItem = getTestItemByName(itemName);
        mVerifiedInfo = new DetectionVerifiedInfo(F_INVALID);
        mResult = mItem.verify(mVerifiedInfo);
    }

    public Pcba3gModulePagePresenter(Pcba3gModuleTestCaseView view) {
        super(view);
        mView = view;
        mDeviceConfig = this.mApp.getDeviceConfig();

    }

    @Override
    public void OnButtonClick(int buttonId) {

        switch (buttonId) {

        case R.id.startTest:
            TestResult result = testTGModule();
            int resId = R.string.tgmodule_fail;
            if (result.getResult() == TestResult.PASS) {
                resId = R.string.tgmodule_pass;
            }
            mView.showResultToast(resId);
            mView.finishTestCase();
            break;

        default:
            break;
        }
    }

    @Override
    public void resume() {
        if (LOCAL_LOG)
            Log.d(TAG, "resume");
        super.resume();
        initItem();
    }

    @Override
    public void pause() {
        if (LOCAL_LOG)
            Log.d(TAG, "pause");
        super.pause();
    }

    /**
     * Test if device is 3G sku.
     */
    private boolean hasMobileRadio() {

        String mDeviceName = mDeviceConfig.getDeviceName();

        /* Get the supported boards for Diagnostic Testing */
        String[] supportedDevices = this.mApp.getAppContext()
                .getResources().getStringArray(R.array.supported_devices);

        if (mDeviceName.equals(supportedDevices[2]))
            return true;
        else
            return false;
    }

    private boolean isUsbHubExisted() {
        String infoFileName = mItem.getInfoFileName();
        try {
            String info = Utils.readContentFromFile(infoFileName);
            if (info != null)
                return true;
        } catch (FileNotFoundException fileNotFind) {
            fileNotFind.printStackTrace();
            if (LOCAL_LOG)
                Log.d(TAG, "The device hasn't 3GModule HardWare!!");
            return false;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private TestResult testTGModule() {
        boolean hasMobile = hasMobileRadio();
        boolean isUsbHubExisted = isUsbHubExisted();
        return is3gValid(hasMobile && isUsbHubExisted);

    }

    private TestResult is3gValid(boolean isExisted) {

        mVerifiedInfo.mInfo = isExisted;
        mResult = mItem.verify(mVerifiedInfo);
        return mResult;
    }
}
