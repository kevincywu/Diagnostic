package com.msi.diagnostic.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import com.msi.diagnostic.R;
import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.ui.RuninWlanTestCaseView;

public class RuninWlanPagePresenter extends AbstractTestCasePresenter {

    private Context mContext;

    private RuninWlanTestCaseView mView;
    private RuninWifiAdmin mWifiAdmin;
    public static final boolean LOCAL_LOGD = true;
    public final String SETTING_INFO = "setInfo";
    public final int JUDGE = 0;
    public final int GETRSSI = 1;
    public Boolean isSignalStrengthPass = false;
    public Boolean isCopyFilePass = false;
    private String mServerFilePath = "";
    private int mRssi;
    private ArrayList<String> apNameList = new ArrayList<String>();
    private List<ScanResult> mWifiList;
    public static final String TAG = "RininWlan";

    private static final String[] TEST_ITEM_NAME = {
            "isSignalStrengthPass", "isCopyFilePass", "isNumberExisted"
    };

    private static final String[] SERVER_FILE_PATH = {
            "//10.101.0.8/user/queen.rar", "//10.101.0.10/user/queen.rar"
    };

    private int mServerFilePathIndex = 0;
    private static final int JUDEG_TIME_OUT = 20; //search server and judge 20s per server;
    private int mJudgeTimes = 0;
    private ArrayList<String> mServerFilePaths = new ArrayList<String>();;

    private static class TestItemId {
        public static final int SIGNAL_STRENGTH = 0;
        public static final int COPY_FILE = 1;
        public static final int IS_NUMBEREXISTED = 2;
    }

    public RuninWlanPagePresenter(RuninWlanTestCaseView view) {
        super(view);
        mView = view;
        mContext = view.getAppContext();
        mWifiAdmin = new RuninWifiAdmin(mContext);
        wlanTest();
    }

    @Override
    public void OnButtonClick(int buttonId) {
        switch (buttonId) {
        case R.id.button1:
            writeToDB(isSignalStrengthPass, isCopyFilePass);
            handler.removeMessages(JUDGE);
            finish();
            break;

        default:
            break;
        }
    }

    private void finish() {
        SharedPreferences settings = mContext.getSharedPreferences(
                SETTING_INFO, 0);
        // if the activity has called remember set the state true
        settings.edit().putBoolean("state", true).commit();
        try {
            mContext.unregisterReceiver(mReceiver); // cancel
                                                               // broadcast
                                                               // receiver
        } catch (Exception e) {
            e.printStackTrace();
        }
        mView.finishTestCase();
    }

    /*
     * start wlan test
     */
    public void wlanTest() {
        if (mWifiAdmin.wifiState() == WifiManager.WIFI_STATE_DISABLED) {
            mWifiAdmin.openWifi();
            String openInfo = mView.getDiagnosticApp().getAppContext().
                    getString(R.string.runin_wifi_openwifi_info);
            mView.setWifiStateInfo(openInfo);
        } else {
            String wifiInfo = mView.getDiagnosticApp().getAppContext().
                    getString(R.string.runin_wifi_openinfo);
            mView.setWifiStateInfo(wifiInfo);
        }
        try {
            registerBroadcast();
            getTestInformation();
        } catch (Exception e1) {
            if (LOCAL_LOGD) {
                Log.e(TAG, "serverFilepath||rssi error");
            }
//            mServerFilePath = "//10.101.0.8/user/queen.rar";
            int length = SERVER_FILE_PATH.length;
            for (int i = 0; i < length; ++i) {
                mServerFilePaths.add(SERVER_FILE_PATH[i]);
            }
            mRssi = -75;
            if (LOCAL_LOGD) {
                Log.e(TAG, e1.toString());
            }
            e1.printStackTrace();
        }
        mServerFilePath = mServerFilePaths.get(mServerFilePathIndex);
        if (LOCAL_LOGD) {
            Log.d(TAG, "serverFilepath= " + mServerFilePath + "   rssi ="
                    + mRssi);
        }
        Message mMessage = new Message();
        mMessage.what = JUDGE;
        handler.sendMessageDelayed(mMessage, 1000);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case JUDGE:
                judge();
                break;

            case GETRSSI:
                getRssi();
                break;

            default:
                break;
            }
        }
    };

    private void getTestInformation() {
        XmlResourceParser XmlResParser = mView
                .getXmlResourceParser(R.xml.ip_list);
        int eventType;
        try {
            eventType = XmlResParser.getEventType();
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                switch (eventType) {
                case XmlResourceParser.START_TAG:
                    String tagName = XmlResParser.getName();
                    if (("serverFilePath").equals(tagName)) {
//                        mServerFilePath = XmlResParser.nextText();
                        String  serverFilePath = XmlResParser.nextText();
                        mServerFilePaths.add(serverFilePath);
                    } else if (("rssi").equals(tagName)) {
                        String rssi = XmlResParser.nextText();
                        mRssi = Integer.parseInt(rssi);
                    } else if (("AP").equals(tagName)) {
                        String apName = XmlResParser.nextText();
                        apNameList.add(apName);
                    }
                    break;

                default:
                    break;
                }
                eventType = XmlResParser.next();
            }
        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void judge() {
        handler.removeMessages(JUDGE);
        if (mWifiAdmin.wifiState() == WifiManager.WIFI_STATE_ENABLED) {
            String wifiInfo = mView.getDiagnosticApp().getAppContext().
                    getString(R.string.runin_wifi_info);
            mView.setWifiStateInfo(wifiInfo);
            mWifiAdmin.startScan();
            mWifiList = mWifiAdmin.getWifiList();
            if ((mWifiList == null) || (mWifiList.size() == 0)) {
                mView.setFinishButton(View.VISIBLE);
                Message message = new Message();
                message.arg1 = JUDGE;
                handler.sendMessageDelayed(message, 1000);
                String apErrorInfo = mView.getDiagnosticApp().getAppContext()
                        .getString(R.string.runin_wifi_error_info);
                mView.setApInfo(apErrorInfo);
                return;
            }
            mView.setFinishButton(View.GONE);
            String APname = getAP(mWifiList);
            if (APname == "error") {
                if (mServerFilePathIndex >= SERVER_FILE_PATH.length - 1) {
                    mView.setFinishButton(View.VISIBLE);
                }
                if (mJudgeTimes < JUDEG_TIME_OUT) {
                    mJudgeTimes++;
                    Message mMessage = new Message();
                    mMessage.what = JUDGE;
                    handler.sendMessageDelayed(mMessage, 1000);
                }
                if (mServerFilePathIndex < SERVER_FILE_PATH.length - 1
                        && mJudgeTimes >= JUDEG_TIME_OUT) {
                    mJudgeTimes = 0;
                    mServerFilePathIndex++;
                    wlanTest();
                }
                String apInfo = mView.getDiagnosticApp().getAppContext()
                        .getString(R.string.runin_wifi_ap_info);
                mView.setApInfo(apInfo);
                return;
            }

            if (mWifiAdmin.connectionUnConfigForNoPsw(APname) == true) {
                String apConnectInfo = mView.getDiagnosticApp().getAppContext()
                        .getString(R.string.runin_wifi_connect) + APname;
                mView.setApInfo(apConnectInfo);
                mView.setApInfoTextColor(Color.GREEN);
                Message message = new Message();
                message.what = GETRSSI;
                handler.sendMessageDelayed(message, 1000);
            } else {
                String apConnectInfo = mView.getDiagnosticApp().getAppContext()
                        .getString(R.string.runin_wifi_connect_error) + APname;
                mView.setApInfo(apConnectInfo);
                mView.setApInfoTextColor(Color.RED);
                mView.setFinishButton(View.VISIBLE);
            }
        } else {
            mWifiAdmin.openWifi();
            mView.setWifiStateInfo(R.string.runin_wifi_opening);
            if (LOCAL_LOGD) {
                Log.d(TAG, "wifi not open wait");
            }
            Message message = new Message();
            message.what = JUDGE;
            handler.sendMessageDelayed(message, 1000);
        }
    }

    /*
     * get the open and strong AP
     */
    public String getAP(List<ScanResult> list) {
        String APname = "error";// test AP ssid
        int strength = mRssi;
        if (LOCAL_LOGD) {
            Log.d(TAG, "strength==" + strength);
            Log.d(TAG, "getAP list=" + list);
        }
        try {
            for (int i = 0; i < list.size(); i++) {
                ScanResult mScanResult = list.get(i);
                if (LOCAL_LOGD) {
                    Log.d(TAG, "AP=" + mScanResult.SSID);
                    Log.d(TAG, "apNameList==" + apNameList);
                }
                if (apNameList.contains(mScanResult.SSID)) {
                    // if the AP is open and strength is ok
                    if (LOCAL_LOGD) {
                        Log.d(TAG, "contain it");
                    }
                    if ((mScanResult.level > strength)) {
                        strength = mScanResult.level;
                        APname = mScanResult.SSID;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "contain it");
            APname = "error";
        }
        return APname;
    }

    public void getRssi() {
        handler.removeMessages(GETRSSI);
        int rssi = 0;
        rssi = mWifiAdmin.getWifiInfoRssi();
        if ((rssi > mRssi) && (rssi < 0)) {
            isSignalStrengthPass = true;
            mView.setFinishButton(View.GONE);
            String rssiInfo = mView.getDiagnosticApp().getAppContext()
                    .getString(R.string.runin_wifi_RSSI)
                    + " = "
                    + rssi
                    + mView.getDiagnosticApp().getAppContext().getString(R.string.runin_wifi_pass);
            mView.setRssiInfo(rssiInfo);
            startDownLoadAsyncTask();
        } else {
            String rssiInfo = "RSSI too weak " + rssi;
            mView.setRssiInfo(rssiInfo);
            mView.setRssiInfoTextColor(Color.RED);
            mView.setFinishButton(View.VISIBLE);

            Message mMessage = new Message();
            mMessage.what = GETRSSI;
            handler.sendMessageDelayed(mMessage, 1000);
        }
    }

    /**
     * register Broadcast
     */
    public void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("DownLoadState");
        mContext.registerReceiver(mReceiver, filter);
    }

    /**
     * Broadcast Receiver
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("DownLoadState")) {
                String state = intent.getStringExtra("State");
                if (LOCAL_LOGD) {
                    Log.d(TAG, "mReceiver get action");
                }
                mView.setResultInfo(state);
                if (state.contains("pass")) {
                    isCopyFilePass = true;
                    if (LOCAL_LOGD) {
                        Log.d(TAG, "write Log PASS");
                    }
                    writeToDB(isSignalStrengthPass, isCopyFilePass);
                    finish();
                } else if (state.contains("fail") && state.contains("3")) {
                    if (mServerFilePathIndex >= SERVER_FILE_PATH.length - 1) {
                        isCopyFilePass = false;
                        writeToDB(isSignalStrengthPass, isCopyFilePass);
                        finish();
                    } else {
                        mServerFilePathIndex++;
                        wlanTest();
                    }
                }
            }
        }
    };

    private void startDownLoadAsyncTask() {
        DownLoadTask downLoadTask = new DownLoadTask(mServerFilePath,
                mView);
        downLoadTask.execute();
    }

    private void writeToDB(Boolean signalStrengthPass, Boolean copyFilePass) {
        String itemName1 = TEST_ITEM_NAME[TestItemId.SIGNAL_STRENGTH];
        String itemName2 = TEST_ITEM_NAME[TestItemId.COPY_FILE];
        TestItem signalStrength = getTestItemByName(itemName1);
        TestItem copyFile = getTestItemByName(itemName2);
        signalStrength.verify(new DetectionVerifiedInfo(signalStrengthPass));
        copyFile.verify(new DetectionVerifiedInfo(copyFilePass));
    }

    @Override
    public void pause() {
        handler.removeMessages(JUDGE);
        super.pause();

    }

    @Override
    public void resume() {
        super.resume();
        mServerFilePathIndex = 0;
        String itemName = TEST_ITEM_NAME[TestItemId.IS_NUMBEREXISTED];
        TestItem item = getTestItemByName(itemName);
        TestResult result = item.verify(new DetectionVerifiedInfo(true));
    }

}
