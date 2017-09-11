package com.msi.diagnostic.app;

import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.ui.ILevelPanelView;

public class ASSYPagePresenter extends AbstractLevelPagePresenter {
    private static final String TAG = "ASSYPagePresenter";
    private static final boolean LOCAL_LOG = false;

    private static final String NAME = TestLevel.TYPES[TestLevel.TYPE_ASSY];

    public ASSYPagePresenter(IDiagnosticApp app, ILevelPanelView view) {
        super(app, view, NAME);
    }

}
