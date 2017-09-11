package com.msi.diagnostic.app;

import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.ui.BacklightTestCaseView;

public class BacklightPagePresenter extends AbstractTestCasePresenter {

    private BacklightTestCaseView mView;
    private static final String TEST_ITEM_NAME = "isBacklightChanged";

    public BacklightPagePresenter(BacklightTestCaseView view) {
        super(view);
        mView = view;
    }

    @Override
    public void OnButtonClick(int buttonId) {
        TestItem item = getTestItemByName(TEST_ITEM_NAME);
        switch (buttonId) {
        case R.id.backlight_pass:
            if (!mView.getTestFlag()) {
                item.verify(new DetectionVerifiedInfo(true));
            }
            mView.finishTestCase();
            break;

        case R.id.backlight_fail:
            if (!mView.getTestFlag()) {
                item.verify(new DetectionVerifiedInfo(false));
            }
            mView.finishTestCase();
            break;

        default:
            break;
        }
    }

    /**
     * Set screen brightness
     *
     * @param value The progress of the seekbar, represent the screen brightness
     */
    public void setBrightness(int value)
    {
        // Prevent too dim
        if (value < 10) {
            value = 10;
        }
        float floatValue = Float.valueOf(value) * (1f / 100f);
        mView.setBacklightBrightness(floatValue);
    }
}
