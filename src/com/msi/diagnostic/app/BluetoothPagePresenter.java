package com.msi.diagnostic.app;

import com.msi.diagnostic.ui.BluetoothTestCaseView;

public class BluetoothPagePresenter extends AbstractTestCasePresenter {

    private BluetoothTest mBluetoothTest;
    private BluetoothTestCaseView mView;

    public BluetoothPagePresenter(BluetoothTestCaseView view) {
        super(view);
        mView = view;
        mBluetoothTest = new BluetoothTest(view.getAppContext(), this);
    }

    @Override
    public void OnButtonClick(int buttonId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resume() {
        super.resume();
        mBluetoothTest.execute();
        mView.finishTestCase();
    }

    @Override
    public void pause() {
        super.pause(false);
    }
}
