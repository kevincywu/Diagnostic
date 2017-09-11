package com.msi.diagnostic.data;

public interface IVerifierVisitor {
    public boolean verify(ThresholdTestItem item, ThresholdVerifiedInfo info);
    public boolean verify(DefinitionTestItem item, DefinitionVerifiedInfo info);
    public boolean verify(DetectionTestItem item, DetectionVerifiedInfo info);
}
