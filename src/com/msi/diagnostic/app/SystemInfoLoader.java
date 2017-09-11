package com.msi.diagnostic.app;

import java.util.ArrayList;
import java.util.HashMap;

import com.msi.diagnostic.utils.SystemInfoProvider;

public class SystemInfoLoader {

    private ArrayList<HashMap<String, String>> mInfoList;
    private String[] mInfoTitle;
    private ArrayList<String> mInfoContent;
    private String mAppVersion;

    public SystemInfoLoader(String appVersion) {
        mAppVersion = appVersion;
    }

    public ArrayList<HashMap<String, String>> getInfo() {
        String appVersion;
        String osVersion;
        String model;
        String buildNumber;
        String sn;
        String date;
        String storageSize;
        String memorySize;

        mInfoList = new ArrayList<HashMap<String, String>>();

        mInfoTitle = new String[] {
            "App Version", "Date", "OS Version", "Model", "Build Number", "Serial Number",
            "Storage Size", "Memory Size"
        };
        mInfoContent = new ArrayList<String>();

        appVersion = mAppVersion;
        mInfoContent.add(appVersion);
        date = SystemInfoProvider.getDate();
        mInfoContent.add(date);
        osVersion = SystemInfoProvider.getOSVersion();
        mInfoContent.add(osVersion);
        model = SystemInfoProvider.getModel();
        mInfoContent.add(model);
        buildNumber = SystemInfoProvider.getBuildNumber();
        mInfoContent.add(buildNumber);
        sn = SystemInfoProvider.getSerialNumber();
        mInfoContent.add(sn);
        storageSize = SystemInfoProvider.getStorageSize();
        mInfoContent.add(storageSize);
        memorySize = SystemInfoProvider.getMemorySize();
        mInfoContent.add(memorySize);

        settingInfoList();

        return mInfoList;
    }

    private void settingInfoList() {
        for (int i = 0; i < mInfoTitle.length; i++) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("title", mInfoTitle[i]);
            item.put("content", mInfoContent.get(i));
            mInfoList.add(item);
        }
    }
}
