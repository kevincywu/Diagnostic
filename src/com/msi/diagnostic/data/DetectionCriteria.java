package com.msi.diagnostic.data;

public final class DetectionCriteria {

    private final boolean mMustBeDetected;

    public DetectionCriteria(boolean mustBeDetected) {
        mMustBeDetected = mustBeDetected;
    }

    public boolean getCriteria() {
        return mMustBeDetected;
    }
}
