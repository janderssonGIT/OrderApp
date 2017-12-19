package com.itintegration.orderapp.ui.assortment;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;

import com.itintegration.orderapp.OrderApp;
import com.itintegration.orderapp.R;
import com.itintegration.orderapp.data.DataManager;
import com.itintegration.orderapp.di.component.ActivityComponent;
import com.itintegration.orderapp.di.component.DaggerActivityComponent;
import com.itintegration.orderapp.di.module.ActivityModule;
import com.itintegration.orderapp.tasks.TaskFragment;

import java.util.List;

import javax.inject.Inject;

public class AssortmentActivity extends AppCompatActivity implements AssortmentFragment.Callback, TaskFragment.TaskCallbacks {
    private static final String FRAGMENT_LIST_VIEW = "list view";
    private static final String TAG_TASK_FRAGMENT = "task_fragment";
    private Menu mOptionsMenu;
    private SearchView mSearchView;
    private ActivityComponent activityComponent;
    private TaskFragment mTaskFragment;
    private ProgressBar mProgressBar;

    @Inject
    DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assortment_container);
        getActivityComponent().inject(this);
        setupToolbar();

        FragmentManager fm = getSupportFragmentManager();
        mTaskFragment = (TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        if (mTaskFragment == null) {
            mTaskFragment = new TaskFragment();
            getSupportFragmentManager().beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }

        if (savedInstanceState == null) {
            fm.beginTransaction()
                    .add(R.id.container, new AssortmentFragment(), FRAGMENT_LIST_VIEW)
                    .commit();
        }
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
        setupSearchView();
        return true;
    }

    private void setupSearchView() {
        mSearchView = (SearchView) mOptionsMenu.findItem(R.id.action_search).getActionView();
        mSearchView.setMaxWidth(Integer.MAX_VALUE);
        mSearchView.setIconified(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mTaskFragment.startSearchStringTask(query);

                List<String> list = mDataManager.submitArticleSearchString(query);

                //get result list via task or direct call

                //get it to adapter and notify.

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onFragmentAttached() {

    }

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //clear searchView content at 1st press on 'home'
                if (mSearchView.getQuery().length() > 0) {
                    mSearchView.setQuery("", true);
                    return false;
                } else {
                    onBackPressed();
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    return true;
                }
            case R.id.orderButton:
                Button btn = (Button) mOptionsMenu.findItem(R.id.orderButton);

                //TODO : Do activity transit? or switch out fragment? Could recreate same list with a new dataset? And switch title of toolbar.
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchResult() {

    }
}
