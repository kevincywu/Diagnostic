package com.msi.diagnostic.data;

import android.content.Context;

public class MockDeviceConfig extends DeviceConfig
{
    private static final String mDeviceName = "n912_3g";

    public MockDeviceConfig(Context context) {
        super(context);
    }

    @Override
    public boolean isDeviceSupported()
    {
        return true;
    }

    @Override
    public String getDeviceName()
    {
        return mDeviceName;
    }
}
