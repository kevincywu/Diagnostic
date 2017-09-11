package com.msi.diagnostic.app;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import android.util.Log;

import com.msi.diagnostic.data.IDiagnoseModel;
import com.msi.diagnostic.data.MapTestSet;
import com.msi.diagnostic.data.TestCase;

/**
 * This class is used to help Level Panel to obtain the Test-Case that will be
 * launched in the next time.
 *
 * @author tonyweng
 *
 */
public final class TestCaseManager {
    private static final String TAG = "TestCaseManager";
    private static final boolean LOCAL_LOG = false;

    private IDiagnoseModel mModel;

    private MapTestSet<String, TestCase> mMapTestCases;
    private TestCaseLinkedList<TestCase> mList;
    private int mLocation = -1;

    public TestCaseManager(IDiagnoseModel model, String levelName) {
        mModel = model;
        mList = createTestCaseList(levelName);
        mMapTestCases = mapTestCases(mList);
    }

    private MapTestSet<String, TestCase>
            mapTestCases(TestCaseLinkedList<TestCase> list) {
        MapTestSet<String, TestCase> maps = new MapTestSet<String, TestCase>();
        for (TestCase t : list) {
            String key = t.getName();
            maps.put(key, t);

            Log.i(TAG, "add item: id=" + t.getId()
                    + ", name=" + t.getCaption());
        }
        return maps;
    }

    private TestCaseLinkedList<TestCase> createTestCaseList(String name) {
        ArrayList<TestCase> testCaseList = mModel.getTestCases(name);
        return new TestCaseLinkedList<TestCase>(testCaseList);
    }

    public void setLocation(int location) {
        mLocation = location;
    }

    public int getLocation() {
        return mLocation;
    }

    public ArrayList<TestCase> getList() {
        return mMapTestCases.getAllTestItems();
    }

    /**
     * Reload the content of the Specified Test-Case.
     *
     * @param name The name of the Test-Case.
     */
    public void reloadTestCase(String name) {
        TestCase testCase = getTestCase(name);
        testCase.reload();
    }

    public void reloadTestCase(long id) {
        TestCase testCase = getTestCase(id);
        testCase.reload();
    }

    /**
     * Get the first item of the Test-Case list.
     *
     * @return a Test-Case instance that is the first Test-Cast.
     */
    public TestCase getFirst() {
        return mList.get(0);
    }

    /**
     * Get the last item of the Test-Case list.
     *
     * @return a Test-Case instance that is the last Test-Case.
     */
    public TestCase getLast() {
        int lastLocation = mList.size() - 1;
        return mList.get(lastLocation);
    }

    public void setLocationByTestCase(TestCase testCase) {
        mLocation = mList.indexOf(testCase);
    }

    /**
     * Get the Test-Case by the id from the Database.
     *
     * @param id The id from the Database.
     * @return Return the Test-Case if the item is existed in the list,
     *          otherwise return null.
     */
    public TestCase getTestCase(long id) {
        ArrayList<TestCase> testCaseList = mMapTestCases.getAllTestItems();
        for (TestCase t : testCaseList) {
            long _id = t.getId();
            if (id == _id)
                return t;
        }
        return null;
    }

    /**
     * Get the Test-Case by the name.
     *
     * @param name The name of the Test-Case.
     * @return Return the Test-Case if the item is existed in the list,
     *          otherwise return null.
     */
    public TestCase getTestCase(String name) {
        return mMapTestCases.get(name);
    }

    /**
     * Get the Test-Case by the location of the list.
     *
     * @param location The location of the list.
     * @return Return the Test-Case if the item is existed in the list,
     *          otherwise return null.
     */
    public TestCase getTestCase(int location) {
        return mList.get(location);
    }

    /**
     * Get the Test-Case in the list by the name.
     *
     * @param testCase The compared Test-Case item.
     * @return Return the Test-Case if the item is existed in the list,
     *          otherwise return null.
     */
    public TestCase getTestCase(TestCase testCase) {
        String name = testCase.getName();
        return mMapTestCases.get(name);
    }

    /**
     * Get the current item of the Test-Case list.
     *
     * @return a Test-Case instance that is the current Test-Case.
     */
    public TestCase current() {
        TestCase testCase = null;
        try {
            testCase = mList.get(mLocation);

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return testCase;
    }

    public TestCase previous() {
        if ((mLocation - 1) < 0) {
            mLocation = 0;
        } else {
            setLocation(mLocation - 1);
        }
        return mList.get(mLocation);
    }

    /**
     * Get the next Test-Case from the Test-Case list.
     *
     * @return a Test-Case instance that will be launched.
     * @throws NoSuchElementException
     */
    public TestCase next() throws NoSuchElementException {
        mLocation = (mLocation + 1) % mList.size();
        return mList.get(mLocation);
    }

    /**
     * Reset the location of the pointer to the first item of Test-Case list.
     *
     * @return the first Test-Case.
     */
    public TestCase reset() {
        mLocation = 0;
        return getFirst();
    }

    public boolean isFirst() {
        return (mLocation == 0) ? true : false;
    }

    public boolean isLast() {
        int lastItemIndex = mList.size() - 1;
        return (mLocation == lastItemIndex) ? true : false;
    }
}
