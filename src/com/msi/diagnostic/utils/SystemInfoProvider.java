package com.msi.diagnostic.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;

import android.os.Environment;
import android.os.StatFs;
import android.text.format.Time;

public final class SystemInfoProvider {

    public static String getOSVersion() {
        String osVersion;
        osVersion = android.os.Build.VERSION.RELEASE;
        return osVersion;
    }

    public static String getModel() {
        String model;
        model = android.os.Build.MODEL;
        return model;
    }

    public static String getBuildNumber() {
        String buildNumber;
        buildNumber = android.os.Build.DISPLAY;
        return buildNumber;
    }

    public static String getSerialNumber() {
        String serial = "null";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception ignored) {

        }
        return serial;
    }

    public static String getDate() {
        Time t = new Time();
        t.setToNow(); // get the system time
        int year = t.year;
        int month = t.month + 1;
        int date = t.monthDay;
        String Data = year + "/" + month + "/" + date;
        return Data;
    }

    public static String getTime() {
        Time t = new Time();
        t.setToNow(); // get the system time
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        String Time = hour + ":" + minute;
        return Time;
    }

    public static String getStorageSize() {
        String size;
        File path = Environment.getExternalStorageDirectory();
        StatFs state = new StatFs(path.getPath());
        long blockSize = state.getBlockSize();
        long blockCount = state.getBlockCount();
        long availableCount = state.getAvailableBlocks();
        long totalSpace = blockCount * blockSize;
        long freeSpace = availableCount * blockSize;
        size = formatSize(totalSpace);
        return size;
    }

    public static String getMemorySize() {
        RandomAccessFile reader = null;
        String memSize = null;
        String[] splitArray = null;
        String strSizeKB = null;
        String strSizeMB = null;
        final long sizeByte;
        final long sizeKB;
        final int MEMORY_SIZE_INDEX = 1;
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            memSize = reader.readLine();
            splitArray = memSize.split("\\s+");
            strSizeKB = splitArray[MEMORY_SIZE_INDEX];
            sizeKB = Long.parseLong(strSizeKB);
            sizeByte = sizeKB * 1024;
            strSizeMB = formatSize(sizeByte);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strSizeMB;
    }

    private static String formatSize(long size) {
        String suffix = null;
        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }
        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));
        if (suffix != null)
            resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    public static final class COLUMN {
        public static final String APP_VERSION      = "App Version";
        public static final String DATE             = "Date";
        public static final String OS_VERSION       = "OS Version";
        public static final String MODEL            = "Model";
        public static final String BUILD_NUMBER     = "Build Number";
        public static final String SN               = "Serial Number";
        public static final String STORAGE_SIZE     = "Storage Size";
        public static final String MEMORY_SIZE      = "Memory Size";
    };
}
