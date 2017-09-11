package com.msi.diagnostic.data;

import java.util.ArrayList;

import com.msi.diagnostic.app.IDiagnosticApp;

import android.provider.BaseColumns;

public final class TestLevel extends TestObject {

    private IDiagnosticApp mApp;
    private int mType;
    private final String mCaption;

    public static final String TYPES[] = {
        "com.msi.diagnostic.ui.PCBAPanelView",
        "com.msi.diagnostic.ui.ASSYPanelView",
        "com.msi.diagnostic.ui.RuninPanelView",
        "UNKNOWN"
    };

    public static final int TYPE_PCBA       = 0;
    public static final int TYPE_ASSY       = 1;
    public static final int TYPE_RUNIN      = 2;
    public static final int TYPE_UNKNOWN    = 3;

    public TestLevel(IDiagnosticApp app, long id, String name, String caption) {
        super(id, name);
        mApp = app;
        mCaption = caption;

        if (TYPES[TYPE_PCBA].equals(name)) {
            mType = TYPE_PCBA;
        } else if (TYPES[TYPE_ASSY].equals(name)) {
            mType = TYPE_ASSY;
        } else if (TYPES[TYPE_RUNIN].equals(name)) {
            mType = TYPE_RUNIN;
        } else {
            mType = TYPE_UNKNOWN;
        }
    }

    public TestLevel(IDiagnosticApp app, String name, String caption) {
        this(app, NONE_ID, name, caption);
    }

    public int getType() {
        return mType;
    }

    public String getCaption() {
        return mCaption;
    }

    public ArrayList<TestCase> getTestCases() {
        IDiagnoseModel model = mApp.getDiagnoseModel();
        return model.getTestCases(mName);
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException();
    }

    public static final class Columns implements BaseColumns {
        public static final String TEST_LEVEL_NAME          = "test_level_name";
        public static final String TEST_LEVEL_CAPTION        = "test_level_caption";

        public static final int INDEX_TEST_LEVEL_ID             = 0;
        public static final int INDEX_TEST_LEVEL_NAME           = 1;
        public static final int INDEX_TEST_LEVEL_CAPTION        = 2;
    }
}
