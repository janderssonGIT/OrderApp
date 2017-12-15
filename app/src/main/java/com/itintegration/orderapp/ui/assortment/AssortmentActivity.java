package com.itintegration.orderapp.ui.assortment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.itintegration.orderapp.OrderApp;
import com.itintegration.orderapp.R;
import com.itintegration.orderapp.data.DataManager;
import com.itintegration.orderapp.di.component.ActivityComponent;
import com.itintegration.orderapp.di.component.DaggerActivityComponent;
import com.itintegration.orderapp.di.module.ActivityModule;
import com.itintegration.orderapp.ui.assortmentitemprovider.AbstractItemProvider;
import com.itintegration.orderapp.ui.assortmentitemprovider.ItemProviderFragment;

import javax.inject.Inject;

public class AssortmentActivity extends AppCompatActivity implements AssortmentFragment.Callback {
    private static final String FRAGMENT_TAG_DATA_PROVIDER = "data provider";
    private static final String FRAGMENT_LIST_VIEW = "list view";
    private Menu mOptionsMenu;

    @Inject
    DataManager mDataManager;

    private ActivityComponent activityComponent;

    //dagger 2
    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(OrderApp.get(this).getComponent())
                    .build();
        }
        return activityComponent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assortment_container);
        getActivityComponent().inject(this);

        setupToolbar();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(new ItemProviderFragment(), FRAGMENT_TAG_DATA_PROVIDER)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AssortmentFragment(), FRAGMENT_LIST_VIEW)
                    .commit();
        }
    }

    public AbstractItemProvider getDataProvider() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_DATA_PROVIDER);
        return ((ItemProviderFragment) fragment).getDataProvider();
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

    @Override
    public void onFragmentAttached() {

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
            case R.id.orderButton:
                Button btn = (Button) mOptionsMenu.findItem(R.id.orderButton);

                //TODO : Do activity transit? or switch out fragment? Hmmm. Could recreate same list with a new dataset? much better. And switch title of toolbar.
        }
        return super.onOptionsItemSelected(item);
    }
}
