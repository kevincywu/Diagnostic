package com.msi.diagnostic.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.view.View;

import com.msi.diagnostic.R;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.data.ThresholdVerifiedInfo;
import com.msi.diagnostic.ui.RuninEndTestCaseView;

public class RuninEndPagePresenter extends AbstractTestCasePresenter {

    private Context mContext;
    private RuninEndTestCaseView mView;

    private IntentFilter mFilter;
    private int mLevel;
    private int mScale;
    private int mStatus;
    private ThresholdVerifiedInfo mThresholdInfo;

    private static class TestItemId {
        public static final int IS_CAPACITY_PASS = 0;
    }

    private static final String[] TEST_ITEM_NAME = {
        "isCapacityPass"
};

    public RuninEndPagePresenter(RuninEndTestCaseView view) {
        super(view);
        mView = view;
        mContext = mView.getAppContext();

        mFilter = new IntentFilter();
        mFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mThresholdInfo = new ThresholdVerifiedInfo(0);
    }

    private int getStatusResString(int status) {

        switch (status) {
        case BatteryManager.BATTERY_STATUS_CHARGING:
            return R.string.battery_status_charging;

        case BatteryManager.BATTERY_STATUS_DISCHARGING:
            return R.string.battery_status_discharging;

        case BatteryManager.BATTERY_STATUS_FULL:
            return R.string.battery_status_full;

        case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
            return R.string.battery_status_not_charging;
        }
        return R.string.battery_status_unknown;
    }

    /**
     * A class for gain information of battery
     * 
     * @author jack
     */

    private BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            mLevel = level;
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
            mScale = scale;
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            mStatus = status;
            int a = (mLevel * 100) / mScale;
            String str = String.valueOf(a);
            mView.setLevelValueViewText(str);

            int statusStringResId = getStatusResString(mStatus);
            if (mLevel < 95) {
                String fail = "%   FAIL";
                mView.setLevelResultViewText(fail);
                mView.setLevelValueViewTextColor(Color.rgb(255, 20, 20));
                mView.setWarningPromptVisibility(View.VISIBLE);
                mView.setWarningPromptText(statusStringResId);
            } else {
                mView.setLevelValueViewTextColor(Color.rgb(20, 250, 250));
                mView.setWarningPromptVisibility(View.INVISIBLE);
                String pass = "%    PASS";
                mView.setLevelResultViewText(pass);
            }

            TestResult result = verify(mLevel);
            if(result.isPass()) {
                mView.finishTestCase();
            }
        }

    };

    private TestResult verify(float capacity) {
        mThresholdInfo.mInfo = capacity;
        String itemName = TEST_ITEM_NAME[TestItemId.IS_CAPACITY_PASS];
        TestItem item = getTestItemByName(itemName);
        TestResult result = item.verify(mThresholdInfo);

        return result;
    }

    @Override
    public void OnButtonClick(int buttonId) {
    }

    @Override
    public void resume() {
        super.resume();
        mContext.registerReceiver(mBatteryInfoReceiver, mFilter);
    }

    @Override
    public void pause() {
        super.pause();
        mContext.unregisterReceiver(mBatteryInfoReceiver);
    }

}
