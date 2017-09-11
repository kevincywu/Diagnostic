package com.msi.diagnostic.ui;

import java.util.ArrayList;

import android.view.View;

import com.msi.diagnostic.data.TestCase;

public interface ILevelPanelView extends IPanelView {
    public int getCountOfTestCaseButtons();
    public void setData(ArrayList<TestCase> testCases);
    public TestCase getData(int position);
    public void finishPanel();
    public void finishPanel(int requestCode);
    public void setListStatus(boolean isEnable);
    public void exitConfirmDialog();
    public void openLog(String str);
    public boolean cancelCurrentTestCase();
    public boolean isExtraScreenExisted();
    public void setSelectItem(int position);
    public void updateList();
    public void requestAddScreen(View viewObject);
    public void requestRemoveScreen();
}
