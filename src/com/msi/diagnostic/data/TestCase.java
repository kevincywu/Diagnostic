
package com.msi.diagnostic.data;

import java.util.ArrayList;

import com.msi.diagnostic.app.IDiagnosticApp;
import com.msi.diagnostic.utils.Utils;

import android.provider.BaseColumns;

public final class TestCase extends TestObject {

    private IDiagnosticApp mApp;

    private final String mParentLevelName;
    private final String mCaption;

    private Class<?> mTargetClass;
    private long mTime;

    private ArrayList<TestItem> mTestItems;
    private MapTestSet<String, TestItem> mMapTestItems;

    public TestCase(
            IDiagnosticApp app,
            long id,
            String testCaseName,
            String parentLevelIName,
            String caption,
            TestResult result,
            long time) {

        super(id, testCaseName);
        mApp = app;
        mParentLevelName = parentLevelIName;
        mCaption = caption;
        mTargetClass = Utils.getClassByStringName(testCaseName);
        mResult = result;
        mTime = time;
    }

    public TestCase(
            IDiagnosticApp app,
            String testCaseName,
            String parentLevelName,
            String caption,
            TestResult result) {

        this(app, NONE_ID, testCaseName, parentLevelName, caption, result, 0);
    }

    private MapTestSet<String, TestItem> mapTestItems(IDiagnoseModel model) {
        MapTestSet<String, TestItem> maps = new MapTestSet<String, TestItem>();

        ArrayList<TestItem> items = model.getTestItems(mParentLevelName, mName);
        for (TestItem item : items) {
            String name = item.getName();
            maps.put(name, item);
        }
        return maps;
    }

    public String getParentLevelName() {
        return mParentLevelName;
    }

    public String getCaption() {
        return mCaption;
    }

    public Class<?> getTargetClass() {
        return mTargetClass;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public ArrayList<TestItem> getTestItems() {
        reload();
        return mTestItems;
    }

    public synchronized void initTestItems() {
        for (TestItem item : mTestItems) {
            item.setResult(TestResult.NONE);
            item.updateContent();
        }
    }

    public TestItem getTestItemByName(String name) {
        return mMapTestItems.get(name);
    }

    public synchronized void updateContent() {
        IDiagnoseModel model = mApp.getDiagnoseModel();
        for (TestItem item : mTestItems)
            item.updateContent();

        verify();
        model.updateTestCase(this);
    }

    /**
     * Load the Test-Case and the its Test-Item from the Model.
     */
    @Override
    public synchronized void reload() {
        IDiagnoseModel model = mApp.getDiagnoseModel();
        TestCase testCase = model.loadTestCase(mParentLevelName, mName);
        if (testCase != null) {
            mId = testCase.getId();
            mTargetClass = testCase.getTargetClass();
            mResult = TestResult.create(testCase.getResult());
            mTime = testCase.mTime;
        }
        mMapTestItems = mapTestItems(model);
        mTestItems = mMapTestItems.getAllTestItems();
    }

    public synchronized TestResult verify() {
        mResult = TestResult.RESULT_PASS;
        for (TestItem i : mTestItems) {
            if(i.getResult() == TestResult.NONE) {
                mResult = TestResult.RESULT_NONE;
            }
            else if(i.getResult() == TestResult.FAIL) {
                mResult = TestResult.RESULT_FAIL;
                break;
            }
        }
        return mResult;
    }

    public static final class Columns implements BaseColumns {
        public static final String TEST_CASE_NAME = "test_case_name";
        public static final String PARENT_TEST_LEVEL_NAME = "parent_test_level_name";
        public static final String TEST_CASE_CAPTION = "test_case_caption";
        public static final String TEST_CASE_RESULT = "test_case_result";
        public static final String TEST_CASE_TIME = "test_case_time";

        public static final int INDEX_TEST_CASE_ID = 0;
        public static final int INDEX_TEST_CASE_NAME = 1;
        public static final int INDEX_PARENT_TEST_LEVEL_NAME = 2;
        public static final int INDEX_TEST_CASE_CAPTION = 3;
        public static final int INDEX_TEST_CASE_RESULT = 4;
        public static final int INDEX_TEST_CASE_TIME = 5;
    }
}
