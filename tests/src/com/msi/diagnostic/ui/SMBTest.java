package com.msi.diagnostic.ui;

import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.app.SMB;

public class SMBTest extends AndroidTestCase {
    private static String TAG = "SMBTest";
    private SMB smb;

    @Override
    protected void setUp() throws Exception {
        Log.d(TAG, "setUp...");
        /**
         * String remoteUrl = "//10.101.0.8/user/queen.rar";
         * String localDir = "/mnt/sdcard";
         */
        String remoteUrl = "test1";
        String localDir = "test2";
        smb = new SMB(remoteUrl, localDir);
    }

    public void test_smbDownload() throws Exception {
        long result = smb.smbDownLoad();
        assertEquals(0, result);
    }
    @Override
    protected void tearDown() throws Exception {
        Log.d(TAG, "tearDown...");
    }

}
