package com.msi.diagnostic.data;


import com.msi.diagnostic.app.IDiagnosticApp;

import android.os.AsyncTask;
import android.provider.BaseColumns;

public abstract class TestItem extends TestObject {

    protected IDiagnosticApp mApp;

    protected final String mInfoFileName;
    protected final String mParentLevelName;
    protected final String mParentCaseName;
    protected final int mType;

    protected IVerifierVisitor mVisitor;

    public TestItem(
            IDiagnosticApp app,
            long id,
            String itemName,
            String itemFile,
            String parentLevelName,
            String parentCaseName,
            int type) {

        super(itemName);

        mApp = app;
        mInfoFileName = itemFile;
        mParentLevelName = parentLevelName;
        mParentCaseName = parentCaseName;
        mType = type;
        mVisitor = app.getVerifierVisitor();
    }

    public final int getType() {
        return mType;
    }

    public final String getInfoFileName() {
        return mInfoFileName;
    }

    public final String getParentLevelName() {
        return mParentLevelName;
    }

    public final String getParentCaseName() {
        return mParentCaseName;
    }

    public void updateContent() {
        UpdateTask updateTask = new UpdateTask();
        updateTask.execute();
    }

    class UpdateTask extends AsyncTask<Void, Void, Integer> {

            @Override
            protected Integer doInBackground(Void... params) {

                IDiagnoseModel model = mApp.getDiagnoseModel();
                int result = model.updateTestItem(TestItem.this);

                return result;

            }


    }
    public abstract TestResult verify(Object info);

    public static final class Columns implements BaseColumns {
        public static final String TEST_ITEM_NAME          = "test_item_name";
        public static final String TEST_ITEM_INFO_FILE     = "test_item_info_file";
        public static final String PARENT_TEST_LEVEL_NAME  = "parent_test_level_name";
        public static final String PARENT_TEST_CASE_NAME   = "parent_test_case_name";
        public static final String TEST_ITEM_RESULT        = "test_item_result";
        public static final String TEST_CRITERIA_TYPE      = "test_criteria_type";
        public static final String TEST_CRITERIA_MIN       = "test_criteria_min";
        public static final String TEST_CRITERIA_MAX       = "test_criteria_max";
        public static final String TEST_CRITERIA_DEFINE    = "test_criteria_define";
        public static final String TEST_CRITERIA_DETECT    = "test_criteria_detect";

        public static final int INDEX_TEST_ITEM_ID             = 0;
        public static final int INDEX_TEST_ITEM_NAME           = 1;
        public static final int INDEX_TEST_ITEM_INFO_FILE      = 2;
        public static final int INDEX_TEST_ITEM_RESULT         = 3;
        public static final int INDEX_PARENT_TEST_LEVEL_NAME   = 4;
        public static final int INDEX_PARENT_TEST_CASE_NAME    = 5;
        public static final int INDEX_TEST_CRITERIA_TYPE       = 6;
        public static final int INDEX_TEST_CRITERIA_MIN        = 7;
        public static final int INDEX_TEST_CRITERIA_MAX        = 8;
        public static final int INDEX_TEST_CRITERIA_DEFINE     = 9;
        public static final int INDEX_TEST_CRITERIA_DETECT     = 10;
    }

    public static final class Types {
        public static final int TYPE_DEFINITION = 0;
        public static final int TYPE_THRESHOLD  = 1;
        public static final int TYPE_DETECTION  = 2;
    }
}
