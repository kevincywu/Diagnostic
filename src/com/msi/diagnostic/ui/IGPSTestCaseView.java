package com.msi.diagnostic.ui;

public interface IGPSTestCaseView {
    public void showCN(String CN_Str);
    public void setCNTextColor(int color);
    public void setFailButtonVisiable(int visiable_code);
    public void setCountDownVisiable(int visiable_code);
    public void setCountDownText(String count_down_text);
}
