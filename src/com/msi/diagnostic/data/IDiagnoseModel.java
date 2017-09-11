package com.msi.diagnostic.data;

import java.util.ArrayList;

public interface IDiagnoseModel {
    public int updateTestCase(TestCase testCase);
    public int updateTestItem(TestItem testItem);

    public ArrayList<TestLevel> getTestLevels();
    public ArrayList<TestCase> getTestCases(String testLevelName);
    public ArrayList<TestItem> getTestItems(String testLevelName, String testCaseName);

    public TestLevel loadTestLevel(String testLevelName);
    public TestCase loadTestCase(String testLevelName, String caseName);
    public TestItem loadTestItem(String testLevelName, String itemName);

    public long addTestLevel(TestLevel testLevel);
    public long addTestCase(TestCase testCase);
    public long addTestItem(TestItem testItem);

    public long removeTestLevel(TestLevel testLevel);
    public long removeTestCase(TestCase testCase);
    public long removeTestItem(TestItem testItem);

    public void setDataChangedListener(IDataChangedListener listener);
}
