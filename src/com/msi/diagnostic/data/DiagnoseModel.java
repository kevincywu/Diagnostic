package com.msi.diagnostic.data;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.msi.diagnostic.app.IDiagnosticApp;
import com.msi.diagnostic.utils.Utils;

import android.content.Context;
import android.database.Cursor;

public final class DiagnoseModel implements IDiagnoseModel {
    private static final String TAG = "DiagnoseModel";

    private final IDiagnosticApp mApp;
    private final IDatabaseSource mDatabaseSource;
    private WeakReference<IDataChangedListener> mListenerWeakRef;

    public DiagnoseModel(Context context, IDatabaseSource source) {
        mApp = (IDiagnosticApp) context.getApplicationContext();
        mDatabaseSource = source;
    }

    private TestLevel loadTestLevelFromCursor(Cursor cursor) {
        final long _id = cursor.getLong(TestLevel.Columns.INDEX_TEST_LEVEL_ID);
        final String levelName = cursor.getString(TestLevel.Columns.INDEX_TEST_LEVEL_NAME);
        final String levelCaption = cursor.getString(
                TestLevel.Columns.INDEX_TEST_LEVEL_CAPTION);

        return new TestLevel(mApp, _id, levelName, levelCaption);
    }

    private TestCase loadTestCaseFromCursor(Cursor cursor) {
        final long _id = cursor.getLong(TestCase.Columns.INDEX_TEST_CASE_ID);
        final String _caseName = cursor.getString(TestCase.Columns.INDEX_TEST_CASE_NAME);
        final String _parentLevelName = cursor.getString(TestCase.Columns.INDEX_PARENT_TEST_LEVEL_NAME);
        final String _caption = cursor.getString(TestCase.Columns.INDEX_TEST_CASE_CAPTION);
        final int _result = cursor.getInt(TestCase.Columns.INDEX_TEST_CASE_RESULT);
        final long _time = cursor.getLong(TestCase.Columns.INDEX_TEST_CASE_TIME);

        return new TestCase(
                mApp,
                _id,
                _caseName,
                _parentLevelName,
                _caption,
                TestResult.create(_result),
                _time);
    }

    private TestItem loadTestItemFromCursor(Cursor cursor) {
        final long _id = cursor.getLong(TestItem.Columns.INDEX_TEST_ITEM_ID);
        final String _itemName = cursor.getString(TestItem.Columns.INDEX_TEST_ITEM_NAME);
        final String _itemInfoFile = cursor.getString(TestItem.Columns.INDEX_TEST_ITEM_INFO_FILE);
        final String _parentLevelName = cursor.getString(TestItem.Columns.INDEX_PARENT_TEST_LEVEL_NAME);
        final String _parentCaseName = cursor.getString(TestItem.Columns.INDEX_PARENT_TEST_CASE_NAME);
        final int _itemResult = cursor.getInt(TestItem.Columns.INDEX_TEST_ITEM_RESULT);
        final int _criteriaType = cursor.getInt(TestItem.Columns.INDEX_TEST_CRITERIA_TYPE);

        switch (_criteriaType) {
        case TestItem.Types.TYPE_DEFINITION: {
            final String criteriaDefine = cursor.getString(TestItem.Columns.INDEX_TEST_CRITERIA_DEFINE);
            return new DefinitionTestItem(
                    mApp,
                    _id,
                    _itemName,
                    _itemInfoFile,
                    _parentLevelName,
                    _parentCaseName,
                    criteriaDefine,
                    TestResult.create(_itemResult));
            }

        case TestItem.Types.TYPE_THRESHOLD: {
            final float _criteriaMin = cursor.getFloat(TestItem.Columns.INDEX_TEST_CRITERIA_MIN);
            final float _criteriaMax = cursor.getFloat(TestItem.Columns.INDEX_TEST_CRITERIA_MAX);
            return new ThresholdTestItem(
                    mApp,
                    _id,
                    _itemName,
                    _itemInfoFile,
                    _parentLevelName,
                    _parentCaseName,
                    _criteriaMax, _criteriaMin,
                    TestResult.create(_itemResult));
            }

        case TestItem.Types.TYPE_DETECTION: {
            final int _criteriaDetect = cursor.getInt(TestItem.Columns.INDEX_TEST_CRITERIA_DETECT);
            return new DetectionTestItem(
                    mApp,
                    _id,
                    _itemName,
                    _itemInfoFile,
                    _parentLevelName,
                    _parentCaseName,
                    (_criteriaDetect == Utils.TRUE) ? true : false,
                    TestResult.create(_itemResult));
            }
        }
        return null;
    }

    private void notifyTestCaseChanged(TestCase testCase) {
        long testCaseId = testCase.getId();
        IDataChangedListener listener = mListenerWeakRef.get();
        listener.notifyDataChanged(testCaseId);
    }

    @Override
    public int updateTestCase(TestCase testCase) {
        int rowsAffects = mDatabaseSource.updateTestCase(testCase);
        notifyTestCaseChanged(testCase);
        return rowsAffects;
    }

    @Override
    public int updateTestItem(TestItem testItem) {
        int rowsAffects = mDatabaseSource.updateTestItem(testItem);
        return rowsAffects;
    }

    @Override
    public ArrayList<TestLevel> getTestLevels() {
        ArrayList<TestLevel> testLevels = new ArrayList<TestLevel>();

        Cursor cursor = mDatabaseSource.queryTestLevels();
        while (cursor.moveToNext()) {
            TestLevel level = loadTestLevelFromCursor(cursor);
            testLevels.add(level);
        }
        return testLevels;
    }

    @Override
    public synchronized ArrayList<TestCase> getTestCases(String testLevelName) {
        ArrayList<TestCase> testCases = new ArrayList<TestCase>();

        Cursor cursor = mDatabaseSource.queryTestCases(testLevelName);
        while (cursor.moveToNext()) {
            TestCase _case = loadTestCaseFromCursor(cursor);
            testCases.add(_case);
        }
        return testCases;
    }

    @Override
    public synchronized ArrayList<TestItem> getTestItems(
            String testLevelName, String testCaseName) {
        ArrayList<TestItem> items = new ArrayList<TestItem>();

        Cursor cursor = mDatabaseSource.queryTestItems(testLevelName, testCaseName);
        while (cursor.moveToNext()) {
            TestItem item = loadTestItemFromCursor(cursor);
            items.add(item);
        }

        return items;
    }

    @Override
    public TestLevel loadTestLevel(String testLevelName) {
        Cursor cursor = mDatabaseSource.queryTestLevel(testLevelName);
        TestLevel testLevel = null;
        if (cursor.moveToNext()) {
            testLevel = loadTestLevelFromCursor(cursor);
        }
        return testLevel;
    }

    /**
     * Load the specific Test-Case from the database.
     *
     *@param name The name of the Test-Case.
     */
    @Override
    public TestCase loadTestCase(String testLevelName, String caseName) {
        Cursor cursor = mDatabaseSource.queryTestCase(testLevelName, caseName);
        TestCase testCase = null;
        if (cursor.moveToNext()) {
            testCase = loadTestCaseFromCursor(cursor);
        }
        return testCase;
    }

    /**
     * Load the Test-Item from the database.
     *
     *@param name The name of the Test-Item.
     */
    @Override
    public TestItem loadTestItem(String levelName, String itemName) {
        Cursor cursor = mDatabaseSource.queryTestItem(levelName, itemName);
        TestItem testItem = null;
        if (cursor.moveToNext()) {
            testItem = loadTestItemFromCursor(cursor);
        }
        return testItem;
    }

    @Override
    public long addTestLevel(TestLevel testLevel) {
        return mDatabaseSource.addTestLevel(testLevel);
    }

    @Override
    public long addTestCase(TestCase testCase) {
        return mDatabaseSource.addTestCase(testCase);
    }

    @Override
    public long addTestItem(TestItem testItem) {
        return mDatabaseSource.addTestItem(testItem);
    }

    @Override
    public long removeTestLevel(TestLevel testLevel) {
        return mDatabaseSource.deleteTestLevel(testLevel);
    }

    @Override
    public long removeTestCase(TestCase testCase) {
        return mDatabaseSource.deleteTestCase(testCase);
    }

    @Override
    public long removeTestItem(TestItem testItem) {
        return mDatabaseSource.deleteTestItem(testItem);
    }

    /**
     * Set the listener for Model to notify the data changed event.
     *
     * @param listener The receiver of data changed event.
     */
    @Override
    public void setDataChangedListener(IDataChangedListener listener) {
        mListenerWeakRef = new WeakReference<IDataChangedListener>(listener);
    }
}
