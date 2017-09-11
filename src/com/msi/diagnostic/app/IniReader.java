/**
 * IniReader.java
 *
 * Written by : River Fan
 * Written for: N0J1 MES
 * Date : April 14, 2012
 * Version : 1.0
 */
package com.msi.diagnostic.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class is used for N0J1 MES,
 * It is used to read test log
 * @author river
 * @Version 1.0, 04/14/2012
 */
public class IniReader
{
    private boolean mFinalResult;

    public IniReader(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        mFinalResult = false;
        read(reader);
        reader.close();
    }

    public boolean getReadResult()
    {
        return mFinalResult;
    }

    protected void read(BufferedReader reader) throws IOException
    {
        String line = "";
        while ((line = reader.readLine()) != null) {
            if (parseLine(line) == true) {
                break;
            }
        }
    }

    protected boolean parseLine(String line)
    {
        line = line.trim();
        if (line.matches(".*=.*")) {
            int i = line.indexOf('=');
            String name = line.substring(0, i);
            String value = line.substring(i + 1);
            if (name.equals("Final Result")) {
                if (value.equals("PASS")) {
                    mFinalResult = true;
                    return true;
                } else {
                    mFinalResult = false;
                    return true;
                }
            }
        }
        return false;
    }
}
