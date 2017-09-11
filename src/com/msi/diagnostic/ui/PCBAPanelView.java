package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.IDiagnosticApp;
import com.msi.diagnostic.app.PCBAPagePresenter;

import android.os.Bundle;

public class PCBAPanelView extends AbstractLevelPanelView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_panel);

        /* The PanelView should be initialized first. */
        IDiagnosticApp app = ((IDiagnosticApp) getApplicationContext());
        initPanelView(new PCBAPagePresenter(app, this));
    }
}
