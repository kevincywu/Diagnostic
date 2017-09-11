package com.msi.diagnostic.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.Pcba3gModulePagePresenter;

/**
 * TODO: onKeyEvent
 */
public class Pcba3gModuleTestCaseView extends AbstractTestCaseView {

    private Activity mLevelPanel;
    private View mView;

    private Button mTestButton;

    private Pcba3gModulePagePresenter mPresenter;
    private ButtonClickListener mButtonClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mLevelPanel = getActivity();
        mView = inflater.inflate(R.layout.pcba_3g_module_testcase, container, false);

        mPresenter = new Pcba3gModulePagePresenter(this);
        mButtonClickListener = new ButtonClickListener(mPresenter);

        mTestButton = (Button) mView.findViewById(R.id.startTest);
        mTestButton.setOnClickListener(mButtonClickListener);

        return mView;
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

    public void showResultToast(int resId) {
        Toast.makeText(mLevelPanel,
                getString(resId),
                Toast.LENGTH_SHORT)
                .show();
    }
}
