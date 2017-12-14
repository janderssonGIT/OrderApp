package com.itintegration.orderapp.ui.assortmentitemprovider;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class ItemProviderFragment extends Fragment {
    private ItemProvider mDataProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        mDataProvider = new ItemProvider();
    }

    public AbstractItemProvider getDataProvider() {
        return mDataProvider;
    }
}
