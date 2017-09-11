package com.msi.diagnostic.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.util.Log;

public final class Utils {
    private static final String TAG = "Utils";

    private static final int BUFFER_SIZE = 8192;

    public static final int FALSE = 0;
    public static final int TRUE = 1;

    public static final String getClassAsStringName(Class<?> className) {
        return className.getName();
    }

    public static final Class<?> getClassByStringName(String className) {
        Class<?> targetClass = null;
        try {
            targetClass = Class.forName(className);

        } catch (ClassNotFoundException e) {
            Log.e(TAG, "class " + className + " is not found.");
            e.printStackTrace();
        }
        return targetClass;
    }

    public static String convertToLine(String line) {
        return String.format("%s\n", line);
    }

    public static String convertToEqual(String left, String right) {
        return String.format("%s=%s\n", left, right);
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }


    /**
     * A method to read the file with you give the path and return the content
     * 
     * @param path is used to assign path of rtc's name ,date ,time
     * @return The content of the file
     */
    public static String readContentFromFile(String path)
            throws IOException, NullPointerException {
        FileReader localFileReader = new FileReader(path);
        BufferedReader localBufferedReader = new BufferedReader(
                localFileReader, BUFFER_SIZE);
        return localBufferedReader.readLine();
    }
}
