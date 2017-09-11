
package com.msi.diagnostic.app;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Environment;
import android.widget.Toast;

import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.TestLevel;
import com.msi.diagnostic.ui.IMainPanelView;

public class DiagnosticPagePresenter implements IPresenter {

    private IDiagnosticApp mApp;
    private IMainPanelView mMainPanelView;
    private SystemInfoLoader mSysInfoLoader;
    private IDiagnoseModel mModel;
    private String mAppVersion;

    public DiagnosticPagePresenter(IDiagnosticApp app, IMainPanelView view, String appVersion) {
        mApp = app;
        mMainPanelView = view;
        mAppVersion = appVersion;
        mSysInfoLoader = new SystemInfoLoader(mAppVersion);
        mModel = app.getDiagnoseModel();

        createTestLevelLogFolder();

    }

    private void createTestLevelLogFolder() {
        if (createLogFolder() != true) {
            Toast.makeText(mApp.getAppContext(), "Fail to create log folders!",
                    Toast.LENGTH_LONG).show();
            return;
        }
    }

    private boolean createLogFolder() {

        ArrayList<TestLevel> testLevels = mModel.getTestLevels();
        String FILE_PATH = Environment.getExternalStorageDirectory().getPath();

        for (TestLevel level : testLevels) {

            String targetPath = new String();
            String[] stringNames = level.getName().split("\\.");

            if (stringNames.length <= 4) {
                return false;
            }
            else {
                // Fetch the project name
                String projectName = new String();
                projectName = stringNames[2].toUpperCase();

                String[] folderNames = stringNames[4].split("PanelView");
                if (folderNames.length == 0) {
                    return false;
                }
                else {
                    // create the project folder for different test-level logs
                    targetPath = FILE_PATH + "/" + projectName;
                    File folderCreated = new File(targetPath);
                    folderCreated.mkdir();

                    folderNames[0] = folderNames[0].toUpperCase();
                    targetPath = FILE_PATH + "/" + projectName + "/" + folderNames[0];
                }
            }
            boolean isSuccess;
            File folderCreated = new File(targetPath);
            isSuccess = folderCreated.mkdir();
        }
        return true;
    }

    @Override
    public void OnButtonClick(int buttonId) {
    }

    @Override
    public void resume() {
        ArrayList<TestLevel> levelList = mModel.getTestLevels();
        mMainPanelView.setLevelListData(levelList);
        ArrayList<HashMap<String, String>> sysInfo = mSysInfoLoader.getInfo();
        mMainPanelView.putSystemInfoContent(sysInfo);

    }

    @Override
    public void pause() {
    }
}
