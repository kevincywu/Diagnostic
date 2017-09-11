package com.msi.diagnostic.data;

import java.util.ArrayList;

import com.msi.diagnostic.utils.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class SQLiteDatabaseSource implements IDatabaseSource {
    private static final String TAG = "Diagnostic::SQLiteDatabaseSource";
    private static final boolean LOCAL_LOG = false;

    private static final String TEST_LEVEL_TABLE_NAME = "DiagnoseTable_Level";
    private static final String TEST_CASE_TABLE_NAME = "DiagnoseTable_Case";
    private static final String TEST_ITEM_TABLE_NAME = "DiagnoseTable_Item";

    private Context mContext;
    private DatabaseHelper mDatabase;
    private TestItemLoader mTestItemLoader;

    public static final String KEY_INSERTED_OR_CONVERTED_DATABASE =
            "key_insert_convert_database";

    private static final String[] TEST_LEVEL_PROJECTION = {
        TestLevel.Columns._ID,
        TestLevel.Columns.TEST_LEVEL_NAME,
        TestLevel.Columns.TEST_LEVEL_CAPTION
    };

    private static final String[] TEST_CASE_PROJECTION = {
        TestCase.Columns._ID,
        TestCase.Columns.TEST_CASE_NAME,
        TestCase.Columns.PARENT_TEST_LEVEL_NAME,
        TestCase.Columns.TEST_CASE_CAPTION,
        TestCase.Columns.TEST_CASE_RESULT,
        TestCase.Columns.TEST_CASE_TIME
    };

    private static final String[] TEST_ITEM_PROJECTION = {
        TestItem.Columns._ID,
        TestItem.Columns.TEST_ITEM_NAME,
        TestItem.Columns.TEST_ITEM_INFO_FILE,
        TestItem.Columns.TEST_ITEM_RESULT,
        TestItem.Columns.PARENT_TEST_LEVEL_NAME,
        TestItem.Columns.PARENT_TEST_CASE_NAME,
        TestItem.Columns.TEST_CRITERIA_TYPE,
        TestItem.Columns.TEST_CRITERIA_MIN,
        TestItem.Columns.TEST_CRITERIA_MAX,
        TestItem.Columns.TEST_CRITERIA_DEFINE,
        TestItem.Columns.TEST_CRITERIA_DETECT
    };

    public SQLiteDatabaseSource(Context context) {
        mContext = context;
        mDatabase = new DatabaseHelper(context);
        mTestItemLoader = new TestItemLoader(mContext);
        reloadTests();
    }

    private void reloadTests() {
        deleteTestLevels();
        deleteTestCases();
        deleteTestItems();

        TestSet set = mTestItemLoader.loadDeviceTestLevels();
        ArrayList<TestLevel> testLevels = set.getTestLevels();
        for (TestLevel level : testLevels)
            addTestLevel(level);

        ArrayList<TestCase> testCases = set.getTestCases();
        for (TestCase testCase : testCases)
            addTestCase(testCase);

        ArrayList<TestItem> testItems = set.getTestItems();
        for (TestItem testItem : testItems)
            addTestItem(testItem);
    }

    /**
     * Add a Test Level into database.
     *
     * @param testLevel The instance of Test-Level will be inserted into database.
     * @return the row ID of the newly inserted row, or -1 if an error occurred.
     */
    @Override
    public synchronized long addTestLevel(TestLevel testLevel) {
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        final String _testLevelName = testLevel.getName();
        final String _caption = testLevel.getCaption();

        ContentValues values = new ContentValues();
        values.put(TestLevel.Columns.TEST_LEVEL_NAME, _testLevelName);
        values.put(TestLevel.Columns.TEST_LEVEL_CAPTION, _caption);

        return db.insert(TEST_LEVEL_TABLE_NAME, null, values);
    }

    /**
     * Add a Test Case into database.
     *
     * @param testCase The instance of Test-Case will be inserted into database.
     * @return the row ID of the newly inserted row, or -1 if an error occurred.
     */
    @Override
    public synchronized long addTestCase(TestCase testCase) {
        SQLiteDatabase db = mDatabase.getWritableDatabase();

        final String _testCaseName = testCase.getName();
        final String _parentLevelName = testCase.getParentLevelName();
        final String _caption = testCase.getCaption();
        final int _testResult = testCase.getResult();
        final long _time = testCase.getTime();

        ContentValues values = new ContentValues();
        values.put(TestCase.Columns.TEST_CASE_NAME, _testCaseName);
        values.put(TestCase.Columns.PARENT_TEST_LEVEL_NAME, _parentLevelName);
        values.put(TestCase.Columns.TEST_CASE_CAPTION, _caption);
        values.put(TestCase.Columns.TEST_CASE_RESULT, _testResult);
        values.put(TestCase.Columns.TEST_CASE_TIME, _time);

        return db.insert(TEST_CASE_TABLE_NAME, null, values);
    }

    /**
     * Add a Test Item into database.
     *
     * @param testItem The instance of Test-Item will be inserted into database.
     * @return the row ID of the newly inserted row, or -1 if an error occurred.
     */
    @Override
    public synchronized long addTestItem(TestItem item) {
        SQLiteDatabase db = mDatabase.getWritableDatabase();

        final String _testItemName = item.getName();
        final String _testInfoFileName = item.getInfoFileName();
        final String _parentLevelName = item.getParentLevelName();
        final String _parentCaseName = item.getParentCaseName();
        final int _testResult = item.getResult();
        final int _testCriteriaType = item.getType();

        ContentValues values = new ContentValues();
        values.put(TestItem.Columns.TEST_ITEM_NAME, _testItemName);
        values.put(TestItem.Columns.TEST_ITEM_INFO_FILE, _testInfoFileName);
        values.put(TestItem.Columns.PARENT_TEST_LEVEL_NAME, _parentLevelName);
        values.put(TestItem.Columns.PARENT_TEST_CASE_NAME, _parentCaseName);       
        values.put(TestItem.Columns.TEST_ITEM_RESULT, _testResult);
        values.put(TestItem.Columns.TEST_CRITERIA_TYPE, _testCriteriaType);

        switch (_testCriteriaType) {
        case TestItem.Types.TYPE_DEFINITION: {
            DefinitionTestItem t = (DefinitionTestItem) item;
            final String define = t.getDefinition();
            values.put(TestItem.Columns.TEST_CRITERIA_DEFINE, define);
            } break;

        case TestItem.Types.TYPE_THRESHOLD: {
            ThresholdTestItem t = (ThresholdTestItem) item;
            final float min = t.getMin();
            final float max = t.getMax();
            values.put(TestItem.Columns.TEST_CRITERIA_MIN, min);
            values.put(TestItem.Columns.TEST_CRITERIA_MAX, max);
            } break;

        case TestItem.Types.TYPE_DETECTION: {
            DetectionTestItem t = (DetectionTestItem) item;
            final boolean mustBeDetected = t.isMustBeDetected();
            final int detected =
                    (mustBeDetected ? Utils.TRUE : Utils.FALSE);
            values.put(TestItem.Columns.TEST_CRITERIA_DETECT, detected);
            } break;
        }

        return db.insert(TEST_ITEM_TABLE_NAME, null, values);
    }

    /**
     * Update the specific Test Case in the database.
     *
     * @param testCase The instance of Test-Case will be updated in the database.
     * @return the number of rows affected.
     */
    @Override
    public synchronized int updateTestCase(TestCase testCase) {
        SQLiteDatabase db = mDatabase.getWritableDatabase();

        final String _testCaseName = testCase.getName();
        final String _parentTestLevelName = testCase.getParentLevelName();
        final String _caption = testCase.getCaption();
        final int _testResult = testCase.getResult();
        final long _time = testCase.getTime();

        ContentValues values = new ContentValues();
        values.put(TestCase.Columns.TEST_CASE_NAME, _testCaseName);
        values.put(TestCase.Columns.PARENT_TEST_LEVEL_NAME,
                _parentTestLevelName);
        values.put(TestCase.Columns.TEST_CASE_CAPTION, _caption);
        values.put(TestCase.Columns.TEST_CASE_RESULT, _testResult);
        values.put(TestCase.Columns.TEST_CASE_TIME, _time);

        String whereClause = TestCase.Columns.TEST_CASE_NAME + "=?" + " AND "
                + TestCase.Columns.PARENT_TEST_LEVEL_NAME + "=?" + " AND "
                + TestCase.Columns.TEST_CASE_CAPTION + "=?";
        String[] whereArgs = new String[] {
                _testCaseName, _parentTestLevelName, _caption};

        return db.update(TEST_CASE_TABLE_NAME, values, whereClause, whereArgs);
    }

    /**
     * Update the specific Test Item in the database.
     *
     * @param testItem The instance of Test-Item will be updated in the database.
     * @return the number of rows affected.
     */
    @Override
    public synchronized int updateTestItem(TestItem testItem) {
        SQLiteDatabase db = mDatabase.getWritableDatabase();

        final String _testItemName = testItem.getName();
        final String _parentLevelName = testItem.getParentLevelName();
        final String _parentCaseName = testItem.getParentCaseName();
        final int _testResult = testItem.getResult();

        ContentValues values = new ContentValues();
        values.put(TestItem.Columns.TEST_ITEM_NAME, _testItemName);
        values.put(TestItem.Columns.PARENT_TEST_LEVEL_NAME, _parentLevelName);
        values.put(TestItem.Columns.PARENT_TEST_CASE_NAME, _parentCaseName);
        values.put(TestItem.Columns.TEST_ITEM_RESULT, _testResult);

        String whereClause =
                TestItem.Columns.PARENT_TEST_LEVEL_NAME + "=?"
                + " AND "
                + TestItem.Columns.PARENT_TEST_CASE_NAME + "=?" 
                + " AND "
                + TestItem.Columns.TEST_ITEM_NAME + "=?";
        String[] whereArgs = new String[] {
                _parentLevelName, _parentCaseName, _testItemName
        };

        return db.update(
                TEST_ITEM_TABLE_NAME,
                values,
                whereClause,
                whereArgs);
    }

    /**
     * Query all the Test-Level from the database.
     *
     * @return A Cursor object, which is positioned before the first entry.
     *          Note that Cursors are not synchronized, see the documentation
     *          for more details.
     */
    @Override
    public Cursor queryTestLevels() {
        SQLiteDatabase db = mDatabase.getReadableDatabase();

        return db.query(
                TEST_LEVEL_TABLE_NAME,
                TEST_LEVEL_PROJECTION,
                null,
                null,
                null,
                null,
                null);
    }

    /**
     * Query all the Test-Case from the database.
     *
     * @param testLevelName The test cases in the particular Test-Level
     *          will be queried.
     * @return A Cursor object, which is positioned before the first entry.
     *          Note that Cursors are not synchronized, see the documentation
     *          for more details.
     */
    @Override
    public Cursor queryTestCases(String testLevelName) {
        SQLiteDatabase db = mDatabase.getReadableDatabase();

        final String selection =
                TestCase.Columns.PARENT_TEST_LEVEL_NAME + "=?";
        final String[] selectionArgs = new String[] { testLevelName };
        final String orderBy = TestCase.Columns._ID + " ASC";
        return db.query(
                TEST_CASE_TABLE_NAME,
                TEST_CASE_PROJECTION,
                selection,
                selectionArgs,
                null,
                null,
                orderBy);
    }

    /**
     * Query all the Test-Item from the database.
     *
     * @param testLevelName The test cases in the particular Test-Level
     *          will be queried.
     * @param testCaseName Specify the test case.
     * @return A Cursor object, which is positioned before the first entry.
     *          Note that Cursors are not synchronized, see the documentation
     *          for more details.
     */
    @Override
    public Cursor queryTestItems(String testLevelName, String testCaseName) {
        SQLiteDatabase db = mDatabase.getReadableDatabase();

        final String selection =
                TestItem.Columns.PARENT_TEST_LEVEL_NAME + "=?"
                + " AND "
                + TestItem.Columns.PARENT_TEST_CASE_NAME + "=?";
        final String[] selectionArgs = new String[] { testLevelName, testCaseName };
        return db.query(
                TEST_ITEM_TABLE_NAME,
                TEST_ITEM_PROJECTION,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }

    /**
     * Query the specific Test-Level from the database.
     *
     * @param testLevelName Name of the specific Test-Level.
     * @return A Cursor object, which is positioned before the first entry.
     *          Note that Cursors are not synchronized, see the documentation
     *          for more details.
     */
    @Override
    public Cursor queryTestLevel(String testLevelName) {
        SQLiteDatabase db = mDatabase.getReadableDatabase();

        final String selection =
                TestLevel.Columns.TEST_LEVEL_NAME + "=?";
        final String[] selectionArgs = new String[] { testLevelName };
        return db.query(
                TEST_LEVEL_TABLE_NAME,
                TEST_LEVEL_PROJECTION,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }

    /**
     * Query the specific Test-Case from the database.
     *
     * @param testLevelName Name of the specific Test-Level.
     * @param testCaseName Name of the specific Test-Case.
     * @return A Cursor object, which is positioned before the first entry.
     *          Note that Cursors are not synchronized, see the documentation
     *          for more details.
     */
    @Override
    public Cursor queryTestCase(String testLevelName, String testCaseName) {
        SQLiteDatabase db = mDatabase.getReadableDatabase();

        final String selection =
                TestCase.Columns.PARENT_TEST_LEVEL_NAME + "=?"
                + " AND "
                + TestCase.Columns.TEST_CASE_NAME + "=?";
        final String[] selectionArgs = new String[] { testLevelName, testCaseName };
        return db.query(
                TEST_CASE_TABLE_NAME,
                TEST_CASE_PROJECTION,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }

    /**
     * Query the specific Test-Item from the database.
     *
     * @param testLevelName Name of the specific Test-Level.
     * @param testItemName Name of the specific Test-Item.
     * @return A Cursor object, which is positioned before the first entry.
     *          Note that Cursors are not synchronized, see the documentation
     *          for more details.
     */
    @Override
    public Cursor queryTestItem(String testLevelName, String testItemName) {
        SQLiteDatabase db = mDatabase.getReadableDatabase();

        final String selection =
                TestItem.Columns.PARENT_TEST_LEVEL_NAME + "=?"
                + " AND "
                + TestItem.Columns.TEST_ITEM_NAME + "=?";
        final String[] selectionArgs = new String[] { testLevelName, testItemName };
        return db.query(
                TEST_ITEM_TABLE_NAME,
                TEST_ITEM_PROJECTION,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }

    /**
     * Delete the specific Test-Level in the database.
     *
     * @param testLevel The specific Test-Level.
     * @return the number of rows affected if a whereClause is passed in,
     *          0 otherwise. To remove all rows and get a count pass "1"
     *          as the whereClause.
     */
    @Override
    public int deleteTestLevel(TestLevel testLevel) {
        SQLiteDatabase db = mDatabase.getWritableDatabase();

        final String whereclause =
                TestLevel.Columns.TEST_LEVEL_NAME + "=?";
        final String testLevelName = testLevel.getName();
        final String[] whereArgs = new String[] { testLevelName };

        return db.delete(
                TEST_LEVEL_TABLE_NAME,
                whereclause,
                whereArgs);
    }

    /**
     * Delete the specific Test-Case in the database.
     *
     * @param testCase The specific Test-Case.
     * @return the number of rows affected if a whereClause is passed in,
     *          0 otherwise. To remove all rows and get a count pass "1"
     *          as the whereClause.
     */
    @Override
    public int deleteTestCase(TestCase testCase) {
        SQLiteDatabase db = mDatabase.getWritableDatabase();

        final String whereclause =
                TestCase.Columns.PARENT_TEST_LEVEL_NAME + "=?" +
                " AND " +
                TestCase.Columns.TEST_CASE_NAME + "=?";
        final String parentTestLevelName = testCase.getParentLevelName();
        final String testCaseName = testCase.getName();
        final String[] whereArgs = new String[] {
                parentTestLevelName,
                testCaseName };

        return db.delete(
                TEST_CASE_TABLE_NAME,
                whereclause,
                whereArgs);
    }

    /**
     * Delete the specific Test-Item in the database.
     *
     * @param testLevel The specific Test-Item.
     * @return the number of rows affected if a whereClause is passed in,
     *          0 otherwise. To remove all rows and get a count pass "1"
     *          as the whereClause.
     */
    @Override
    public int deleteTestItem(TestItem testItem) {
        SQLiteDatabase db = mDatabase.getWritableDatabase();

        final String whereclause =
                TestItem.Columns.PARENT_TEST_LEVEL_NAME + "=?" +
                " AND " +
                TestItem.Columns.PARENT_TEST_CASE_NAME + "=?" +
                " AND " +
                TestItem.Columns.TEST_ITEM_NAME + "=?";
        final String parentTestLevelName = testItem.getParentLevelName();
        final String parentTestCaseName = testItem.getParentCaseName();
        final String testItemName = testItem.getName();
        final String[] whereArgs = new String[] {
                parentTestLevelName,
                parentTestCaseName,
                testItemName };

        return db.delete(
                TEST_ITEM_TABLE_NAME,
                whereclause,
                whereArgs);
    }

    /**
     * Delete all the Test-Levels in the database.
     *
     * @return the number of rows affected if a whereClause is passed in,
     *          0 otherwise. To remove all rows and get a count pass "1"
     *          as the whereClause.
     */
    @Override
    public int deleteTestLevels() {
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        return db.delete(TEST_LEVEL_TABLE_NAME, null, null);
    }

    /**
     * Delete all the Test-Cases in the database.
     *
     * @return the number of rows affected if a whereClause is passed in,
     *          0 otherwise. To remove all rows and get a count pass "1"
     *          as the whereClause.
     */
    @Override
    public int deleteTestCases() {
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        return db.delete(TEST_CASE_TABLE_NAME, null, null);
    }

    /**
     * Delete all the Test-Items in the database.
     *
     * @return the number of rows affected if a whereClause is passed in,
     *          0 otherwise. To remove all rows and get a count pass "1"
     *          as the whereClause.
     */
    @Override
    public int deleteTestItems() {
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        return db.delete(TEST_ITEM_TABLE_NAME, null, null);
    }

    private final class DatabaseHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 4;
        private static final String DATABASE_NAME = "production_test.db";

        public DatabaseHelper(Context context, String name, CursorFactory factory,
                int version) {
            super(context, name, factory, version);
        }

        public DatabaseHelper(Context context) {
            this(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            if (LOCAL_LOG)
                Log.d(TAG, "create a new Diagnostic Database");

            /* Create the Test Level Table */
            String levelTable = "create table if not exists "
                    + TEST_LEVEL_TABLE_NAME + "("
                    + TestLevel.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TestLevel.Columns.TEST_LEVEL_NAME + " TEXT,"
                    + TestLevel.Columns.TEST_LEVEL_CAPTION + " TEXT)";
            db.execSQL(levelTable);

            /* Create the Test Case Table */
            String caseTable = "create table if not exists "
                    + TEST_CASE_TABLE_NAME + "("
                    + TestCase.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TestCase.Columns.TEST_CASE_NAME + " TEXT,"
                    + TestCase.Columns.PARENT_TEST_LEVEL_NAME + " TEXT,"
                    + TestCase.Columns.TEST_CASE_CAPTION + " TEXT,"
                    + TestCase.Columns.TEST_CASE_RESULT + " INTEGER,"
                    + TestCase.Columns.TEST_CASE_TIME + " INTEGER)";
            db.execSQL(caseTable);

            /* Create the Test Item Table */
            String itemTable = "create table if not exists "
                    + TEST_ITEM_TABLE_NAME + "("
                    + TestItem.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TestItem.Columns.TEST_ITEM_NAME + " TEXT,"
                    + TestItem.Columns.TEST_ITEM_INFO_FILE + " TEXT,"
                    + TestItem.Columns.TEST_ITEM_RESULT + " INTEGER,"
                    + TestItem.Columns.PARENT_TEST_LEVEL_NAME + " TEXT,"
                    + TestItem.Columns.PARENT_TEST_CASE_NAME + " TEXT,"
                    + TestItem.Columns.TEST_CRITERIA_TYPE + " INTEGER,"
                    + TestItem.Columns.TEST_CRITERIA_MIN + " REAL,"
                    + TestItem.Columns.TEST_CRITERIA_MAX + " REAL,"
                    + TestItem.Columns.TEST_CRITERIA_DEFINE + " TEXT,"
                    + TestItem.Columns.TEST_CRITERIA_DETECT + " INTEGER)";
            db.execSQL(itemTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all data");

            db.execSQL("DROP TABLE IF EXISTS " + TEST_LEVEL_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TEST_CASE_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TEST_ITEM_TABLE_NAME);
            onCreate(db);
        }
    }
}
