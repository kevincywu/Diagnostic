
package com.msi.diagnostic.ui;

import android.database.Cursor;
import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.data.DefinitionTestItem;
import com.msi.diagnostic.data.DiagnoseModel;
import com.msi.diagnostic.data.SQLiteDatabaseSource;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

public class SQLiteDatabaseSourceTest extends AndroidTestCase
{
    private static final String TAG = "SQLiteDatabaseSourceTest";

    private SQLiteDatabaseSource mSQLDBSource;

    private MockDiagnosticApp mApp;

    private DiagnoseModel mModel;

    private final String mTestLevelPCBA = TestLevel.TYPES[TestLevel.TYPE_PCBA];

    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;

    private final String mTestCaseName = "com.msi.diagnostic.ui.BarCodeTestCaseView";

    private final String mTestItemName = "isNumberExisted";

    private final String mVerifiedInfoDefine = "@null";

    @Override
    protected void setUp() throws Exception
    {
        Log.i(TAG, "setUp...");
        mApp = new MockDiagnosticApp(getContext());
        mSQLDBSource = new SQLiteDatabaseSource(getContext());
        mModel = new DiagnoseModel(getContext(), mSQLDBSource);
    }

    @Override
    protected void tearDown() throws Exception
    {
        Log.d(TAG, "tearDown...");
    }

    public void test_addTestCase()
    {
        TestCase testCase = new TestCase(null, 0, mTestCaseName, mTestLevelPCBA, null,
                TestResult.create(TestResult.NONE), 0);
        long result = mSQLDBSource.addTestCase(testCase);
        assertNotNull(result);
    }

    public void test_addTestItem()
    {
        TestItem testItem = new DefinitionTestItem(mApp, 0, mTestItemName, null,
                mTestLevelPCBA, mTestCaseName, mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        long result = mSQLDBSource.addTestItem(testItem);
        assertNotNull(result);
    }

    public void test_addTestLevel()
    {
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelPCBA, mTestLevelCaption);
        long result = mSQLDBSource.addTestLevel(testLevel);
        assertNotNull(result);
    }

    public void test_deleteTestCase()
    {
        TestCase testCase = new TestCase(null, 0, mTestCaseName, mTestLevelPCBA, null,
                TestResult.create(TestResult.NONE), 0);
        int result = mSQLDBSource.deleteTestCase(testCase);
        assertNotNull(result);
    }

    public void test_deleteTestCases()
    {
        int result = mSQLDBSource.deleteTestCases();
        assertNotNull(result);
    }

    public void test_deleteTestItem()
    {
        TestItem testItem = new DefinitionTestItem(mApp, 0, mTestItemName, null,
                mTestLevelPCBA, mTestCaseName, null, null);
        int result = mSQLDBSource.deleteTestItem(testItem);
        assertNotNull(result);
    }

    public void test_deleteTestItems()
    {
        int result = mSQLDBSource.deleteTestItems();
        assertNotNull(result);
    }

    public void test_deleteTestLevel()
    {
        TestLevel testLevel = new TestLevel(null, 0, mTestLevelPCBA, null);
        int result = mSQLDBSource.deleteTestLevel(testLevel);
        assertNotNull(result);
    }

    public void test_deleteTestLevels()
    {
        int result = mSQLDBSource.deleteTestItems();
        assertNotNull(result);
    }

    public void test_queryTestCase()
    {
        Cursor cursor = mSQLDBSource.queryTestCase(mTestLevelPCBA, mTestCaseName);
        assertNotNull(cursor);
    }

    public void test_queryTestCases()
    {
        Cursor cursor = mSQLDBSource.queryTestCases(mTestLevelPCBA);
        assertNotNull(cursor);
    }

    public void test_queryTestItem()
    {
        Cursor cursor = mSQLDBSource.queryTestItem(mTestLevelPCBA, mTestItemName);
        assertNotNull(cursor);
    }

    public void test_queryTestItems()
    {
        Cursor cursor = mSQLDBSource.queryTestItems(mTestLevelPCBA, mTestCaseName);
        assertNotNull(cursor);
    }

    public void test_queryTestLevel()
    {
        Cursor cursor = mSQLDBSource.queryTestLevel(mTestLevelPCBA);
        assertNotNull(cursor);
    }

    public void test_queryTestLevels()
    {
        Cursor cursor = mSQLDBSource.queryTestLevels();
        assertNotNull(cursor);
    }

    public void test_updateTestCase()
    {
        TestCase testCase = new TestCase(null, 0, mTestCaseName, mTestLevelPCBA, null,
                TestResult.create(TestResult.NONE), 0);
        mSQLDBSource.addTestCase(testCase);
        TestCase testcase = mModel.loadTestCase(mTestLevelPCBA, mTestCaseName);
        testcase.setResult(TestResult.FAIL);
        int result = mSQLDBSource.updateTestCase(testcase);
        TestCase tcase = mModel.loadTestCase(mTestLevelPCBA, mTestCaseName);
        assertNotNull(result);
        assertEquals(TestResult.FAIL, tcase.getResult());
    }

    public void test_updateTestItem()
    {
        TestItem testItem = new DefinitionTestItem(mApp, 0, mTestItemName, null,
                mTestLevelPCBA, mTestCaseName, mVerifiedInfoDefine,
                TestResult.create(TestResult.NONE));
        mSQLDBSource.addTestItem(testItem);
        TestItem testitem = mModel.loadTestItem(mTestLevelPCBA, mTestItemName);
        testitem.setResult(TestResult.PASS);
        int result = mSQLDBSource.updateTestItem(testitem);
        TestItem item = mModel.loadTestItem(mTestLevelPCBA, mTestItemName);
        assertNotNull(result);
        assertEquals(TestResult.PASS, item.getResult());
    }
}
