package com.msi.diagnostic.app.logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.msi.diagnostic.app.TestCaseManager;
import com.msi.diagnostic.utils.SystemInfoProvider;
import com.msi.diagnostic.utils.Utils;

import android.os.Environment;

public final class LogManager {

    private static final File FILE_PATH = Environment.getExternalStorageDirectory();
    private static final String DEFAULT_FILENAME = "default_log";

    private TestCaseManager mTestCaseMgr;

    public LogManager(TestCaseManager tMgr) {
        mTestCaseMgr = tMgr;
    }

    private StringBuffer getSystemInfo() {
        StringBuffer buf = new StringBuffer();

        String date = SystemInfoProvider.getDate();
        String osVersion = SystemInfoProvider.getOSVersion();
        String model = SystemInfoProvider.getModel();
        String buildNumber = SystemInfoProvider.getBuildNumber();
        String sn = SystemInfoProvider.getSerialNumber();
        String storageSize = SystemInfoProvider.getStorageSize();
        String memorySize = SystemInfoProvider.getMemorySize();

        String _date = Utils.convertToEqual(
                SystemInfoProvider.COLUMN.DATE,
                date);
        String _osVersion = Utils.convertToEqual(
                SystemInfoProvider.COLUMN.OS_VERSION,
                osVersion);
        String _model = Utils.convertToEqual(
                SystemInfoProvider.COLUMN.MODEL,
                model);
        String _buildNumber = Utils.convertToEqual(
                SystemInfoProvider.COLUMN.BUILD_NUMBER,
                buildNumber);
        String _sn = Utils.convertToEqual(
                SystemInfoProvider.COLUMN.SN,
                sn);
        String _storageSize = Utils.convertToEqual(
                SystemInfoProvider.COLUMN.STORAGE_SIZE,
                storageSize);
        String _memorySize = Utils.convertToEqual(
                SystemInfoProvider.COLUMN.MEMORY_SIZE,
                memorySize);

        buf.append(_date);
        buf.append(_osVersion);
        buf.append(_model);
        buf.append(_buildNumber);
        buf.append(_sn);
        buf.append(_storageSize);
        buf.append(_memorySize);
        buf.append("\n");

        return buf;
    }

    /**
     * Save all the system information and test log into a file.
     *
     * @param fileName the file-name of the log file.
     * @return return true if success, otherwise is failed to access the log file.
     */
    public boolean saveToFile(String fileName, String barCode) {
        if (fileName == null)
            fileName = DEFAULT_FILENAME;

        try {
            File logFile = new File(FILE_PATH, fileName);
            StringBuffer sysInfoBuf = getSystemInfo();
            StringBuffer logBuf = LogParser.parseLogToBuffer(mTestCaseMgr, barCode);
            BufferedOutputStream outStream =
                    new BufferedOutputStream(new FileOutputStream(logFile));
            byte[] outSysBuf = sysInfoBuf.toString().getBytes();
            byte[] outLogBuf = logBuf.toString().getBytes();
            outStream.write(outSysBuf);
            outStream.write(outLogBuf);
            outStream.close();

        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getLog(String barCode){
        String log = "";
        StringBuffer sysInfoBuf = getSystemInfo();
        StringBuffer logBuf = LogParser.parseLogToBuffer(mTestCaseMgr, barCode);
        log = sysInfoBuf.toString()+logBuf.toString();
        return log;
    }
}
