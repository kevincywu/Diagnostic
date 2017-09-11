
package com.msi.diagnostic.app.logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.impl.cookie.DateUtils;

import com.msi.diagnostic.app.TestCaseManager;
import com.msi.diagnostic.data.TestCase;
import com.msi.diagnostic.data.TestItem;
import com.msi.diagnostic.data.TestResult;
import com.msi.diagnostic.utils.Utils;

public final class LogParser {

    private static final String NAME_TIME_ROW = "TIME";
    private static final String CONCATENATION = "=";
    private static final String NEW_LINE = "\n";

    private static String convertToDateTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        return DateUtils.formatDate(date, DateUtils.PATTERN_ASCTIME);
    }

    private static String getTitle(String titleName) {
        return "[" + titleName + "]";
    }

    private static String getTime(long time) {
        String convertToDateTimeString = new String();

        // The test case is not executed. we give it a special string value
        if (time == 0) {
            convertToDateTimeString = "NONE";
        }
        else {
            convertToDateTimeString = convertToDateTime(time);
        }
        return NAME_TIME_ROW + CONCATENATION + convertToDateTimeString;
    }

    private static String getTestItemResult(String itemName, int result) {
        TestResult r = TestResult.create(result);
        return itemName + CONCATENATION + r.getResultAsString();
    }

    /**
     * Query all the log of Test-Case & Test-Item from Database.
     * 
     * @param testCaseMgr The provider of the TestCase
     * @return The log content
     */
    public static StringBuffer parseLogToBuffer(TestCaseManager testCaseMgr, String barCode) {
        StringBuffer buffer = new StringBuffer();

        ArrayList<TestCase> testCases = testCaseMgr.getList();
        for (TestCase t : testCases) {
            String title = getTitle(t.getCaption());
            String time = getTime(t.getTime());

            String _title = Utils.convertToLine(title);
            String _time = Utils.convertToLine(time);
            buffer.append(_title);
            buffer.append(_time);

            ArrayList<TestItem> items = t.getTestItems();
            for (TestItem i : items) {
                String itemName = i.getName();
                int result = i.getResult();
                String itemResult;
                if(itemName.equals("isNumberExisted"))
                    itemResult = "barcode=" + barCode;
                else
                    itemResult = getTestItemResult(itemName, result);
                //String itemResult = getTestItemResult(itemName, result);
                String _itemResult = Utils.convertToLine(itemResult);
                buffer.append(_itemResult);
            }

            buffer.append(NEW_LINE);
        }

        return buffer;
    }
}
