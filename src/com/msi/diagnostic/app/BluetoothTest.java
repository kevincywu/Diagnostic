package com.msi.diagnostic.app;

import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.utils.CountDownWaitingTimer;
import com.msi.diagnostic.utils.ICountDownListener;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;

public class BluetoothTest extends AsyncTask<String, Void, Boolean> implements
        ICountDownListener {

    private BluetoothAdapter bluetoothDefaultAdapter;
    private BluetoothPagePresenter mBluetoothPagePresenter;
    private static int mNumber = 0;
    private CountDownWaitingTimer mCountDownWaitingTimer;
    private Context mContext;
    private static final String TEST_ITEM_NAME = "isFoundDevice";
    private static final int COUNT_DOWN_WAITING_TIME = 20000; // / 20 second
    private static final int COUNT_DOWN_TIME_INTERVAL = 1000; // / 1 second

    public BluetoothTest(Context context, BluetoothPagePresenter pagePresenter) {
        super();
        mBluetoothPagePresenter = pagePresenter;
        mContext = context;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        TestItem item = mBluetoothPagePresenter
                .getTestItemByName(TEST_ITEM_NAME);
        item.verify(new DetectionVerifiedInfo(result));
        mContext.unregisterReceiver(mReceiver);
        mBluetoothPagePresenter.pause(true);
    }

    @Override
    protected void onPreExecute() {
        bluetoothDefaultAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mContext.registerReceiver(mReceiver, filter);
        mCountDownWaitingTimer = new CountDownWaitingTimer(
                COUNT_DOWN_WAITING_TIME, COUNT_DOWN_TIME_INTERVAL, this);
        mCountDownWaitingTimer.start();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {

        while (mNumber == 0) {
            if (isCancelled()) {
                break;
            }
            if (bluetoothDefaultAdapter == null) {
                cancelLoop();
                break;
            } else {
                if (bluetoothDefaultAdapter.isEnabled()) {
                    bluetoothDefaultAdapter.startDiscovery();
                } else {
                    bluetoothDefaultAdapter.enable();
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        TestItem item = mBluetoothPagePresenter
                .getTestItemByName(TEST_ITEM_NAME);
        item.verify(new DetectionVerifiedInfo(false));
        mContext.unregisterReceiver(mReceiver);
        mBluetoothPagePresenter.pause(true);
    }

    private void cancelLoop() {
        cancel(true);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                if (mNumber == 0) {
                    mCountDownWaitingTimer.cancel();
                    mNumber++;
                }
            }
        }
    };

    @Override
    public void onCountDownFinish() {
        cancelLoop();
    }

    @Override
    public void onCountDownTick(long millisUntilFinished) {
    }
}
