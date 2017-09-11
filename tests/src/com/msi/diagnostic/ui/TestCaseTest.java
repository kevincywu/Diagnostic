package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.test.AndroidTestCase;

public class TestCaseTest extends AndroidTestCase {
    private MockDiagnosticApp mApp;
    private final String mTestLevel = TestLevel.TYPES[TestLevel.TYPE_UNKNOWN];
    private final String mTestCaseName = "com.msi.diagnostic.ui.PCBAPanelView";
    private TestCase mTestCase;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mApp = new MockDiagnosticApp(getContext());
        mTestCase = new TestCase(mApp, 0, mTestCaseName,
                mTestLevel, null, TestResult.create(TestResult.NONE),
                0);
    }

    public void test_TestCaseMethod() {
        mTestCase.reload();
        mTestCase.initTestItems();
        mTestCase.updateContent();
        mTestCase.verify();
        mTestCase.getTestItems();
        assertEquals(null,mTestCase.getCaption());
        assertEquals(mTestLevel,mTestCase.getParentLevelName());
        assertEquals(0,mTestCase.getTime());
    }
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
