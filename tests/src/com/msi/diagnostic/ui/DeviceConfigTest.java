package com.msi.diagnostic.ui;

import com.msi.diagnostic.data.DeviceConfig;

import android.test.AndroidTestCase;

public class DeviceConfigTest extends AndroidTestCase {
private DeviceConfig mDeviceConfig;
    @Override
    protected void setUp() throws Exception {
        mDeviceConfig = new DeviceConfig(getContext());
        super.setUp();
    }

    public void test_GetDeviceName_ValidValue_WithoutResult(){
        String str = mDeviceConfig.getDeviceName();
        boolean isSupported = false;
        if(str.equals("generic")){
            isSupported = true;
        }else if(str.equals("n912_wifi")){
            isSupported = true;
        }else if(str.equals("n912_3g")){
            isSupported = true;
        }else if(str.equals("n912_forerunner")){
            isSupported = true;
        }else if(str.equals("vit-t4000")){
            isSupported = true;
        }else if(str.equals("Primo 75")){
            isSupported = true;
        }else if(str.equals("default")){
            isSupported = true;
        }
        assertTrue(isSupported);
    }

    public void test_IsDeviceSupported_ValidValue_WithoutResult(){
        assertTrue(mDeviceConfig.isDeviceSupported());
    }
    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
    }

}
