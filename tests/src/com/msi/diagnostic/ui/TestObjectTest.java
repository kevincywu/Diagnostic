package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestObject;

import android.test.AndroidTestCase;

public class TestObjectTest extends AndroidTestCase {
    private MockDiagnosticApp mApp;
    private final String mTestLevelName = TestLevel.TYPES[TestLevel.TYPE_UNKNOWN];
    private final String mTestLevelCaption = TestLevel.Columns.TEST_LEVEL_CAPTION;
    private TestObject mTestObject;
    @Override
    protected void setUp() throws Exception {
        mApp = new MockDiagnosticApp(getContext());
        mTestObject = new TestLevel(mApp, 0, mTestLevelName, mTestLevelCaption);
        super.setUp();
    }
    public void test_TestObjectMethod() {

        mTestObject.setId(0);
        assertEquals("NONE",mTestObject.getResultAsString());
        assertEquals(0,mTestObject.getId());
    }
    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
    }

}
