package com.msi.diagnostic.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.ComponentName;
import android.graphics.Color;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.ILevelPagePresenter;
import com.msi.diagnostic.data.TestCase;

public class AbstractLevelPanelView extends Activity implements ILevelPanelView {

    private TestCaseButtonAdapter mAdapter;
    private ListView mButtonListView;
    protected ItemClickListener mItemClickListener;
    protected ILevelPagePresenter mPresenter;

    private static final int MENU_ID_STOP = Menu.FIRST;
    private static final int MENU_ID_EXIT = Menu.FIRST + 1;
    private static final int MENU_ID_LOG = Menu.FIRST + 2;

    private ArrayList<View> mExtraScreens;

    public void initPanelView(ILevelPagePresenter presenter) {
        mPresenter = presenter;
        mButtonListView = (ListView) findViewById(R.id.levelPanelListView);
        mAdapter = new TestCaseButtonAdapter(this);
        mItemClickListener = new ItemClickListener(mPresenter);
        mButtonListView.setAdapter(mAdapter);
        mButtonListView.setClickable(true);
        mButtonListView.setOnItemClickListener(mItemClickListener);
        mExtraScreens = new ArrayList<View>();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ID_LOG, 0, R.string.log).setShowAsAction(
                MenuItem.SHOW_AS_ACTION_IF_ROOM
                        | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        menu.add(Menu.NONE, MENU_ID_STOP, 0, R.string.stop).setShowAsAction(
                MenuItem.SHOW_AS_ACTION_IF_ROOM
                        | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        menu.add(Menu.NONE, MENU_ID_EXIT, 0, R.string.exit).setShowAsAction(
                MenuItem.SHOW_AS_ACTION_IF_ROOM
                        | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_ID_EXIT: {
            mPresenter.onOptionsItemExitSelected();
            break;
        }
        case MENU_ID_STOP: {
            mPresenter.onOptionsItemStopSelected();
            break;
        }
        case MENU_ID_LOG: {
            mPresenter.onOptionsItemLogSelected();
            break;
        }
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mPresenter.onRestoreState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mPresenter.onSaveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setData(ArrayList<TestCase> testCases) {
        mAdapter.setData(testCases);
    }

    @Override
    public TestCase getData(int position) {
        return (TestCase) mAdapter.getItem(position);
    }

    @Override
    public int getCountOfTestCaseButtons() {
        return mAdapter.getCount();
    }

    @Override
    public String getLevelName() {
        ComponentName cName = getComponentName();
        String className = cName.getClassName();
        return className;
    }

    @Override
    public void navigate(Class<?> className, Bundle data) {
        Object testCaseView = null;
        try {
            testCaseView = className.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        cancelCurrentTestCase();

        this.setListStatus(false);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,
                (Fragment) testCaseView);
        fragmentTransaction.commit();
    }

    @Override
    public void navigate(Class<?> className) {
        navigate(className, new Bundle());
    }

    @Override
    public void finishPanel(int requestCode) {
        finishActivity(requestCode);
    }

    @Override
    public void finishPanel() {
        finish();
    }

    public void onTestCaseDone() {
        mPresenter.onFinishTestCase();
    }

    @Override
    public void exitConfirmDialog() {
        AlertDialog.Builder confirmWindow = new AlertDialog.Builder(this);
        confirmWindow.setTitle(R.string.prompt);
        confirmWindow.setMessage(R.string.outOrNot);
        DialogInterface.OnClickListener sureToExit = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.clearTestData();
                finishPanel();
            }
        };
        confirmWindow.setPositiveButton(R.string.ok, sureToExit);
        confirmWindow.setNegativeButton(R.string.runin_wifi_cancle, null);
        confirmWindow.show();

    }

    @Override
    public boolean cancelCurrentTestCase() {

        // if the previous test-case has at least one extra screen,
        // we recycle it
        cancelExtraScreenViews();

        Fragment currentWorkingFragment;
        FragmentManager fragmentManager = getFragmentManager();
        currentWorkingFragment = fragmentManager
                .findFragmentById(R.id.fragment_container);

        if (currentWorkingFragment == null)
            return false;

        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.remove(currentWorkingFragment);
        fragmentTransaction.commit();

        return true;
    }

    @Override
    public void setListStatus(boolean isEnable) {
        if (isEnable) {
            mButtonListView.setEnabled(true);
            mButtonListView.setBackgroundColor(Color.WHITE);
        } else {
            mButtonListView.setEnabled(false);
            mButtonListView.setBackgroundColor(Color.GRAY);
        }
    }

    private void cancelExtraScreenViews() {
        int mExtraViewSize = mExtraScreens.size();
        if (mExtraViewSize != 0) {
            for (int cnt = 0; cnt < mExtraViewSize; cnt++) {
                View refView = mExtraScreens.get(cnt);
                ViewGroup vg = (ViewGroup) (refView.getParent());
                vg.removeView(refView);
            }
            mExtraScreens.clear();
        }
    }

    @Override
    public void requestAddScreen(View viewObject) {
        mExtraScreens.add(viewObject);
        addContentView(viewObject, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
    }

    @Override
    public void requestRemoveScreen() {
        cancelExtraScreenViews();
    }

    public void requestCancelCurrentTestCase() {
        cancelCurrentTestCase();
    }

    @Override
    public boolean isExtraScreenExisted() {
        return !(mExtraScreens.isEmpty());
    }

    @Override
    public void setSelectItem(int position) {
        mAdapter.setSelectItem(position);
        mButtonListView.smoothScrollToPosition(position);
    }

    @Override
    public void updateList() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void openLog(String str) {
        requestRemoveScreen();
        LayoutInflater inflater = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.logshow,
                (ViewGroup)findViewById(R.id.showloglayout), false);
        requestAddScreen(view);
        TextView mShowView = (TextView)findViewById(R.id.showlog);
        mShowView.setTextColor(Color.WHITE);
        mShowView.setMovementMethod(ScrollingMovementMethod.getInstance());
        mShowView.setText(str);
    }
}
