
package com.msi.diagnostic.ui;

import java.util.ArrayList;

import com.msi.diagnostic.app.MockDataChangedListener;
import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.data.DefinitionTestItem;
import com.msi.diagnostic.data.DiagnoseModel;
import com.msi.diagnostic.data.IDataChangedListener;
import com.msi.diagnostic.data.SQLiteDatabaseSource;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.test.AndroidTestCase;

public class DiagnoseModelTest extends AndroidTestCase
{
    private DiagnoseModel mModel;
    private SQLiteDatabaseSource mSQLDBSource;
    private MockDiagnosticApp mApp;
    private final String mTestLevelPCBA = TestLevel.TYPES[TestLevel.TYPE_PCBA];
    private final String mTestCaseName = "com.msi.diagnostic.ui.BarCodeTestCaseView";
    private final String mTestItemName = "isNumberExisted";
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private final String mVerifiedInfoDefine = "@null";
    @Override
    protected void setUp() throws Exception
    {
        mSQLDBSource = new SQLiteDatabaseSource(getContext());
        mModel = new DiagnoseModel(getContext(), mSQLDBSource);
        mApp = new MockDiagnosticApp(getContext());
        super.setUp();
    }

    public void test_updateTestCase_ValidValue_WithoutResult()
    {
        TestCase testCase = new TestCase(mApp, 0, mTestCaseName, mTestLevelPCBA, null,
                TestResult.create(TestResult.NONE), 0);
        IDataChangedListener listener = new MockDataChangedListener();
        mModel.setDataChangedListener(listener);
        int num = mModel.updateTestCase(testCase);
        boolean isUpdate = false;
        if (num >= 0) {
            isUpdate = true;
        }
        assertTrue(isUpdate);
    }
    public void test_updateTestItem_ValidValue_WithoutResult()
    {
        TestItem testItem = new DefinitionTestItem(mApp, 0, mTestItemName, null, mTestLevelPCBA,
                mTestCaseName, mVerifiedInfoDefine, TestResult.create(TestResult.NONE));
        int num = mModel.updateTestItem(testItem);
        boolean isUpdate = false;
        if (num >= 0) {
            isUpdate = true;
        }
        assertTrue(isUpdate);
    }

    public void test_getTestLevels_ValidValue_WithoutResult()
    {
        ArrayList<TestLevel> list = null;
        list = mModel.getTestLevels();
        assertNotNull(list);
    }

    public void test_getTestCases_ValidValue_WithoutResult()
    {
        ArrayList<TestCase> list = null;
        list = mModel.getTestCases(mTestLevelPCBA);
        assertNotNull(list);
    }

    public void test_getTestItems_ValidValue_WithoutResult()
    {
        ArrayList<TestItem> list = null;
        list = mModel.getTestItems(mTestLevelPCBA, mTestCaseName);
        assertNotNull(list);
    }

    public void test_loadTestLevel_ValidValue_WithoutResult()
    {
        TestLevel testLevel = null;
        testLevel = mModel.loadTestLevel(mTestLevelPCBA);
        assertNotNull(testLevel);
    }

    public void test_loadTestCase_ValidValue_WithoutResult()
    {
        TestCase testCase = null;
        testCase = mModel.loadTestCase(mTestLevelPCBA, mTestCaseName);
        assertNotNull(testCase);
    }

    public void test_loadTestItem_ValidValue_WithoutResult()
    {
        TestItem testItem = null;
        testItem = mModel.loadTestItem(mTestLevelPCBA, mTestItemName);
        assertNotNull(testItem);
    }

    public void test_addTestLevel_ValidValue_WithoutResult()
    {
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelPCBA, mTestLevelCaption);
        long num = mModel.addTestLevel(testLevel);
        boolean isAdd = false;
        if (num >= 0) {
            isAdd = true;
        }
        assertTrue(isAdd);
    }

    public void test_addTestCase_ValidValue_WithoutResult()
    {
        TestCase testCase = new TestCase(mApp, 0, mTestCaseName, mTestLevelPCBA, null,
                TestResult.create(TestResult.NONE), 0);
        long num = mModel.addTestCase(testCase);
        boolean isAdd = false;
        if (num >= 0) {
            isAdd = true;
        }
        assertTrue(isAdd);
    }

    public void test_addTestItem_ValidValue_WithoutResult()
    {
        TestItem testItem = new DefinitionTestItem(mApp, 0, mTestItemName, null, mTestLevelPCBA,
                mTestCaseName, mVerifiedInfoDefine, TestResult.create(TestResult.NONE));
        long num = mModel.addTestItem(testItem);
        boolean isAdd = false;
        if (num >= 0) {
            isAdd = true;
        }
        assertTrue(isAdd);
    }

    public void test_removeTestLevel_ValidValue_WithoutResult()
    {
        TestLevel testLevel = new TestLevel(mApp, 0, mTestLevelPCBA, mTestLevelCaption);
        long num = mModel.removeTestLevel(testLevel);
        boolean isAdd = false;
        if (num > 0) {
            isAdd = true;
        }
        assertTrue(isAdd);
    }

    public void test_removeTestCase_ValidValue_WithoutResult()
    {
        TestCase testCase = new TestCase(mApp, 0, mTestCaseName, mTestLevelPCBA, null,
                TestResult.create(TestResult.NONE), 0);
        long num = mModel.removeTestCase(testCase);
        boolean isAdd = false;
        if (num > 0) {
            isAdd = true;
        }
        assertTrue(isAdd);
    }

    public void test_removeTestItem_ValidValue_WithoutResult()
    {
        TestItem testItem = new DefinitionTestItem(mApp, 0, mTestItemName, null, mTestLevelPCBA,
                mTestCaseName, mVerifiedInfoDefine, TestResult.create(TestResult.NONE));
        long num = mModel.removeTestItem(testItem);
        boolean isAdd = false;
        if (num > 0) {
            isAdd = true;
        }
        assertTrue(isAdd);
    }

    @Override
    protected void tearDown() throws Exception
    {

        super.tearDown();
    }

}
