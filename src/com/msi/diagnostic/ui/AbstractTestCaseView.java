package com.msi.diagnostic.ui;

import com.msi.diagnostic.app.IDiagnosticApp;

import android.app.Fragment;
import android.content.ComponentName;
import android.view.View;

public abstract class AbstractTestCaseView extends Fragment implements IDiagnosticActivity {

    public static final int REQUEST_NEXT_TEST = 0;

    private AbstractLevelPanelView mLevelPanel;

    public String getLevelName() {
        ComponentName cName = getActivity().getComponentName();
        String className = cName.getClassName();
        return className;
    }

    @Override
    public IDiagnosticApp getDiagnosticApp() {
        IDiagnosticApp app = (IDiagnosticApp) getActivity().getApplication();
        return app;
    }

    public void finishTestCase() {
        mLevelPanel = (AbstractLevelPanelView) getActivity();
        mLevelPanel.requestCancelCurrentTestCase();
    }

    public void exitFullScreen() {
        mLevelPanel = (AbstractLevelPanelView) getActivity();
        mLevelPanel.requestRemoveScreen();
    }

    public void requestFullScreen(View view) {
        mLevelPanel = (AbstractLevelPanelView) getActivity();
        mLevelPanel.requestAddScreen(view);
    }
}
