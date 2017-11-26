package com.itintegration.orderapp.ui.assortment.test;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.itintegration.orderapp.R;
import com.itintegration.orderapp.data.test.AbstractDataProvider;
import com.itintegration.orderapp.data.test.DataProviderFragment;

public class NewAssortmentActivity extends AppCompatActivity {
    private static final String FRAGMENT_TAG_DATA_PROVIDER = "data provider";
    private static final String FRAGMENT_LIST_VIEW = "list view";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assortment_container);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(new DataProviderFragment(), FRAGMENT_TAG_DATA_PROVIDER)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new NewAssortmentFragment(), FRAGMENT_LIST_VIEW)
                    .commit();
        }
    }

    public AbstractDataProvider getDataProvider() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_DATA_PROVIDER);
        return ((DataProviderFragment) fragment).getDataProvider();
    }
}
