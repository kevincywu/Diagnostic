/**
 * SMB.java
 *
 * Written by : River Fan
 * Written for: N0J1 MES
 * Date : April 14, 2012
 * Version : 1.0
 */
package com.msi.diagnostic.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

/**
 * This class is used for download file form server
 * @author river
 */
public class SMB
{
    private String mRemoteUrl;
    private String mLocalDir;

    public SMB(String remoteUrl, String localDir) {
        super();
        this.mRemoteUrl = remoteUrl;
        this.mLocalDir = localDir;
    }

    public long smbDownLoad()
    {
        InputStream in = null;
        OutputStream out = null;
        SmbFile remotefile = null;
        File localFile = null;
        long startTime = 0;
        long costtime = 0;
        try {
            remotefile = new SmbFile(mRemoteUrl);
            remotefile.connect();
            remotefile.setConnectTimeout(5000);
            String fileName = remotefile.getName();
            localFile = new File(mLocalDir + File.separator + fileName);
            startTime = System.currentTimeMillis();
            in = new BufferedInputStream(new SmbFileInputStream(remotefile));
            out = new BufferedOutputStream(new FileOutputStream(localFile));
            byte[] buffer = new byte[1024];

            while (in.read(buffer) != -1) {
                out.write(buffer);
                buffer = new byte[1024];
            }
            long endTime = System.currentTimeMillis();
            costtime = endTime - startTime;
            if (costtime > 0) {
                if (localFile.exists() && localFile != null) {
                    return costtime;
                }
            }
        } catch (SmbException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
