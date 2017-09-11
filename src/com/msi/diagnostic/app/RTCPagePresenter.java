package com.msi.diagnostic.app;

import java.io.IOException;

import com.msi.diagnostic.data.DefinitionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.ui.RTCTestCaseView;
import com.msi.diagnostic.utils.Utils;

public class RTCPagePresenter extends AbstractTestCasePresenter {

    private RTCTestCaseView mView;
    private static class TestItemId {
        public static final int NameExisted = 0;
        public static final int DateExisted = 1;
        public static final int TimeExisted = 2;
    }

    private static final String[] TEST_ITEM_NAME = {
            "isNameExisted",
            "isDateExisted",
            "isTimeExisted"
    };

    public RTCPagePresenter(RTCTestCaseView view) {
        super(view);
        mView = view;
    }

    private void isExisted(int itemNameIndex) {
        String itemName = TEST_ITEM_NAME[itemNameIndex];
        TestItem item = getTestItemByName(itemName);

        String infoFileName = item.getInfoFileName();
        try {
            String info = Utils.readContentFromFile(infoFileName);
            item.verify(new DefinitionVerifiedInfo(info));

        } catch (NullPointerException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resume() {
        super.resume();
        testRtc();
    }

    @Override
    public void pause() {
        super.pause();
    }


    /**
     * Test whether the RTC-related files are existed or not.
     */
    public void testRtc() {

        isExisted(TestItemId.NameExisted);
        isExisted(TestItemId.DateExisted);
        isExisted(TestItemId.TimeExisted);

        /* write the result back to database and to finish this testing */
        mView.finishTestCase();
    }

    @Override
    public void OnButtonClick(int buttonId) {
        throw new UnsupportedOperationException();
    }
}
