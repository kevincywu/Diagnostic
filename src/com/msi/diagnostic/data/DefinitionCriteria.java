package com.msi.diagnostic.data;

public class DefinitionCriteria {

    public static final String NOT_EMPTY = "@null";

    protected final String mDefine;

    public DefinitionCriteria(String definition) {
        mDefine = definition;
    }

    public String getDefinition() {
        return mDefine;
    }
}
