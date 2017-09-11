package com.msi.diagnostic.data;

public class VerifierVisitorImp implements IVerifierVisitor {

    private static VerifierVisitorImp mVerifierVisitorImp = null;

    private VerifierVisitorImp() {
    }

    public static VerifierVisitorImp getInstance() {
        if (mVerifierVisitorImp == null) {
            synchronized (VerifierVisitorImp.class) {
                if (null == mVerifierVisitorImp) {
                    mVerifierVisitorImp = new VerifierVisitorImp();
                }
            }
        }
        return mVerifierVisitorImp;
    }

    @Override
    public boolean verify(ThresholdTestItem item, ThresholdVerifiedInfo info) {
        float min = item.getMin();
        float max = item.getMax();
        float value = info.mInfo;
        if (value >= min && value <= max) {
            return true;
        }
        return false;
    }

    @Override
    public boolean verify(DefinitionTestItem item, DefinitionVerifiedInfo info) {
        String definition = item.getDefinition();
        final String valueDefinition = info.mDefinition;

        if (definition.equals(DefinitionCriteria.NOT_EMPTY)) {
            if (!valueDefinition.isEmpty())
                return true;
        }
        return (definition.equals(valueDefinition));
    }

    @Override
    public boolean verify(DetectionTestItem item, DetectionVerifiedInfo info) {
        boolean isMustBeDetected = item.isMustBeDetected();
        boolean isDetected = info.mInfo;
        return (isMustBeDetected == isDetected);
    }
}
