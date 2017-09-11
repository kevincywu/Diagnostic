package com.msi.diagnostic.data;

import java.util.ArrayList;

public class TestSet {
    private ArrayList<TestLevel> mTestLevels = new ArrayList<TestLevel>();
    private ArrayList<TestCase> mTestCases = new ArrayList<TestCase>();
    private ArrayList<TestItem> mTestItems = new ArrayList<TestItem>();

    public void add(TestObject obj) {

        if (obj instanceof TestLevel) {
            mTestLevels.add((TestLevel) obj);

        } else if (obj instanceof TestCase) {
            mTestCases.add((TestCase) obj);

        } else if (obj instanceof TestItem) {
            mTestItems.add((TestItem) obj);
        }
    }

    public ArrayList<TestLevel> getTestLevels() {
        return mTestLevels;
    }

    public ArrayList<TestCase> getTestCases() {
        return mTestCases;
    }

    public ArrayList<TestItem> getTestItems() {
        return mTestItems;
    }

    public void removeAll() {
        mTestLevels.clear();
        mTestCases.clear();
        mTestItems.clear();
    }
}
