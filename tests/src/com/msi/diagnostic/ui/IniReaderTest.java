package com.msi.diagnostic.ui;

import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.app.IniReader;

public class IniReaderTest extends AndroidTestCase {
    private static String TAG = "IniReaderTest";
    private IniReader mIniReader;

    @Override
    protected void setUp() throws Exception {
        Log.d(TAG, "setUp...");
        try {
            /**
             * fileNam為Android模擬器中的一個文件路徑 為unit test測試需要，也
             * 可以自行創建一個文件，然後修改fileName
             */
            String fileName = "/system/build.prop";
            mIniReader = new IniReader(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test_getReadResult() {
        assertFalse(mIniReader.getReadResult());
    }

    @Override
    protected void tearDown() throws Exception {
        Log.d(TAG, "tearDown...");
    }

}
