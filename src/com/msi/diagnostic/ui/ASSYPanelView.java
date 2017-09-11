package com.msi.diagnostic.ui;

import android.os.Bundle;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.ASSYPagePresenter;
import com.msi.diagnostic.app.IDiagnosticApp;

public class ASSYPanelView extends AbstractLevelPanelView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_panel);

        /* The PanelView should be initialized first. */
        IDiagnosticApp app = ((IDiagnosticApp) getApplicationContext());
        initPanelView(new ASSYPagePresenter(app, this));
    }
}
