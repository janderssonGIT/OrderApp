package com.itintegration.orderapp.ui.assortment.test;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.itintegration.orderapp.R;
import com.itintegration.orderapp.ui.test.AbstractDataProvider;
import com.itintegration.orderapp.ui.test.DataProviderFragment;

public class NewAssortmentActivity extends AppCompatActivity {
    private static final String FRAGMENT_TAG_DATA_PROVIDER = "data provider";
    private static final String FRAGMENT_LIST_VIEW = "list view";
    private Menu mOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assortment_container);
        setupToolbar();

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

    private void setupToolbar() {
        Toolbar bar = findViewById(R.id.toolbar_home);
        setSupportActionBar(bar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.assortment_title);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mOptionsMenu = menu;
        getMenuInflater().inflate(R.menu.toolbar_assortment, mOptionsMenu);
        SearchView sv = (SearchView) menu.findItem(R.id.action_search).getActionView();
        sv.setMaxWidth(Integer.MAX_VALUE);
        return true;
    }

    /**
     * Vid knapptryck på "retur" knappen, kolla om sökfält innehåller sökfras.
     * Vid 'TRUE'; rensa sökfält, om FALSE; utför RETUR till föregående vy.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SearchView sv = (SearchView) mOptionsMenu.findItem(R.id.action_search).getActionView();
                if (sv.getQuery().length() > 0) {
                    sv.setQuery("", true);
                    return false;
                } else {
                    onBackPressed();
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
