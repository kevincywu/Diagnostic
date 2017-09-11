
package com.msi.diagnostic.ui;

import java.io.File;

import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.app.TestCaseManager;
import com.msi.diagnostic.app.logger.LogManager;
import com.msi.diagnostic.data.DiagnoseModel;
import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.SQLiteDatabaseSource;
import com.msi.diagnostic.data.TestLevel;

public class LogManagerTest extends AndroidTestCase
{
    private static String TAG = "LogManagerTest";

    private LogManager mLogManager;

    private TestCaseManager mTestCaseManager;

    private IDiagnoseModel mModel;

    private static final String levelName = TestLevel.TYPES[TestLevel.TYPE_ASSY];

    @Override
    protected void setUp() throws Exception
    {
        Log.i(TAG, "setUp...");
        SQLiteDatabaseSource source = new SQLiteDatabaseSource(getContext());
        mModel = new DiagnoseModel(getContext(), source);
        mTestCaseManager = new TestCaseManager(mModel, levelName);
        mLogManager = new LogManager(mTestCaseManager);
    }

    @Override
    protected void tearDown() throws Exception
    {
        Log.d(TAG, "tearDown...");
    };

    public void test_getLog()
    {
        String s = "test";
        String log = mLogManager.getLog(s);
        assertNotSame("", log);
    }

    public void test_savaToFile()
    {
        String fileName = Environment.getExternalStorageDirectory().getPath() + "/default_log";
        boolean result = mLogManager.saveToFile(null, "test");
        File file = new File(fileName);
        assertTrue(result);
        assertTrue(file.exists());
    }
}
