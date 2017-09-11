package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.GPSPagePresenter;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.app.GPSPagePresenter.MyGPSListener;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MockThresholdTestItem;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.content.Context;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.test.AndroidTestCase;
import android.util.Log;

public class GPSTest extends AndroidTestCase {
    private static final String TAG = "GPSTest";

    private LocationManager mLocationManager;
    private MockGPSPanelView mMockView;
    private GPSPagePresenter mPresenter;
    private MockDiagnosticApp mApp;
    private IDiagnoseModel mModel;

    private final String mTestLevelPCBA = TestLevel.TYPES[TestLevel.TYPE_PCBA];
    private final String mTestLevelASSY = TestLevel.TYPES[TestLevel.TYPE_ASSY];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mTestCaseName = "com.msi.diagnostic.ui.GPSTestCaseView";
    private final String mTestItemName = "isValidCN";
    private final float mMaxValue = 1000;
    private final float mMinValue = 27;

    protected void setUp() throws Exception {
        Log.i(TAG, "setUp...");

        mApp = new MockDiagnosticApp(getContext());
        mModel = mApp.getDiagnoseModel();
        mMockView = new MockGPSPanelView(mApp);
    }

    public void test_handlePCBAItem_ValidValue_ResultPass() {
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelPCBA, mTestLevelCaption);
        TestCase testCase = new TestCase(mApp, 0, mTestCaseName,
                mTestLevelPCBA, null, TestResult.create(TestResult.NONE), 0);
        TestItem testItem = new MockThresholdTestItem(mApp, 0, mTestItemName,
                null, mTestLevelPCBA, mTestCaseName, mMaxValue, mMinValue,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItem);

        mLocationManager = (LocationManager) this.getContext()
                .getSystemService(Context.LOCATION_SERVICE);
        mPresenter = new GPSPagePresenter(mMockView, mLocationManager);
        mPresenter.setSettingSecureString("gps");
        mPresenter.resume();

        MyGPSListener gpsListener = mPresenter.mGpsListener;
        gpsListener.onGpsStatusChanged(GpsStatus.GPS_EVENT_STARTED);
        int status = mPresenter.getGPSStatus();
        assertEquals(GpsStatus.GPS_EVENT_STARTED, status);
    }

    public void test_handleASSYItem_ValidValue_ResultPass() {
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelASSY, mTestLevelCaption);
        TestCase testCase = new TestCase(mApp, 0, mTestCaseName,
                mTestLevelASSY, null, TestResult.create(TestResult.NONE), 0);
        TestItem testItem = new MockThresholdTestItem(mApp, 0, mTestItemName,
                null, mTestLevelASSY, mTestCaseName, mMaxValue, mMinValue,
                TestResult.create(TestResult.NONE));
        mModel.addTestLevel(testLevel);
        mModel.addTestCase(testCase);
        mModel.addTestItem(testItem);

        mLocationManager = (LocationManager) this.getContext()
                .getSystemService(Context.LOCATION_SERVICE);
        mPresenter = new GPSPagePresenter(mMockView, mLocationManager);
        mPresenter.setSettingSecureString("gps");
        mPresenter.resume();

        MyGPSListener gpsListener = mPresenter.mGpsListener;
        gpsListener.onGpsStatusChanged(GpsStatus.GPS_EVENT_STARTED);
        int status = mPresenter.getGPSStatus();
        assertEquals(GpsStatus.GPS_EVENT_STARTED, status);
    }

    protected void tearDown() throws Exception {
        mPresenter = null;
        mMockView = null;
        Log.d(TAG, "tearDown...");
    }
}
