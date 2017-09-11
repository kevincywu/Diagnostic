package com.msi.diagnostic.data;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.msi.diagnostic.R;
import com.msi.diagnostic.app.IDiagnosticApp;

import android.content.Context;
import android.content.res.XmlResourceParser;

public class TestItemLoader {

    private static final String TAG_DEVICE = "device";
    private static final String TAG_NAME = "name";
    private static final String TAG_FILE = "file";
    private static final String TAG_CAPTION = "caption";
    private static final String TAG_CRITERIA_TYPE = "type";
    private static final String TAG_THRESHOLD_CRITERIA_MIN = "min";
    private static final String TAG_THRESHOLD_CRITERIA_MAX = "max";
    private static final String TAG_DEFINITION_CRITERIA_DEFINE = "define";
    private static final String TAG_DEFINITION_CRITERIA_DETECT = "detect";

    private static final String TYPE_CRITERIA_THRESHOLD = "Threshold";
    private static final String TYPE_CRITERIA_DEFINITION = "Definition";
    private static final String TYPE_CRITERIA_DETECTION = "Detection";

    private Context mContext;
    private IDiagnosticApp mApp;
    private DeviceConfig mDeviceConfig;

    private TestSet mTestSet = new TestSet();

    public TestItemLoader(Context context) {
        mContext = context;
        mApp = (IDiagnosticApp) context.getApplicationContext();
        mDeviceConfig = mApp.getDeviceConfig();
    }

    /**
     * Load all the Test Items from XML file.
     *
     * TODO:    Prevent the ANR issue, we should do this operation in a
     *          separated thread. In addition, we should improve the readability.
     *
     * @param targetDeviceName The name which board will be tested.
     * @return The set of a Tests such as TestLevel, TestCase, and TestItem.
     * @throws XmlPullParserException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private TestSet loadTestLevelFromXML(String targetDeviceName)
            throws XmlPullParserException, IOException, ClassNotFoundException {

        XmlResourceParser parser =
                mContext.getResources().getXml(R.xml.test_cases_definition);

        final int depth = parser.getDepth();
        int type;
        while (((type = parser.next()) != XmlPullParser.END_TAG ||
                parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {

            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            String elementName = parser.getName();
            if (TAG_DEVICE.equals(elementName)) {
                String deviceName = parser.getAttributeValue(null, TAG_NAME);
                if (deviceName.equals(targetDeviceName)) {

                    /* Parsing the Test Level */
                    final int levelDepth = parser.getDepth();
                    while ((type = parser.next()) != XmlPullParser.END_TAG ||
                            parser.getDepth() > levelDepth) {

                        String testLevelName =
                                parser.getAttributeValue(null, TAG_NAME);
                        String testLevelCaption =
                                parser.getAttributeValue(null, TAG_CAPTION);
                        mTestSet.add(new TestLevel(mApp, testLevelName, testLevelCaption));

                        /* Parsing the Test Case */
                        final int testCaseDepth = parser.getDepth();
                        while ((type = parser.next()) != XmlPullParser.END_TAG ||
                                parser.getDepth() > testCaseDepth) {

                            String testCaseName =
                                    parser.getAttributeValue(null, TAG_NAME);
                            String testCaseCaption = parser.getAttributeValue(
                                    null, TAG_CAPTION);
                            TestCase testCase = new TestCase(
                                    mApp,
                                    testCaseName,
                                    testLevelName,
                                    testCaseCaption,
                                    TestResult.create(TestResult.NONE));
                            mTestSet.add(testCase);

                            /* Parsing the Test Item */
                            final int testItemDepth = parser.getDepth();
                            while ((type = parser.next()) != XmlPullParser.END_TAG ||
                                    parser.getDepth() > testItemDepth) {
                                String testItemName =
                                        parser.getAttributeValue(null, TAG_NAME);
                                String testItemFile =
                                        parser.getAttributeValue(null, TAG_FILE);
                                TestItem item = null;
                                /* Parsing the Test Criteria */
                                final int criteriaDepth = parser.getDepth();
                                if ((type = parser.next()) != XmlPullParser.END_TAG ||
                                        parser.getDepth() > criteriaDepth) {

                                    String criteriaType =
                                            parser.getAttributeValue(null, TAG_CRITERIA_TYPE);

                                    if (TYPE_CRITERIA_THRESHOLD.equals(criteriaType)) {
                                        float min = parser.getAttributeFloatValue(
                                                null, TAG_THRESHOLD_CRITERIA_MIN, 0);
                                        float max = parser.getAttributeFloatValue(
                                                null, TAG_THRESHOLD_CRITERIA_MAX, 0);
                                        item = new ThresholdTestItem(
                                                mApp,
                                                testItemName,
                                                testItemFile,
                                                testLevelName,
                                                testCaseName,
                                                max, min,
                                                TestResult.create(TestResult.NONE));

                                    } else if (TYPE_CRITERIA_DEFINITION.equals(criteriaType)) {
                                        String define = parser.getAttributeValue(
                                                null, TAG_DEFINITION_CRITERIA_DEFINE);
                                        item = new DefinitionTestItem(
                                                mApp,
                                                testItemName,
                                                testItemFile,
                                                testLevelName,
                                                testCaseName,
                                                define,
                                                TestResult.create(TestResult.NONE));

                                    } else if (TYPE_CRITERIA_DETECTION.equals(criteriaType)) {
                                        boolean mustBeDetected = parser.getAttributeBooleanValue(
                                                null, TAG_DEFINITION_CRITERIA_DETECT, true);
                                        item = new DetectionTestItem(
                                                mApp,
                                                testItemName,
                                                testItemFile,
                                                testLevelName,
                                                testCaseName,
                                                mustBeDetected,
                                                TestResult.create(TestResult.NONE));
                                    }
                                }
                                mTestSet.add(item);
                            }
                        }
                    }
                }
            }
        }
        return mTestSet;
    }

    /**
     * Get a set of test level from a XML file for the specific device.
     *
     * @return The set of a Tests such as TestLevel, TestCase, and TestItem.
     */
    public synchronized TestSet loadDeviceTestLevels() {
        try {
            String targetBoardName = mDeviceConfig.getDeviceName();
            return loadTestLevelFromXML(targetBoardName);

        } catch (XmlPullParserException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
