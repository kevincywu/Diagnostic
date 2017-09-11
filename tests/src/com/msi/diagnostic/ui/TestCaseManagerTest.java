package com.msi.diagnostic.ui;

import android.test.AndroidTestCase;
import android.util.Log;

import com.msi.diagnostic.app.TestCaseManager;
import com.msi.diagnostic.data.DiagnoseModel;
import com.msi.diagnostic.data.SQLiteDatabaseSource;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestLevel;

public class TestCaseManagerTest extends AndroidTestCase {
    private static String TAG = "TestCasemanagerTest";
    private TestCaseManager mTestCaseManager;
    private DiagnoseModel model;
    private final String levelName = TestLevel.TYPES[TestLevel.TYPE_PCBA];

    @Override
    protected void setUp() throws Exception {
        Log.d(TAG, "setUp...");
        SQLiteDatabaseSource source = new SQLiteDatabaseSource(getContext());
        model = new DiagnoseModel(getContext(), source);
        mTestCaseManager = new TestCaseManager(model, levelName);
    }

    public void test_setLocation() {
        int location = 1;
        mTestCaseManager.setLocation(location);
        assertEquals(location, mTestCaseManager.getLocation());
    }

    public void test_getList() {
        assertNotNull(mTestCaseManager.getList());
    }

    public void test_getFirst() {
        assertNotNull(mTestCaseManager.getFirst());
    }

    public void test_getLast() {
        assertNotNull(mTestCaseManager.getLast());
    }

    public void test_getTestCase_withParameter_Long() {
        // 獲取TestCases中的第一個TestCase的ID
        long id = mTestCaseManager.getList().get(0).getId();
        assertNotNull(mTestCaseManager.getTestCase(id));
    }

    public void test_getTestCase_withParameter_String() {
        String testCaseName = "com.msi.diagnostic.ui.WifiTestCaseView";
        assertNotNull(mTestCaseManager.getTestCase(testCaseName));
    }

    public void test_getTestCase_withParameter_int() {
        int location = 1;
        assertNotNull(mTestCaseManager.getTestCase(location));
    }

    public void test_getTestCase_withParameter_TestCase() {
        TestCase testcase = new TestCase(
                null,
                "com.msi.diagnostic.ui.BarCodeTestCaseView",
                null,
                null,
                null);
        assertNotNull(mTestCaseManager.getTestCase(testcase));
    }

    public void test_current() throws Exception {
        mTestCaseManager.setLocation(1);
        assertNotNull(mTestCaseManager.current());
    }

    public void test_previous() {
        mTestCaseManager.setLocation(0);
        assertNotNull(mTestCaseManager.previous());
        assertEquals(0, mTestCaseManager.getLocation());
        mTestCaseManager.setLocation(2);
        TestCase testCase = mTestCaseManager.previous();
        mTestCaseManager.setLocationByTestCase(testCase);
        assertNotNull(testCase);
        assertEquals(1, mTestCaseManager.getLocation());
    }

    public void test_next() throws Exception {
        mTestCaseManager.setLocation(0);
        assertNotNull(mTestCaseManager.next());
        assertEquals(1, mTestCaseManager.getLocation());
        int testCaseNum = mTestCaseManager.getList().size();
        mTestCaseManager.setLocation(testCaseNum - 1);
        assertNotNull(mTestCaseManager.next());
        assertEquals(0, mTestCaseManager.getLocation());
    }

    public void test_setLocationByTestCase() {
        mTestCaseManager.setLocation(2);
        mTestCaseManager.setLocationByTestCase(mTestCaseManager.next());
        assertEquals(3, mTestCaseManager.getLocation());
    }

    public void test_reset() {
        mTestCaseManager.reset();
        assertEquals(0, mTestCaseManager.getLocation());
    }

    public void test_isFirst() {
        mTestCaseManager.setLocation(0);
        assertTrue(mTestCaseManager.isFirst());
        mTestCaseManager.setLocation(2);
        assertFalse(mTestCaseManager.isFirst());
    }

    public void test_isLase() {
        int testCaseNum = mTestCaseManager.getList().size();
        mTestCaseManager.setLocation(testCaseNum - 1);
        assertTrue(mTestCaseManager.isLast());
        mTestCaseManager.setLocation(2);
        assertFalse(mTestCaseManager.isLast());
    }

    @Override
    protected void tearDown() throws Exception {
        Log.d(TAG, "tearDown...");
    }

}
