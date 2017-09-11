package com.msi.diagnostic.data;

import android.database.Cursor;

public interface IDatabaseSource {
    /* Test-Level Related */
    public long addTestLevel(TestLevel testLevel);

    /* Test-Case Related */
    public long addTestCase(TestCase testCase);
    public int updateTestCase(TestCase testCase);

    /* Test-Item Related */
    public long addTestItem(TestItem testItem);
    public int updateTestItem(TestItem testItem);

    public Cursor queryTestLevels();
    public Cursor queryTestCases(String testLevelName);
    public Cursor queryTestItems(String testLevelName, String testCaseName);

    public Cursor queryTestLevel(String testLevelName);
    public Cursor queryTestCase(String testLevelName, String testCaseName);
    public Cursor queryTestItem(String testLevelName, String testItemName);

    public int deleteTestLevels();
    public int deleteTestCases();
    public int deleteTestItems();

    public int deleteTestLevel(TestLevel testLevel);
    public int deleteTestCase(TestCase testCase);
    public int deleteTestItem(TestItem testItem);
}
