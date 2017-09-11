package com.msi.diagnostic.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.DiagnosticPagePresenter;
import com.msi.diagnostic.app.IDiagnosticApp;
import com.msi.diagnostic.data.TestLevel;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Diagnostic extends Activity implements IMainPanelView {

    public static final int REQUEST_NEXT_TEST = 0;

    private ListView mSystemInfoListView;

    private DiagnosticPagePresenter mPresenter;
    private SimpleAdapter mAdapter;
    private ListView mLevelListView;
    private ArrayList<String> mLevelListItems;
    private ArrayAdapter<String> mLevelListAdapter;
    private ArrayList<Class> mClassList;
    private String mAppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_panel);
        mClassList = new ArrayList<Class>();

        mLevelListView = (ListView) findViewById(R.id.levelListView);
        mLevelListItems = new ArrayList<String>();
        mLevelListAdapter = new ArrayAdapter<String>(this,
                R.layout.testlevel_button_item, mLevelListItems);
        mLevelListView.setAdapter(mLevelListAdapter);
        mLevelListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                            View view, int position, long id) {
                        navigate(mClassList.get(position));
                    }
                });

        mAppVersion = getAppVersion();
        mPresenter = new DiagnosticPagePresenter(
                (IDiagnosticApp) getApplication(), this, mAppVersion);

        mSystemInfoListView = (ListView) findViewById(R.id.systemInfoListView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override
    public void putSystemInfoContent(ArrayList<HashMap<String, String>> info) {
        mAdapter = new SimpleAdapter(this, info,
                                     android.R.layout.simple_list_item_2,
                                     new String[] {"title", "content"},
                                     new int[] {android.R.id.text1, android.R.id.text2}
        );
        mSystemInfoListView.setAdapter(mAdapter);
    }

    @Override
    public String getLevelName() {
        ComponentName cName = getCallingActivity();
        String className = cName.getClassName();
        return className;
    }

    @Override
    public void navigate(Class<?> className, Bundle data) {
        Intent intent = new Intent();
        intent.setClass(this, className);
        intent.putExtras(data);
        /**
         * WARNING: You must use the startActivityForResult to navigate to other
         * Activity, because the getCallingActivity method needs it.
         */
        startActivityForResult(intent, REQUEST_NEXT_TEST);
    }

    @Override
    public void navigate(Class<?> className) {
        navigate(className, new Bundle());
    }

    @Override
    public void setLevelListData(ArrayList<TestLevel> levelList) {
        mLevelListItems.clear();
        int levelListSize = levelList.size();
        for (int index = 0; index < levelListSize; index++) {
            TestLevel testLevel;
            String levelName;
            String levelCaption;
            Class<?> levelClass;

            testLevel = levelList.get(index);
            levelName = testLevel.getName();
            levelCaption = testLevel.getCaption();
            try {
                levelClass = Class.forName(levelName);
                mClassList.add(levelClass);
                mLevelListItems.add(levelCaption);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        mLevelListAdapter.notifyDataSetChanged();
    }

    private String getAppVersion() {
        String appVersion = null;
        PackageInfo packageInfo;
        try {
            packageInfo = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            appVersion = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }
}
