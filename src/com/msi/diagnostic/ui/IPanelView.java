package com.msi.diagnostic.ui;

import android.os.Bundle;

public interface IPanelView {
    public String getLevelName();
    public void navigate(Class<?> className);
    public void navigate(Class<?> className, Bundle data);
}
