package com.msi.diagnostic.app;

import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.ui.ILevelPanelView;

public class RuninPagePresenter extends AbstractLevelPagePresenter {
    private static final String TAG = "RuninPagePresenter";
    private static final boolean LOCAL_LOG = false;

    private static final String NAME = TestLevel.TYPES[TestLevel.TYPE_RUNIN];

    public RuninPagePresenter(IDiagnosticApp app, ILevelPanelView view) {
        super(app, view, NAME);
    }
}
