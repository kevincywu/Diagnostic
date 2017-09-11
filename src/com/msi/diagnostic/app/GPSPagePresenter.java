
package com.msi.diagnostic.app;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.msi.diagnostic.R;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.data.ThresholdVerifiedInfo;
import com.msi.diagnostic.ui.GPSTestCaseView;
import com.msi.diagnostic.utils.CountDownWaitingTimer;
import com.msi.diagnostic.utils.ICountDownListener;

public final class GPSPagePresenter extends AbstractTestCasePresenter implements
        ICountDownListener {

    private static class TestItemId {
        public static final int CN = 0;
    }

    private static final String[] TEST_ITEM_NAME = {
            "isValidCN"
    };

    public MyLocationListener mLocListener;
    public MyGPSListener mGpsListener;

    private int mGPSStatus = 0;
    private GPSTestCaseView mView;
    private CountDownWaitingTimer mWait;
    private String mFinalMaxCN = null;
    private String mCNLogFilePath;
    private String mSettingSecureString;
    private String mCombineCN;
    private float mCN;
    private ThresholdVerifiedInfo mThresholdInfo;
    private LocationManager mLocationManager;

    private static final boolean LOCAL_LOG = true;
    private static final String TAG_GPS = "gps";
    private static final String CN_FILE_NAME = "CN.log";
    private static final String SETTINGS_PACKAGE = "com.android.settings";
    private static final String SETTINGS_APP_WIDGET_PROVIDER =
            "com.android.settings.widget.SettingsAppWidgetProvider";
    private static final String WIDGET_GPS_SERIAL_NUM = "3";
    private static final long GPS_UPDATE_TIME_INTERVAL = 0;
    private static final float GPS_UPDATE_DISTANCE_INTERVAL = 0;
    private static final boolean BOOLEAN_TRUE = true;
    private static final int RESULT_PASS = TestResult.PASS;
    private static final int RESULT_FAIL = TestResult.FAIL;
    private static final int FAIL_CN_VALUE = 0;
    private static String TAG_DETECT;

    public GPSPagePresenter(GPSTestCaseView view, LocationManager locationManager) {
        super(view);
        mView = view;
        mLocationManager = locationManager;

        TAG_DETECT = view.getDiagnosticApp().getAppContext().
                getString(R.string.gps_detect);

        initGPS();
    }

    private void initGPS() {
        mLocListener = new MyLocationListener();
        mGpsListener = new MyGPSListener();
        mLocationManager.addGpsStatusListener(mGpsListener);
        mWait = new CountDownWaitingTimer(20000, 1000, this);
        mWait.start();
        mWait.onTick(20000);
        mView.showCN(TAG_DETECT);
        mThresholdInfo = new ThresholdVerifiedInfo(0);
        mView.setFailButtonVisiable(View.INVISIBLE);
    }

    @Override
    public void OnButtonClick(int buttonId) {
        switch (buttonId) {
            case R.id.failbutton:
                verifyCN(FAIL_CN_VALUE);
                mView.finishTestCase();
                break;
        }
    }

    @Override
    public void resume() {
        if (LOCAL_LOG)
            Log.d(TAG_GPS, "resume");
        super.resume();
        turnOnGPS(mSettingSecureString);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                GPS_UPDATE_TIME_INTERVAL, GPS_UPDATE_DISTANCE_INTERVAL,
                mLocListener);
    }

    @Override
    public void pause() {
        if (LOCAL_LOG)
            Log.d(TAG_GPS, "pause");
        super.pause();
        mLocationManager.removeUpdates(mLocListener);
        mLocationManager.removeGpsStatusListener(mGpsListener);
    }

    private void turnOnGPS(String settingSecureStr) {
        if (!settingSecureStr.contains(TAG_GPS)) { // if gps is disabled
            final Intent settingGPS = new Intent();
            settingGPS.setClassName(SETTINGS_PACKAGE,
                    SETTINGS_APP_WIDGET_PROVIDER);
            settingGPS.addCategory(Intent.CATEGORY_ALTERNATIVE);
            settingGPS.setData(Uri.parse(WIDGET_GPS_SERIAL_NUM));
            Activity gpsActivity = mView.getActivity();
            gpsActivity.sendBroadcast(settingGPS);
        }
    }

    public void setSettingSecureString(String settingSecureString) {
        mSettingSecureString = settingSecureString;
    }

    private void turnOffGPS(String settingSecureStr) {
        if (settingSecureStr.contains(TAG_GPS)) { // if gps is enabled
            final Intent settingGPS = new Intent();
            settingGPS.setClassName(SETTINGS_PACKAGE,
                    SETTINGS_APP_WIDGET_PROVIDER);
            settingGPS.addCategory(Intent.CATEGORY_ALTERNATIVE);
            settingGPS.setData(Uri.parse(WIDGET_GPS_SERIAL_NUM));
            Activity gpsActivity = mView.getActivity();
            gpsActivity.sendBroadcast(settingGPS);
        }
    }

    private TestResult verifyCN(float cn_value) {
        mThresholdInfo.mInfo = cn_value;
        String itemName = TEST_ITEM_NAME[TestItemId.CN];
        TestItem item = getTestItemByName(itemName);
        TestResult result = item.verify(mThresholdInfo);

        return result;
    }

    public int getGPSStatus() {
        return mGPSStatus;
    }

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location arg0) {
            // Do nothing
        }

        public void onProviderDisabled(String arg0) {
            // Do nothing
        }

        public void onProviderEnabled(String arg0) {
            // Do nothing
        }

        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            // Do nothing
        }
    }

    public class MyGPSListener implements GpsStatus.Listener {

        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    if (BOOLEAN_TRUE)
                        Log.v(TAG_GPS, "GPS_EVENT_SATELLITE_STATUS");
                    mGPSStatus = GpsStatus.GPS_EVENT_SATELLITE_STATUS;
                    GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
                    Iterable stats = gpsStatus.getSatellites();
                    Iterator statsI = stats.iterator();
                    List<Float> satelliteCNList = new ArrayList<Float>();
                    int satelliteSize;
                    float maxCN;
                    String maxCNStr = null;
                    TestResult testResult;
                    int result;
                    while (statsI.hasNext()) {
                        GpsSatellite gpssatellite = (GpsSatellite) statsI.next();
                        mCN = gpssatellite.getSnr();
                        satelliteCNList.add(mCN);
                    }
                    Collections.sort(satelliteCNList);
                    satelliteSize = satelliteCNList.size();
                    int lastSatelliteIndex = satelliteSize - 1;
                    if(satelliteSize != 0) {
                        maxCN = satelliteCNList.get(lastSatelliteIndex);
                        maxCNStr = Float.toString(maxCN);
                    } else {
                        maxCN = 0;
                        maxCNStr = "N/A";
                    }
                    testResult = verifyCN(maxCN);
                    result = testResult.getResult();
                    if (result == RESULT_PASS && satelliteSize != 0) {
                        if (mFinalMaxCN == null) {
                            mFinalMaxCN = maxCNStr;
                            mCombineCN = mFinalMaxCN;
                            mView.setCNTextColor(Color.RED);
                            mView.showCN(mCombineCN);
                            writeCNFile();

                            /** Wait 2 seconds to close activity start **/
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mView.finishTestCase();
                                }
                            }, 2000);
                            /** Wait 2 seconds to close activity end **/
                        }
                    } else if (result == RESULT_FAIL && satelliteSize != 0) {
                        if (mFinalMaxCN == null) {
                            String tempCombineCN = maxCNStr;
                            mView.setCNTextColor(Color.BLACK);
                            mView.showCN(tempCombineCN);
                        }
                    }
                    break;
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    if (BOOLEAN_TRUE)
                        Log.v(TAG_GPS, "GPS_EVENT_FIRST_FIX");
                    mGPSStatus = GpsStatus.GPS_EVENT_FIRST_FIX;
                    break;
                case GpsStatus.GPS_EVENT_STARTED:
                    if (BOOLEAN_TRUE)
                        Log.v(TAG_GPS, "GPS_EVENT_STARTED");
                    mGPSStatus = GpsStatus.GPS_EVENT_STARTED;
                    break;
                case GpsStatus.GPS_EVENT_STOPPED:
                    if (BOOLEAN_TRUE)
                        Log.v(TAG_GPS, "GPS_EVENT_STOPPED");
                    mGPSStatus = GpsStatus.GPS_EVENT_STOPPED;
                    break;
            }
        }

        private void writeCNFile() {
            FileOutputStream output = null;
            String CNLogFolderPath = Environment.getExternalStorageDirectory()
                    .getPath();
            mCNLogFilePath = getFilePath(CNLogFolderPath);
            try {
                output = new FileOutputStream(mCNLogFilePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte mergebyte[] = mCombineCN.getBytes();
            try {
                output.write(mergebyte);
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String getFilePath(String folderPath) {
            String filePath;
            filePath = folderPath + "/" + CN_FILE_NAME;
            return filePath;
        }
    }

    @Override
    public void onCountDownFinish() {
        mView.setCountDownVisiable(View.INVISIBLE);
        mView.setFailButtonVisiable(View.VISIBLE);
    }

    @Override
    public void onCountDownTick(long millisUntilFinished) {
        int tick = (int) (millisUntilFinished / 1000);
        String tickStr = Integer.toString(tick);
        mView.setCountDownText(tickStr);
    }
}
