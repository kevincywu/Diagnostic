package com.msi.diagnostic.data;

import java.util.ArrayList;

public class MockDiagnoseModel implements IDiagnoseModel {

    private TestLevel mTestLevel;
    private TestCase mTestCase;
    private TestItem mTestItem;
    ArrayList<TestItem> mTestItems;

    public MockDiagnoseModel() {
        mTestItems = new ArrayList<TestItem>();
    }

    @Override
    public int updateTestCase(TestCase testCase) {
        mTestCase = testCase;
        return 1;
    }

    @Override
    public int updateTestItem(TestItem testItem) {
        return 1;
    }

    @Override
    public ArrayList<TestLevel> getTestLevels() {
        ArrayList<TestLevel> testLevels = new ArrayList<TestLevel>();
        testLevels.add(mTestLevel);
        return testLevels;
    }

    @Override
    public ArrayList<TestCase> getTestCases(String testLevelName) {
        ArrayList<TestCase> testCases = new ArrayList<TestCase>();
        testCases.add(mTestCase);
        return testCases;
    }

    @Override
    public ArrayList<TestItem> getTestItems(String testLevelName, String testCaseName) {
        return mTestItems;
    }

    @Override
    public TestLevel loadTestLevel(String testLevelName) {
        return mTestLevel;
    }

    @Override
    public TestCase loadTestCase(String testLevelName, String caseName) {
        return mTestCase;
    }

    @Override
    public TestItem loadTestItem(String testLevelName, String itemName) {
        for (int idx = 0; idx < mTestItems.size(); idx++) {
            if (mTestItems.get(idx).getName().equals(itemName)) {
                mTestItem = mTestItems.get(idx);
                break;
            }
        }
        return mTestItem;
    }

    @Override
    public long addTestLevel(TestLevel testLevel) {
        mTestLevel = testLevel;
        return 0;
    }

    @Override
    public long addTestCase(TestCase testCase) {
        mTestCase = testCase;
        return 0;
    }

    @Override
    public long addTestItem(TestItem testItem) {
        mTestItems.add(testItem);
        return (long) 1;
    }

    @Override
    public long removeTestLevel(TestLevel testLevel) {
        return 1;
    }

    @Override
    public long removeTestCase(TestCase testCase) {
        return 1;
    }

    @Override
    public long removeTestItem(TestItem testItem) {
        return 1;
    }

    @Override
    public void setDataChangedListener(IDataChangedListener listener) {
        throw new UnsupportedOperationException();
    }
}
