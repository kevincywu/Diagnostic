
package com.msi.diagnostic.app;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.msi.diagnostic.data.DetectionVerifiedInfo;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.ui.IAssy3gModuleTestCaseView;
import com.msi.diagnostic.ui.Assy3gModuleTestCaseView;
import com.msi.diagnostic.utils.CountDownWaitingTimer;
import com.msi.diagnostic.utils.ICountDownListener;
import com.msi.diagnostic.R;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

public final class Assy3gModulePagePresenter extends AbstractTestCasePresenter
        implements ICountDownListener {

    private static class TestItemId {
        public static final int ConnectBaidu = 0;
    }

    private static final String[] TEST_ITEM_NAME = {
            "isConnectBaidu"
    };

    private IAssy3gModuleTestCaseView mThirdGenerationPanelView;
    private Context mContext;
    private TelephonyManager mTelephonyManager;
    private MyPhoneStateListener mPhoneStateListener;
    private WifiManager mWifiManager;
    private CountDownWaitingTimer mWait;
    private Thread mCheckInternetThread;
    private Handler mCheckInternetHandler;
    private int mHttpStatusCode;
    private boolean mExecuteHttpRequest;

    private static final String TAG = "3G";
    private static final String STRING_CONNECT = "Connect to Baidu .....";
    private static final int CONNECT_TIMEOUT_MILLSEC = 20000;
    private static final int SET_STRING_CONNECT = 0;
    private static final int SET_FINISH_ACTIVITY = 1;
    private static final int SET_STRING_FAIL = 2;
    private static String WAIT_SECONDS;
    private static String STRING_FAIL;

    public Assy3gModulePagePresenter(Assy3gModuleTestCaseView view,
            WifiManager wifiManger, TelephonyManager telephonyManager, boolean executeHttpRequest,
            int httpStatusCode) {
        super(view);
        mThirdGenerationPanelView = view;
        mWifiManager = wifiManger;
        mTelephonyManager = telephonyManager;
        mExecuteHttpRequest = executeHttpRequest;
        mHttpStatusCode = httpStatusCode;

        WAIT_SECONDS = view.getDiagnosticApp().getAppContext().
                getString(R.string.tgmodule_count_down_text);
        STRING_FAIL = view.getDiagnosticApp().getAppContext().
                getString(R.string.general_fail);

        init3GTest();
    }

    private void init3GTest() {
        mPhoneStateListener = new MyPhoneStateListener();

        mCheckInternetHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case SET_STRING_CONNECT:
                        mThirdGenerationPanelView.setStatusText(STRING_CONNECT);
                        break;
                    case SET_FINISH_ACTIVITY:
                        mView.finishTestCase();
                        break;
                    case SET_STRING_FAIL:
                        mThirdGenerationPanelView.setStatusText(STRING_FAIL);
                        break;
                }
            }
        };
        mCheckInternetThread = new Thread() {
            @Override
            public void run() {
                boolean result = false;
                Message msgSetConnectStr = new Message();
                Message msgFinishActivity = new Message();
                Message msgSetFailStr = new Message();
                msgSetConnectStr.what = SET_STRING_CONNECT;
                msgFinishActivity.what = SET_FINISH_ACTIVITY;
                msgSetFailStr.what = SET_STRING_FAIL;
                try {
                    int status;
                    mCheckInternetHandler.sendMessage(msgSetConnectStr);
                    if (mExecuteHttpRequest)
                    {
                        HttpGet request = new HttpGet("http://www.baidu.com/");
                        HttpParams httpParameters = new BasicHttpParams();
                        HttpConnectionParams.setConnectionTimeout(httpParameters,
                                CONNECT_TIMEOUT_MILLSEC);
                        HttpClient httpClient = new DefaultHttpClient(
                                httpParameters);
                        HttpResponse response;
                        response = httpClient.execute(request);
                        status = response.getStatusLine().getStatusCode();
                    }
                    else {
                        status = mHttpStatusCode;
                    }
                    if (status == HttpStatus.SC_OK) {
                        result = true;
                        isConnectBaidu(result);
                        mCheckInternetHandler.sendMessage(msgFinishActivity);
                    } else {
                        result = false;
                        isConnectBaidu(result);
                        mCheckInternetHandler.sendMessage(msgFinishActivity);
                    }
                } catch (ClientProtocolException e) {
                    result = false;
                    isConnectBaidu(result);
                    mCheckInternetHandler.sendMessage(msgFinishActivity);
                    e.printStackTrace();
                } catch (IOException e) {
                    result = false;
                    isConnectBaidu(result);
                    mCheckInternetHandler.sendMessage(msgFinishActivity);
                    e.printStackTrace();
                }
                if (result == false) {
                    mCheckInternetHandler.sendMessage(msgSetFailStr);
                }
            }
        };

        mWait = new CountDownWaitingTimer(10000, 1000, this);
        mWait.start();
        mWait.onTick(10000);
    }

    public void setWifi(boolean status) {
            mWifiManager.setWifiEnabled(status);
    }

    public void setTelphonyListenSignal() {
        mTelephonyManager.listen(mPhoneStateListener,
                PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    public void setTelphonyListenNone() {
        mTelephonyManager.listen(mPhoneStateListener,
                PhoneStateListener.LISTEN_NONE);
    }

    @Override
    public void OnButtonClick(int buttonId) {
    }

    @Override
    public void resume() {
        super.resume();
        this.setWifi(false);
        setTelphonyListenSignal();
    }

    @Override
    public void pause() {
        super.pause();
        setTelphonyListenNone();
        this.setWifi(true);
        mWait.cancel();
    }

    private TestResult isConnectBaidu(boolean info) {
        String itemName = TEST_ITEM_NAME[TestItemId.ConnectBaidu];
        TestItem item = getTestItemByName(itemName);
        TestResult result = item.verify(new DetectionVerifiedInfo(info));
        return result;
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        /*
         * Get the Signal strength from the provider, each time there is an
         * update
         */
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
        }
    };

    @Override
    public void onCountDownFinish() {
        mCheckInternetThread.start();
    }

    @Override
    public void onCountDownTick(long millisUntilFinished) {
        int second = (int) millisUntilFinished / 1000;
        String strSecond = Integer.toString(second);
        mThirdGenerationPanelView.setStatusText(WAIT_SECONDS + strSecond);
    }
}
