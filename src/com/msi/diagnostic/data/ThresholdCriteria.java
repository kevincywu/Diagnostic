package com.msi.diagnostic.data;

public class ThresholdCriteria {

    protected final float mMax;
    protected final float mMin;

    public ThresholdCriteria(float max, float min) {
        mMin = min;
        mMax = max;
    }

    public float getMin() {
        return mMin;
    }

    public float getMax() {
        return mMax;
    }

}
