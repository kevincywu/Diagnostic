package com.msi.diagnostic.utils;

import android.os.CountDownTimer;

public class CountDownWaitingTimer extends CountDownTimer {

    private ICountDownListener mCountDownListener;

    public CountDownWaitingTimer(long millisInFuture, long countDownInterval,
            ICountDownListener countDownListener) {
        super(millisInFuture, countDownInterval);
        mCountDownListener = countDownListener;
    }

    @Override
    public void onFinish() {
        mCountDownListener.onCountDownFinish();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mCountDownListener.onCountDownTick(millisUntilFinished);
    }

}
