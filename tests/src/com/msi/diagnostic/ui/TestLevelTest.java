package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.data.TestLevel;

import android.test.AndroidTestCase;

public class TestLevelTest extends AndroidTestCase {
    private MockDiagnosticApp mApp;
    private final String mTestLevelName = TestLevel.TYPES[TestLevel.TYPE_UNKNOWN];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private TestLevel mTestLevel;
    @Override
    protected void setUp() throws Exception {
        mApp = new MockDiagnosticApp(getContext());
        mTestLevel = new TestLevel(mApp, 0, mTestLevelName, mTestLevelCaption);
        super.setUp();
    }
    public void test_TestLevelMethod() {
        mTestLevel.getTestCases();
        assertEquals("test_level_caption",mTestLevel.getCaption());
        assertEquals(3,mTestLevel.getType());
    }
    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
    }

}
