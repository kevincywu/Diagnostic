/**
 * WifiAdmin.java
 * 
 * Written by : River Fan Written for: N0J1 MES Date : April 14, 2012 Version :
 * 1.0
 */
package com.msi.diagnostic.app;

import java.util.List;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.GroupCipher;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiConfiguration.PairwiseCipher;
import android.net.wifi.WifiConfiguration.Protocol;
import android.net.wifi.WifiManager.WifiLock;

/**
 * This class provide some methods to control wlan
 * 
 * @author river
 * @version 1.1
 */
public class RuninWifiAdmin {
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private List<ScanResult> mWifiList;
    private List<WifiConfiguration> mWifiConfiguration;
    private WifiLock mWifiLock;

    public RuninWifiAdmin(Context context) {
        mWifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    public int wifiState() {
        return mWifiManager.getWifiState();

    }

    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);

        }
    }

    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    public void acquireWifiLock() {
        mWifiLock.acquire();
    }

    public void releaseWifiLock() {
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }

    public void creatWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("Test");
    }

    public List<WifiConfiguration> getConfiguration() {
        return mWifiConfiguration;
    }

    public void ConnectConfiguration(int index) {
        if (index > mWifiConfiguration.size()) {
            return;
        }
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
                true);
    }

    public void startScan() {
        mWifiManager.startScan();
        mWifiList = mWifiManager.getScanResults();
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
    }

    public List<ScanResult> getWifiList() {
        mWifiList = mWifiManager.getScanResults();
        return mWifiList;
    }

    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++) {
            stringBuilder
                    .append("Index_" + new Integer(i + 1).toString() + ":");
            stringBuilder.append((mWifiList.get(i)).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }

    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    public String getBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    public int getIPAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    public int getNetworkId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    public int getWifiInfoRssi() {
        mWifiInfo = mWifiManager.getConnectionInfo();
        return (mWifiInfo == null) ? 0 : mWifiInfo.getRssi();
    }

    public String getWifiInfo() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    public int addNetwork(WifiConfiguration wcg) {
        int wcgID = mWifiManager.addNetwork(wcg);
        mWifiManager.enableNetwork(wcgID, true);
        return wcgID;
    }

    public void disconnectWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    /*
     * connect to open AP
     */
    public boolean connectionUnConfigForNoPsw(String ssid) {
        /*
         * if remember the AP connect it and return
         */
        if (!mWifiConfiguration.isEmpty()) {
            int num = mWifiConfiguration.size();
            for (int i = 0; i < num; i++) {
                if (mWifiConfiguration.get(i).SSID.equals("\"" + ssid + "\"")) {
                    mWifiManager.enableNetwork(
                            mWifiConfiguration.get(i).networkId, true);
                    mWifiInfo = mWifiManager.getConnectionInfo();
                    return true;
                }
            }
        }
        /*
         * add AP and connect it
         */
        try {
            WifiConfiguration wifiConfiguration = new WifiConfiguration();

            wifiConfiguration.hiddenSSID = false;
            wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
            wifiConfiguration.allowedAuthAlgorithms
                    .set((WifiConfiguration.AuthAlgorithm.OPEN));
            wifiConfiguration.allowedKeyManagement.set(KeyMgmt.NONE);
            wifiConfiguration.allowedProtocols.set(Protocol.WPA);
            wifiConfiguration.allowedProtocols.set(Protocol.RSN);
            wifiConfiguration.allowedPairwiseCiphers.set(PairwiseCipher.TKIP);
            wifiConfiguration.allowedPairwiseCiphers.set(PairwiseCipher.CCMP);
            wifiConfiguration.allowedGroupCiphers.set(GroupCipher.WEP40);
            wifiConfiguration.allowedGroupCiphers.set(GroupCipher.WEP104);
            wifiConfiguration.allowedGroupCiphers.set(GroupCipher.TKIP);
            wifiConfiguration.allowedGroupCiphers.set(GroupCipher.CCMP);
            wifiConfiguration.SSID = "\"" + ssid + "\"";
            wifiConfiguration.preSharedKey = null;

            int networkId = mWifiManager.addNetwork(wifiConfiguration);
            wifiConfiguration.networkId = networkId;
            if (networkId != -1) {
                mWifiManager.enableNetwork(networkId, false);
            }
            mWifiManager.updateNetwork(wifiConfiguration);
            mWifiManager.enableNetwork(networkId, true);
            mWifiInfo = mWifiManager.getConnectionInfo();
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

}
