package com.msi.diagnostic.app;

import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.ui.LcdTestCaseView;
import com.msi.diagnostic.utils.CountDownWaitingTimer;
import com.msi.diagnostic.utils.ICountDownListener;

public class LcdPagePresenter extends AbstractTestCasePresenter implements
        ICountDownListener {

    private LcdTestCaseView mView;

    private final int COUNT_DOWN_TIME = 15000;
    private final int COUNT_INTERVAL = 1000;
    private int mColorIndex;
    private CountDownWaitingTimer mCountDownTimer;

    private static final String[] TEST_ITEM_NAME = {
            "isSingleTouchPass", "isLcdPass"
    };

    private static class TestItemId {
        public static final int SINGLE_TOUCH = 0;
        public static final int LCD = 1;
    }

    private static class LcdColor {
        public static final int BLACK = 0;
        public static final int WHITE = 1;
        public static final int RED = 2;
        public static final int GREEN = 3;
        public static final int BLUE = 4;
    }

    public LcdPagePresenter(LcdTestCaseView view) {
        super(view);
        mView = view;
        mColorIndex = 0;
        mCountDownTimer = new CountDownWaitingTimer(COUNT_DOWN_TIME,
                COUNT_INTERVAL, this);
        mCountDownTimer.start();
    }

    public void singleTouchFailed() {
        writeToDB(false, false);
        mView.finishTestCase();
    }

    private void lcdPass() {
        writeToDB(true, true);
        mView.finishTestCase();
    }

    private void lcdFailed() {
        writeToDB(true, false);
        mView.finishTestCase();
    }

    private void writeToDB(Boolean singleTouchPass, Boolean lcdPass) {
        String itemName1 = TEST_ITEM_NAME[TestItemId.SINGLE_TOUCH];
        String itemName2 = TEST_ITEM_NAME[TestItemId.LCD];
        TestItem singleTouch = getTestItemByName(itemName1);
        TestItem lcd = getTestItemByName(itemName2);
        singleTouch.verify(new DetectionVerifiedInfo(singleTouchPass));
        lcd.verify(new DetectionVerifiedInfo(lcdPass));
    }

    /**
     * When click the button, change the background color and create new button
     * position. If all colors have been tested, turn to the result judge
     * activity.
     */
    public void setBackground() {
        mCountDownTimer.cancel();
        switch (mColorIndex) {
        case LcdColor.BLACK:
            mView.setBackground(R.color.black);
            ++mColorIndex;
            mCountDownTimer.start();
            break;

        case LcdColor.WHITE:
            mView.setBackground(R.color.white);
            ++mColorIndex;
            mCountDownTimer.start();
            break;

        case LcdColor.RED:
            mView.setBackground(R.color.red);
            ++mColorIndex;
            mCountDownTimer.start();
            break;

        case LcdColor.GREEN:
            mView.setBackground(R.color.green);
            ++mColorIndex;
            mCountDownTimer.start();
            break;

        case LcdColor.BLUE:
            mView.setBackground(R.color.blue);
            ++mColorIndex;
            mCountDownTimer.start();
            break;

        default:
            mView.jumpToJudge();
            break;
        }
    }

    @Override
    public void OnButtonClick(int buttonId) {

        switch (buttonId) {
        case R.id.lcd_pass_button:
            lcdPass();
            break;

        case R.id.lcd_fail_button:
            lcdFailed();
            break;

        case R.id.click_button:
            setBackground();
            break;

        default:
            break;
        }
    }

    @Override
    public void pause() {
        mCountDownTimer.cancel();
        super.pause();
    }
    @Override
    public void onCountDownFinish() {
        singleTouchFailed();
    }

    @Override
    public void onCountDownTick(long millisUntilFinished) {
        long time = millisUntilFinished / 1000;
        mView.setClickButtonText(time);
    }
}
