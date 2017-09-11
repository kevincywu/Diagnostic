package com.msi.diagnostic.ui;

import java.lang.ref.WeakReference;

import com.msi.diagnostic.app.IPresenter;

import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ItemClickListener implements OnItemClickListener {

    private WeakReference<IPresenter> mPresenterWeakRef;

    public ItemClickListener(IPresenter presenter) {
        mPresenterWeakRef = new WeakReference<IPresenter>(presenter);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
        IPresenter presenter = mPresenterWeakRef.get();
        presenter.OnButtonClick(pos);
    }
}
