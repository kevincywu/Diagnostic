package com.msi.diagnostic.app;

import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.utils.CountDownWaitingTimer;
import com.msi.diagnostic.utils.ICountDownListener;
import android.content.Context;
import android.os.AsyncTask;

public class WifiTest extends AsyncTask<String, Void, Boolean> implements
        ICountDownListener {

    private WifiAdmin mWifiAdmin;
    private Context mContext;
    private WifiPagePresenter mWifiPagePresenter;
    private CountDownWaitingTimer mCountDownWaitingTimer;
    private static final int COUNT_DOWN_WAITING_TIME = 20000; // / 20 second
    private static final int COUNT_DOWN_TIME_INTERVAL = 1000; // / 1 second
    private static final String TEST_ITEM_NAME = "isGetMacAddress";

    public WifiTest(Context context, WifiPagePresenter pagePresenter) {
        super();
        mContext = context;
        mWifiPagePresenter = pagePresenter;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        TestItem item = mWifiPagePresenter.getTestItemByName(TEST_ITEM_NAME);
        item.verify(new DetectionVerifiedInfo(result));
        mWifiPagePresenter.pause(true);
    }

    @Override
    protected void onPreExecute() {
        mWifiAdmin = new WifiAdmin(mContext);
        mCountDownWaitingTimer = new CountDownWaitingTimer(
                COUNT_DOWN_WAITING_TIME, COUNT_DOWN_TIME_INTERVAL, this);
        mCountDownWaitingTimer.start();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if (!mWifiAdmin.wifiState()) {
            mWifiAdmin.openWifi();
        }
        judge();
        return true;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        TestItem item = mWifiPagePresenter.getTestItemByName(TEST_ITEM_NAME);
        item.verify(new DetectionVerifiedInfo(false));
        mWifiPagePresenter.pause(true);
    }

    private void cancelLoop() {
        cancel(true);
    }

    private void judge() {

        while (true) {
            if (isCancelled()) {
                break;
            }
            if (mWifiAdmin.wifiState()) {
                String macAddress = mWifiAdmin.getMacAddress(); // get MAC
                                                                // Address
                if ((macAddress != null) && (!macAddress.equals("NULL"))) {
                    break;
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCountDownFinish() {
        cancelLoop();
    }

    @Override
    public void onCountDownTick(long millisUntilFinished) {
    }
}
