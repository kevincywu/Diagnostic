package com.msi.diagnostic.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.msi.diagnostic.data.TestLevel;

public interface IMainPanelView extends IPanelView {
    public void putSystemInfoContent(ArrayList<HashMap<String,String>> info);
    public void setLevelListData(ArrayList<TestLevel> levelList);
}
