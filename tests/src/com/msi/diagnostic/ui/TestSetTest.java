package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestSet;

import android.test.AndroidTestCase;

public class TestSetTest extends AndroidTestCase {
    private MockDiagnosticApp mApp;
    private final String mTestLevelName = TestLevel.TYPES[TestLevel.TYPE_UNKNOWN];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private TestLevel mTestLevel;
    private TestSet mTestSet;
    @Override
    protected void setUp() throws Exception {
        mApp = new MockDiagnosticApp(getContext());
        mTestLevel = new TestLevel(mApp, 0, mTestLevelName, mTestLevelCaption);
        mTestSet = new TestSet();
        super.setUp();
    }

    public void test_TestSetMethod(){
        mTestSet.add(mTestLevel);
        mTestSet.getTestCases();
        mTestSet.getTestItems();
        mTestSet.getTestLevels();
        mTestSet.removeAll();
    }
    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
    }

}
