package com.msi.diagnostic.ui;

import com.msi.diagnostic.data.TestItemLoader;

import android.test.AndroidTestCase;

public class TestItemLoaderTest extends AndroidTestCase {
    private TestItemLoader mTestItemLoader;
    @Override
    protected void setUp() throws Exception {
        mTestItemLoader = new TestItemLoader(getContext());
        super.setUp();
    }

    public void test_TestItemLoaderMethod(){
        mTestItemLoader.loadDeviceTestLevels();
    }
    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
    }

}
