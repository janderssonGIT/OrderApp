package com.itintegration.orderapp.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.itintegration.orderapp.OrderApp;
import com.itintegration.orderapp.R;
import com.itintegration.orderapp.data.DataManager;
import com.itintegration.orderapp.di.component.ActivityComponent;
import com.itintegration.orderapp.di.component.DaggerActivityComponent;
import com.itintegration.orderapp.di.module.ActivityModule;
import com.itintegration.orderapp.ui.settings.SettingsActivity;
import com.itintegration.orderapp.ui.signin.SignInActivity;

import org.w3c.dom.Text;

import javax.inject.Inject;

public class OrderActivity extends AppCompatActivity implements OrderFragment.Callback, PopupMenu.OnMenuItemClickListener{
    private static final String ORDER_FRAGMENT_LIST_VIEW = "order_list view";
    private static final String TAG_TASK_FRAGMENT = "task_fragment_order";
    private ActivityComponent activityComponent;
    private ActionBar mActionBar;
    private Context mContext;
    private FragmentManager mFragmentManager;
    private Menu mOptionsMenu;
    private MenuItem deleteOk;
    private MenuItem deleteCancel;
    private MenuItem dropdownItem;

    @Inject
    DataManager mDataManager;

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
        mContext = getApplicationContext();
        mFragmentManager = getSupportFragmentManager();
        setContentView(R.layout.order_activity);
        getActivityComponent().inject(this);
        setupToolbar();

        if (savedInstanceState == null) {
            mFragmentManager.beginTransaction()
                    .add(R.id.container, new OrderFragment(), ORDER_FRAGMENT_LIST_VIEW)
                    .commit();
        }
    }

    private void setupToolbar() {
        Toolbar bar = findViewById(R.id.toolbar_order );
        setSupportActionBar(bar);
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(R.string.order_title);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mOptionsMenu = menu;
        getMenuInflater().inflate(R.menu.toolbar_order, mOptionsMenu);
        deleteOk = mOptionsMenu.findItem(R.id.deleteOK);
        deleteCancel = mOptionsMenu.findItem(R.id.deleteCancel);
        dropdownItem = mOptionsMenu.findItem(R.id.dropdownItem);
        return true;
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.order_popup, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dropdownItemDeleteOrders:
                deleteCancel.setVisible(true);
                deleteOk.setVisible(true);
                dropdownItem.setVisible(false);
                mActionBar.setDisplayShowTitleEnabled(false);

                //TODO : mOrderFragment.checkBoxesVisible(true);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        OrderFragment mOrderFragment = (OrderFragment) mFragmentManager.findFragmentByTag(ORDER_FRAGMENT_LIST_VIEW);
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                return true;

            case R.id.dropdownItem:
                showPopup(findViewById(R.id.dropdownItem));
                return true;

            case R.id.deleteOK:
                // TODO : mDataManager.getSearchArticleProvider().removeItemsMarkedForDeletion();
                deleteOk.setVisible(false);
                deleteCancel.setVisible(false);
                dropdownItem.setVisible(true);
                mActionBar.setDisplayShowTitleEnabled(true);
                // TODO : mOrderFragment.notify();
                return true;

            case R.id.deleteCancel:
                deleteOk.setVisible(false);
                deleteCancel.setVisible(false);
                dropdownItem.setVisible(true);
                mActionBar.setDisplayShowTitleEnabled(true);
                //TODO : mOrderFragment.checkBoxesVisible(false);
                return true;
            //TODO : undo checkbox reveal and hide deleteGroup
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentAttached() {

    }
}
