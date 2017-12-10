package com.itintegration.orderapp.data.provider;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class DataProviderFragment extends Fragment {
    private DataProvider mDataProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        mDataProvider = new DataProvider();
    }

    public AbstractDataProvider getDataProvider() {
        return mDataProvider;
    }
}
