package com.msi.diagnostic.data;

import android.content.Context;
import android.os.Build;

import com.msi.diagnostic.R;

public class DeviceConfig {

    private final Context mContext;
    /* Get the board name for selecting the current testing SKU */
    private static final String mDeviceName = Build.DEVICE;
    public static final String DEFAULT = "default";
    public static final String SPECIAL = "Primo 75";
    public static final String SPECIAL_N915 = "N915";
    public static final String SPECIAL_VIT_T4100 = "VIT T4100";
    public static final String SPECIAL_N71J = "N71J";
    public static final String SPECIAL_N821 = "N821";
    public static final String SPECIAL_N71L = "N71L";
    public static final String SPECIAL_N823 = "N823";
    public static final String SPECIAL_POLARIS_KURIO7 = "polaris-kurio7";

    public DeviceConfig(Context context) {
        mContext = context;
    }

    public boolean isDeviceSupported() {
        /* Get the supported boards for Diagnostic Testing */
        final String[] supportedDevices =
                mContext.getResources().getStringArray(R.array.supported_devices);

        boolean isSupported = false;
        for (String device : supportedDevices) {
            if (mDeviceName.equals(device)) {
                isSupported = true;
                break;
            }
        }
        return isSupported;
    }

    public String getDeviceName() {
        if(!isDeviceSupported()) {
            return DEFAULT;
        }
        else {
            return mDeviceName;
        }
    }
}
