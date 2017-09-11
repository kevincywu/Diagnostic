package com.msi.diagnostic.ui;

import java.lang.ref.WeakReference;

import com.msi.diagnostic.app.IPresenter;

import android.view.View;
import android.view.View.OnClickListener;

public final class ButtonClickListener implements OnClickListener {

    private WeakReference<IPresenter> mPresenterWeakRef;

    public ButtonClickListener(IPresenter presenter) {
        mPresenterWeakRef = new WeakReference<IPresenter>(presenter);
    }

    @Override
    public synchronized void onClick(View v) {
        IPresenter presenter = mPresenterWeakRef.get();
        final int viewId = v.getId();
        presenter.OnButtonClick(viewId);
    }

}
