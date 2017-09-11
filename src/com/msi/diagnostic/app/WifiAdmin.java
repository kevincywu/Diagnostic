/**
 * WifiAdmin.java
 *
 * Written by : River Fan
 * Written for: N0J1 MES
 * Date : April 14, 2012
 * Version : 1.0
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

/**
 * This class provide some methods to control wlan
 *
 * @author river
 * @version 1.1
 */
public class WifiAdmin
{
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private List<ScanResult> mWifiList;
    private List<WifiConfiguration> mWifiConfiguration;

    public WifiAdmin(Context context) {
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    public boolean wifiState()
    {
        if (mWifiManager.isWifiEnabled()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * enable wifi
     */
    public void openWifi()
    {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    /**
     * disable wifi
     */
    public void closeWifi()
    {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    public List<WifiConfiguration> GetConfiguration()
    {
        return mWifiConfiguration;
    }

    public void ConnectConfiguration(int index)
    {
        if (index > mWifiConfiguration.size()) {
            return;
        }
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId, true);
    }

    public void StartScan()
    {
        mWifiManager.startScan();
        mWifiList = mWifiManager.getScanResults();
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
    }

    public List<ScanResult> GetWifiList()
    {
        mWifiList = mWifiManager.getScanResults();
        return mWifiList;
    }

    public String getMacAddress()
    {
        mWifiInfo = mWifiManager.getConnectionInfo();
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    public String getBSSID()
    {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    public int getIPAddress()
    {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    public int getNetworkId()
    {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    public String getWifiInfo()
    {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    public int addNetwork(WifiConfiguration wcg)
    {
        int wcgID = mWifiManager.addNetwork(wcg);
        mWifiManager.enableNetwork(wcgID, true);
        return wcgID;
    }

    public void disconnectWifi(int netId)
    {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    /**
     * connect to the open AP
     *
     * @param ssid , the SSID of wifi AP
     * @return the result true or false
     */
    public boolean connectionConfigForNoPSW(String ssid)
    {
        try {
            WifiConfiguration mWifiConfiguration = new WifiConfiguration();
            mWifiConfiguration.hiddenSSID = false;
            mWifiConfiguration.status = WifiConfiguration.Status.ENABLED;
            mWifiConfiguration.allowedAuthAlgorithms.set((WifiConfiguration.AuthAlgorithm.OPEN));
            mWifiConfiguration.allowedKeyManagement.set(KeyMgmt.NONE);
            mWifiConfiguration.allowedProtocols.set(Protocol.WPA);
            mWifiConfiguration.allowedProtocols.set(Protocol.RSN);
            mWifiConfiguration.allowedPairwiseCiphers.set(PairwiseCipher.TKIP);
            mWifiConfiguration.allowedPairwiseCiphers.set(PairwiseCipher.CCMP);
            mWifiConfiguration.allowedGroupCiphers.set(GroupCipher.WEP40);
            mWifiConfiguration.allowedGroupCiphers.set(GroupCipher.WEP104);
            mWifiConfiguration.allowedGroupCiphers.set(GroupCipher.TKIP);
            mWifiConfiguration.allowedGroupCiphers.set(GroupCipher.CCMP);
            mWifiConfiguration.SSID = "\"" + ssid + "\"";
            mWifiConfiguration.preSharedKey = null;
            int res = mWifiManager.addNetwork(mWifiConfiguration);
            return mWifiManager.enableNetwork(res, true);
        } catch (Exception ex) {
            return false;
        }
    }
}
