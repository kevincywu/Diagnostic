package com.msi.diagnostic.data;

public final class TestResult {

    private int mResult;

    public static final int PASS = 0;
    public static final int FAIL = 1;
    public static final int NONE = 2;
    public static final int TESTING = 3;

    public static final TestResult RESULT_PASS = TestResult.create(TestResult.PASS);
    public static final TestResult RESULT_FAIL = TestResult.create(TestResult.FAIL);
    public static final TestResult RESULT_NONE = TestResult.create(TestResult.NONE);
    public static final TestResult RESULT_TESTING = TestResult.create(TestResult.TESTING);

    public TestResult(int result) {
        mResult = result;
    }

    public static TestResult create(int result) {
        return new TestResult(result);
    }

    public boolean isPass() {
        return (mResult == PASS) ? true : false;
    }

    public int getResult() {
        return mResult;
    }

    public String getResultAsString() {
        switch (mResult) {
        case PASS:
            return "PASS";
        case FAIL:
            return "FAIL";
        case TESTING:
            return "TESTING";
        default:
            return "NONE";
        }
    }
}
