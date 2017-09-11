
package com.msi.diagnostic.ui;

import java.lang.ref.WeakReference;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.LayoutInflater;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.BarCodePagePresenter;

public class BarCodeTestCaseView extends AbstractTestCaseView {

    private View mView;
    private EditText mNumberEditText;
    private TextView mStatusTextView;
    private Button mSubmitButton;

    private BarCodePagePresenter mPresenter;
    private ButtonClickListener mButtonClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.barcode_testcase, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mNumberEditText = (EditText) mView.findViewById(R.id.numberEditText);

        mStatusTextView = (TextView) mView.findViewById(R.id.statusTextView);

        mSubmitButton = (Button) mView.findViewById(R.id.submit);

        mPresenter = new BarCodePagePresenter(this);
        mNumberEditText.setOnKeyListener(new KeyEventListener(mPresenter));
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

    public void setBarCodeNumberText(String barCodeNumber) {
        mNumberEditText.setText(barCodeNumber);
    }

    public String getBarCodeNumberText() {
        return mNumberEditText.getText().toString();
    }

    public void setStatusText(String text) {
        mStatusTextView.setText(text);
    }

    public void setStatusText(int resId) {
        mStatusTextView.setText(resId);
    }

    public String getStatusText() {
        CharSequence chars = mStatusTextView.getText();
        return chars.toString();
    }

    // *****************************************
    // prevent pressing back key during testing
    // *****************************************
    // Disable the following functions for external events temporarily
    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
     */

    private static final class KeyEventListener implements View.OnKeyListener {
        private WeakReference<BarCodePagePresenter> mPresenterWeakRef;

        public KeyEventListener(BarCodePagePresenter presenter) {
            mPresenterWeakRef = new WeakReference<BarCodePagePresenter>(presenter);
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            int action = event.getAction();
            if (action == KeyEvent.ACTION_UP) {
                BarCodePagePresenter presenter = mPresenterWeakRef.get();
                presenter.OnButtonClick(keyCode);
            }
            return false;
        }
    }

}
