package com.msi.diagnostic.utils;

public interface ICountDownListener {
    public void onCountDownFinish();
    public void onCountDownTick(long millisUntilFinished);
}
