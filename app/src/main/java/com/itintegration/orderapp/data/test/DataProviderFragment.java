package com.itintegration.orderapp.data.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class DataProviderFragment extends Fragment {
    private DataProvider mDataProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);  // keep the mDataProvider instance
        mDataProvider = new DataProvider();
    }

    public AbstractDataProvider getDataProvider() {
        return mDataProvider;
    }
}
