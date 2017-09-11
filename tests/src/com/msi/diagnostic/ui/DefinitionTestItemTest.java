package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.MockDiagnosticApp;
import com.msi.diagnostic.data.DefinitionTestItem;
import com.msi.diagnostic.data.DefinitionVerifiedInfo;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.data.TestResult;

import android.test.AndroidTestCase;

public class DefinitionTestItemTest extends AndroidTestCase {
private DefinitionTestItem mTestItem;
private MockDiagnosticApp mApp;
private final String mTestLevel = TestLevel.TYPES[TestLevel.TYPE_PCBA];
private final String mTestCaseName = "com.msi.diagnostic.ui.PCBAPanelView";
private final String mTestItemNameHealth = "isValidHealth";
private DefinitionVerifiedInfo mDefinitionVerifiedInfo;
    @Override
    protected void setUp() throws Exception {
        final String mVerifiedInfoDefine = "Good";
        mApp = new MockDiagnosticApp(getContext());
        mDefinitionVerifiedInfo = new DefinitionVerifiedInfo(mTestItemNameHealth);
        mTestItem = new DefinitionTestItem(mApp, 0, mTestItemNameHealth,
                null, mTestLevel, mTestCaseName,
                mVerifiedInfoDefine, TestResult.create(TestResult.NONE));
        super.setUp();
    }

    public void test_verify_ValidValue_WithResultFail(){
        String result = mTestItem.verify(mDefinitionVerifiedInfo).getResultAsString();
        assertEquals("FAIL", result);
    }
    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
    }

}
