package com.msi.diagnostic.app;

import com.msi.diagnostic.ui.RuninWlanTestCaseView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;

public class DownLoadTask extends AsyncTask<String, Void, Boolean> {

    private String mTestIP;
    private RuninWlanTestCaseView mView;

    public DownLoadTask(String downLoadIp, RuninWlanTestCaseView view) {
        mTestIP = downLoadIp;
        mView = view;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        downLoad();
        return null;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        sendBroadcast("Start download from server");
    }

    /**
     * @param state
     */
    public void sendBroadcast(String state)
    {
        Intent intent = new Intent();
        intent.setAction("DownLoadState");
        intent.putExtra("State", state);
        mView.getAppContext().sendBroadcast(intent);
    }

    public void downLoad()
    {
        new Thread(new Runnable() {
            public void run()
            {
                for (int i = 1; i < 4; i++) {
                    // from smb://10.101.0.8/user/queen.rar" to "/mnt/sdcard"
                    SMB mSMB = new SMB("smb:" + mTestIP, Environment.getExternalStorageDirectory()
                            .toString());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    long costTime = mSMB.smbDownLoad();
                    if (costTime != 0) {
                        sendBroadcast("Start download from server \n number: " + i
                                + "result pass \n" + "cost time : " + costTime);
                        break;
                    } else {
                        sendBroadcast("Start download from server number: " + i + " result fail");
                    }
                }
            }
        }).start();
    }

}
