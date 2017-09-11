package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.data.DefinitionTestItem;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.test.AndroidTestCase;

public class TestItemTest extends AndroidTestCase {
    private MockDiagnosticApp mApp;
    private final String mTestLevel = TestLevel.TYPES[TestLevel.TYPE_PCBA];
    private final String mTestCaseName = "com.msi.diagnostic.ui.PCBAPanelView";
    private final String mTestItemNameHealth = "isValidHealth";
    private TestItem mTestItem;
    @Override
    protected void setUp() throws Exception {
        final String mVerifiedInfoDefine = "Good";
        mApp = new MockDiagnosticApp(getContext());
        mTestItem = new DefinitionTestItem(mApp, 0, mTestItemNameHealth,
                null, mTestLevel, mTestCaseName,
                mVerifiedInfoDefine, TestResult.create(TestResult.NONE));
        super.setUp();
    }
    public void test_TestItemMethod() {
        mTestItem.updateContent();
        assertEquals(mTestCaseName,mTestItem.getParentCaseName());
        assertEquals(mTestLevel,mTestItem.getParentLevelName());
        assertEquals(0,mTestItem.getType());
    }
    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
    }

}
