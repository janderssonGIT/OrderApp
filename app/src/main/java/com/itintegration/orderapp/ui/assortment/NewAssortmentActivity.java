package com.itintegration.orderapp.ui.assortment;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.itintegration.orderapp.R;

public class NewAssortmentActivity extends AppCompatActivity {
    private static final String FRAGMENT_LIST_VIEW = "list view";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assortment_container);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AssortmentFragment(), FRAGMENT_LIST_VIEW)
                    .commit();
        }
    }

}
