/**
 * HotKeyTest.java
 *
 * Written by : Qiuchen Jiang
 * Written for: N0J1 MES
 * Date : Dec 14, 2011
 * Version : 1.0
 *
 * Modified by: Qiuchen Jiang
 * Written for: N0J1 MES
 * Date : Dec 21, 2011
 * Version : 1.1
 *
 * Modified by: Qiuchen Jiang
 * Written for: N0J1 MES
 * Date : Jan 19, 2012
 * Version : 1.2
 *
 * Modified by: Qiuchen Jiang
 * Written for: N0J1 MES
 * Date : Feb 20, 2012
 * Version : 1.3
 *
 * Modified by: Zhenjiang Yang
 * Written for: ICS MES
 * Date : MAY 2, 2012
 * Version : 1.4
 */

package com.msi.diagnostic.ui;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.HotKeyPagePresenter;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import android.view.View.OnKeyListener;

/**
 * This class directs testing the hot keys and volume button, press each key, if
 * the method gets the match key value, then this test will be supposed PASS. It
 * will also write log file.
 * 
 * Version 1.1: Add annotations. Version 1.2: Add home key test. Version 1.3:
 * Prevent execute onCreate again when connect miniUSB devices.
 * 
 * @Author Qiuchen Jiang, Jim.
 * @Version 1.3, 02/20/2012
 */

public class HotKeyTestCaseView extends AbstractTestCaseView {

    private View mView;

    private TextView mVolumnUpResult;
    private TextView mVolumnDownResult;
    private Button mSubmitButton;
    private ButtonClickListener mButtonClickListener;

    private HotKeyPagePresenter mPresenter;

    private HotKeyListener mOnKeyListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.hotkey_testcase, container, false);

        mOnKeyListener = new HotKeyListener();
        mView.setOnKeyListener(mOnKeyListener);
        mView.setFocusable(true);
        mView.setFocusableInTouchMode(true);
        mView.requestFocus();

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mVolumnUpResult = (TextView) mView.findViewById(R.id.volumn_up_result);
        mVolumnDownResult = (TextView) mView.findViewById(R.id.volumn_down_result);
        mSubmitButton = (Button) mView.findViewById(R.id.submit_button);

        mPresenter = new HotKeyPagePresenter(this);
        mButtonClickListener = new ButtonClickListener(mPresenter);
        mSubmitButton.setOnClickListener(mButtonClickListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    public void setVolumnUpResult(String testResult) {
        mVolumnUpResult.setText(testResult);
    }

    public void setVolumnDownResult(String testResult) {
        mVolumnDownResult.setText(testResult);
    }

    public void setVolumnUpResultTextColor(int color) {
        mVolumnUpResult.setTextColor(color);
    }

    public void setVolumnDownResultTextColor(int color) {
        mVolumnDownResult.setTextColor(color);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class HotKeyListener implements View.OnKeyListener {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            mPresenter.onKeyEventHandler(keyCode, event);
            return true;
        }
    }

}
