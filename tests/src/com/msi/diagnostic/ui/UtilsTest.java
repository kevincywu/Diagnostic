
package com.msi.diagnostic.ui;

import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.utils.Utils;

public class UtilsTest extends AndroidTestCase
{
    private static final String TAG = "UtilsTest";

    @Override
    protected void setUp() throws Exception
    {
        Log.i(TAG, "setUp...");
    }

    @Override
    protected void tearDown() throws Exception
    {
        Log.d(TAG, "tearDown...");
    }

    public void test_convertToLine()
    {
        String str = "test";
        String converToLine = Utils.convertToLine(str);
        assertEquals("test\n", converToLine);
    }

    public void test_convertToEqual()
    {
        String str1 = "left";
        String str2 = "right";
        String converToEqual = Utils.convertToEqual(str1, str2);
        assertEquals("left=right\n", converToEqual);
    }

    public void test_getClassAsStringName()
    {
        String className = Utils.getClassAsStringName(Utils.class);
        assertEquals("com.msi.diagnostic.utils.Utils", className);
    }

    public void test_getClassByStringName()
    {
        String className = "com.msi.diagnostic.utils.Utils";
        Class<?> getClass = Utils.getClassByStringName(className);
        assertEquals(Utils.class, getClass);
    }
}
