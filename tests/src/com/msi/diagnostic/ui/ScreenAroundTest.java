
package com.msi.diagnostic.ui;

import android.test.AndroidTestCase;
import android.util.Log;
import android.view.MotionEvent;

import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.app.ScreenAroundPagePresenter;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockDetectionTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

public class ScreenAroundTest extends AndroidTestCase {

    private ScreenAroundPagePresenter mTestPresenter;
    private MockScreenAroundTestCaseView mMockView;
    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;

    private final String mTestLevelName = TestLevel.TYPES[TestLevel.TYPE_ASSY];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mTestCaseNameScreenArond = "com.msi.diagnostic.ui.ScreenAroundTestCaseView";
    private final String mTestItemName = "isScreenAroundPass";

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();

        final boolean mVerifiedInfoDefine = true;
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelName, mTestLevelCaption);
        TestCase testCase = new TestCase(mApp, 0, mTestCaseNameScreenArond, mTestLevelName, null,
                TestResult.create(TestResult.NONE), 0);
        TestItem testItem = new MockDetectionTestItem(mApp, 0, mTestItemName,
                null, mTestLevelName, mTestCaseNameScreenArond,
                mVerifiedInfoDefine, TestResult.create(TestResult.NONE));

        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItem);

        mMockView = new MockScreenAroundTestCaseView(mApp);
        mTestPresenter = new ScreenAroundPagePresenter(mMockView);

    }

    private void checkTopNormalTouchAndLineDone() {
        int xdotAmount = mMockView.mScreenWidth / (2 * mMockView.RADIUS);
        MotionEvent eventObject;
        // the top points(horizental)
        for (int cnt = 0; cnt < xdotAmount; cnt++) {
            eventObject = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 
                    mMockView.RADIUS * (2 * cnt + 1), 0, 0);
            mTestPresenter.touchEvent(eventObject);
        }
        // the last one point for condition test
        eventObject = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 0, 
                mMockView.RADIUS + mMockView.RADIUS + mMockView.RADIUS, 0);
        mTestPresenter.touchEvent(eventObject);

        assertEquals("RETEST", mMockView.getTestResult());
    }

    private void checkConditionHandleLineFail() {
        int xLoc = mMockView.mScreenWidth / 4 + 50;
        int yLoc = mMockView.mScreenHeight / 4 + 100;

        MotionEvent eventObject;
        // For 6 incorrect points locating at the finish text area
        for (int cnt = 0; cnt < 15; cnt++) {
            eventObject = MotionEvent
                    .obtain(0, 0, MotionEvent.ACTION_UP, xLoc + cnt, yLoc + cnt, 0);
            Log.i("SAT", "cond2 x= " + (xLoc + cnt) + " y= " + (yLoc + cnt));
            mTestPresenter.touchEvent(eventObject);
        }

        assertEquals("TESTFAIL", mMockView.getTestResult());

    }

    private void checkRightNormalTouchAndLineDone() {
        int xdotAmount = mMockView.mScreenWidth / (2 * mMockView.RADIUS);
        int ydotAmount = mMockView.mScreenHeight / (2 * mMockView.RADIUS);
        MotionEvent eventObject;
        // the right points(vertical)
        for (int cnt = 0; cnt < ydotAmount; cnt++) {
            eventObject = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN,
                    mMockView.RADIUS * (2 * (xdotAmount - 1) + 1),
                    mMockView.RADIUS * (2 * cnt + 1), 0);
            mTestPresenter.touchEvent(eventObject);
        }

        // the last one point for condition test
        eventObject = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 0,
                mMockView.RADIUS + mMockView.RADIUS + mMockView.RADIUS, 0);
        mTestPresenter.touchEvent(eventObject);

        assertEquals("RETEST", mMockView.getTestResult());
    }

    private void checkBottomNormalTouchAndLineDone() {
        int xdotAmount = mMockView.mScreenWidth / (2 * mMockView.RADIUS);
        int ydotAmount = mMockView.mScreenHeight / (2 * mMockView.RADIUS);

        MotionEvent eventObject;
        // the bottom points(horizental)
        for (int cnt = 0; cnt < xdotAmount; cnt++) {
            eventObject = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN,
                    mMockView.RADIUS * (2 * cnt + 1),
                    mMockView.RADIUS * (2 * ydotAmount - 1), 0);
            mTestPresenter.touchEvent(eventObject);
        }

        // the last one point for condition test
        eventObject = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 0,
                mMockView.RADIUS + mMockView.RADIUS + mMockView.RADIUS, 0);
        mTestPresenter.touchEvent(eventObject);

        assertEquals("RETEST", mMockView.getTestResult());
    }

    private void checkLeftNormalTouchAndLineDone() {
        int xdotAmount = mMockView.mScreenWidth / (2 * mMockView.RADIUS);
        int ydotAmount = mMockView.mScreenHeight / (2 * mMockView.RADIUS);

        MotionEvent eventObject;
        // the left points(horizental)
        for (int cnt = 0; cnt < ydotAmount; cnt++) {
            eventObject = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN,
                    mMockView.RADIUS * (2 * cnt + 1), 0, 0);
            mTestPresenter.touchEvent(eventObject);
        }

        // the last one point for condition test
        eventObject = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 0,
                mMockView.RADIUS + mMockView.RADIUS + mMockView.RADIUS, 0);
        mTestPresenter.touchEvent(eventObject);

        assertEquals("RETEST", mMockView.getTestResult());

    }

    private void checkMiddleHorizontalNormalTouchAndLineDone() {
        int xdotAmount = mMockView.mScreenWidth / (2 * mMockView.RADIUS);
        int ydotAmount = mMockView.mScreenHeight / (2 * mMockView.RADIUS);
        int ydotMiddle = ydotAmount / 2;

        MotionEvent eventObject;
        // the middle horizontal points(horizental)
        for (int cnt = 0; cnt < xdotAmount; cnt++) {
            eventObject = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN,
                    mMockView.RADIUS * (2 * cnt + 1),
                    mMockView.RADIUS * (2 * (ydotMiddle - 1) + 1), 0);
            mTestPresenter.touchEvent(eventObject);
        }

        // the last one point for condition test
        eventObject = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 0,
                mMockView.RADIUS + mMockView.RADIUS + mMockView.RADIUS, 0);
        mTestPresenter.touchEvent(eventObject);

        assertEquals("RETEST", mMockView.getTestResult());
    }

    private void checkMiddleVerticalNormalTouchAndLineDone() {
        int xdotAmount = mMockView.mScreenWidth / (2 * mMockView.RADIUS);
        int ydotAmount = mMockView.mScreenHeight / (2 * mMockView.RADIUS);
        int xdotMiddle = xdotAmount / 2;

        MotionEvent eventObject;
        // the middle horizontal points(horizental)
        for (int cnt = 0; cnt < ydotAmount; cnt++) {
            eventObject = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN,
                    mMockView.RADIUS * (2 * (xdotMiddle - 1) + 1),
                    mMockView.RADIUS * (2 * cnt + 1), 0);
            mTestPresenter.touchEvent(eventObject);
        }

        // the last one point for condition test
        eventObject = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 0,
                mMockView.RADIUS + mMockView.RADIUS + mMockView.RADIUS, 0);
        mTestPresenter.touchEvent(eventObject);

        assertEquals("RETEST", mMockView.getTestResult());
    }
    public void test_touchEvent_TopvalidDots_ResultRETEST() {

        mMockView.getCoordinate(0); // TOP
        mTestPresenter.resume();
        checkTopNormalTouchAndLineDone();

    }

    public void test_touchEvent_TopinvalidDots_ResultTESTFAIL() {

        mMockView.getCoordinate(0); // TOP
        mTestPresenter.resume();
        checkConditionHandleLineFail();

    }

    public void test_touchEvent_RightvalidDots_ResultRETEST() {

        mMockView.getCoordinate(1); // RIGHT
        mTestPresenter.resume();
        checkRightNormalTouchAndLineDone();

    }

    public void test_touchEvent_RightinvalidDots_ResultTESTFAIL() {
        mMockView.getCoordinate(1); // RIGHT
        mTestPresenter.resume();
        checkConditionHandleLineFail();
    }

    public void test_touchEvent_BottomvalidDots_ResultRETEST() {
        mMockView.getCoordinate(2); // BOTTOM
        mTestPresenter.resume();
        checkBottomNormalTouchAndLineDone();
    }

    public void test_touchEvent_TopRight7invalidDots_ResultTESTFAIL() {
        mMockView.getCoordinate(2); // BOTTOM
        mTestPresenter.resume();
        checkConditionHandleLineFail();
    }

    public void test_touchEvent_LeftvalidDots_ResultRETEST() {
        mMockView.getCoordinate(3); // LEFT
        mTestPresenter.resume();
        checkLeftNormalTouchAndLineDone();
    }

    public void test_touchEvent_BottomRight7invalidDots_ResultTESTFAIL() {
        mMockView.getCoordinate(3); // LEFT
        mTestPresenter.resume();
        checkConditionHandleLineFail();

    }

    public void test_touchEvent_MiddleHorizontalvalidDots_ResultRETEST() {
        mMockView.getCoordinate(4); // MIDDLE_HORIZONTAL
        mTestPresenter.resume();
        checkMiddleHorizontalNormalTouchAndLineDone();
    }

    public void test_touchEvent_MiddleHorizontalinvalidDots_ResultTESTFAIL() {
        mMockView.getCoordinate(4); // MIDDLE_HORIZONTAL
        mTestPresenter.resume();
        checkConditionHandleLineFail();
    }

    public void test_touchEvent_MiddleVerticalvalidDots_ResultRETEST() {
        mMockView.getCoordinate(5); // MIDDLE_VERTICAL
        mTestPresenter.resume();
        checkMiddleVerticalNormalTouchAndLineDone();
    }

    public void test_touchEvent_MiddleVerticalinvalidDots_ResultTESTFAIL() {
        mMockView.getCoordinate(5); // MIDDLE_VERTICAL
        mTestPresenter.resume();
        checkConditionHandleLineFail();
    }
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
