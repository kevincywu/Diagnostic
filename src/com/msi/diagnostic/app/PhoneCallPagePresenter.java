package com.msi.diagnostic.app;

import android.content.Intent;
import android.net.Uri;

import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.ui.PhoneCallTestView;

public class PhoneCallPagePresenter extends AbstractTestCasePresenter {
    private PhoneCallTestView mView;
    private static final String mPhoneNumber = "10086";
    private Intent mDailIntent;

    private static class TestItemId {
        public static final int PHONE_CALL = 0;
    }

    private static final String[] TEST_ITEM_NAME = {
        "isPhoneCallPass"
    };

    public PhoneCallPagePresenter(PhoneCallTestView view) {
        super(view);
        mView = view;
    }

    @Override
    public void OnButtonClick(int buttonId) {
        switch (buttonId) {
            case R.id.phone_call_call_button:
                mView.startActivity(mDailIntent);
                break;
            case R.id.phone_call_pass:
                verifyResult(true);
                mView.finishTestCase();
                break;
            case R.id.phone_call_fail:
                verifyResult(false);
                mView.finishTestCase();
                break;
        }
    }

    @Override
    public void resume() {
        super.resume();
        mDailIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPhoneNumber));
    }

    @Override
    public void pause() {
        super.pause();
    }

    private void verifyResult(boolean result) {
        String itemName = TEST_ITEM_NAME[TestItemId.PHONE_CALL];
        TestItem item = getTestItemByName(itemName);
        DetectionVerifiedInfo info = new DetectionVerifiedInfo(result);
        item.verify(info);
    }
}
