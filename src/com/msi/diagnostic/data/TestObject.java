package com.msi.diagnostic.data;

public abstract class TestObject {
    protected static final long NONE_ID = -1;

    protected long mId = NONE_ID;
    protected final String mName;
    protected TestResult mResult = TestResult.create(TestResult.NONE);

    public TestObject(String name) {
        mName = name;
    }

    public TestObject(long id, String name) {
        this(name);
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setId(long id) {
        mId = id;
    }

    public final int getResult() {
        return mResult.getResult();
    }

    public void setResult(int result) {
        mResult = TestResult.create(result);
    }

    public final String getResultAsString() {
        return mResult.getResultAsString();
    }

    public abstract void reload();
}
