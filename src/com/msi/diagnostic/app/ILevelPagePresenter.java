package com.msi.diagnostic.app;

import android.os.Bundle;

public interface ILevelPagePresenter extends IPresenter {
    public void onSaveState(Bundle savedInstanceState);
    public void onRestoreState(Bundle outState);
    public void onFinishTestCase();
    public void onOptionsItemExitSelected();
    public void onOptionsItemStopSelected();
    public void onOptionsItemLogSelected();
    public void clearTestData();
}
