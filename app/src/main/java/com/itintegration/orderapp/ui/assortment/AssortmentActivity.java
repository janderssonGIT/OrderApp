package com.itintegration.orderapp.ui.assortment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itintegration.orderapp.OrderApp;
import com.itintegration.orderapp.R;
import com.itintegration.orderapp.data.DataManager;
import com.itintegration.orderapp.data.provider.SearchArticleProvider;
import com.itintegration.orderapp.di.component.ActivityComponent;
import com.itintegration.orderapp.di.component.DaggerActivityComponent;
import com.itintegration.orderapp.di.module.ActivityModule;
import com.itintegration.orderapp.tasks.TaskFragment;
import com.itintegration.orderapp.ui.order.OrderActivity;

import javax.inject.Inject;

public class AssortmentActivity extends AppCompatActivity implements TaskFragment.TaskCallbacks, View.OnTouchListener,
            EditText.OnEditorActionListener, View.OnFocusChangeListener{
    private static final String ASSORTMENT_FRAGMENT_LIST_VIEW = "assortment_list_view";
    private static final String TAG_TASK_FRAGMENT = "task_fragment_assortment";
    private EditText mSearchView;
    private Context mContext;
    private ActivityComponent activityComponent;
    private FragmentManager mFragmentManager;
    private TaskFragment mTaskFragment;
    private Menu mOptionsMenu;
    private ProgressBar mProgressBar; //Show using Taskfragment callback to activate/dismiss when Taskfragment is processing REST-call.

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
        mFragmentManager = getSupportFragmentManager();
        mContext = this;
        setContentView(R.layout.assortment_activity);
        getActivityComponent().inject(this);
        setupToolbar();
        setupSearchBar();

        mTaskFragment = (TaskFragment) mFragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);
        if (mTaskFragment == null) {
            mTaskFragment = new TaskFragment();
            mFragmentManager.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }

        if (savedInstanceState == null) {
            mFragmentManager.beginTransaction()
                    .add(R.id.container, new AssortmentFragment(), ASSORTMENT_FRAGMENT_LIST_VIEW)
                    .commit();
        }
    }

    private void setupToolbar() {
        /**
            The "go to cart"-button is set up differently through the onCreateOptionsMenu, and its layout is inside res/menu/toolbar_assortment.
            The optionsMenu is best for toolbar button placement and also allows for better interaction between widget elements that uses the
            appcompat support widget package. The exception here is the SearchView that is declared in setupSearchBar() as there were some experimenting with
             CoordinatorLayout and the searchBar. It's possible to have it in the onCreateOptionsMenu but it's clunky to configure and takes too much space
             even when helpful animations are added to shrink/enlarge it and also hides the "go to cart"-button when attempting to search.
         */
        Toolbar bar = findViewById(R.id.toolbar_assortment);
        setSupportActionBar(bar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.assortment_title);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mOptionsMenu = menu;
        getMenuInflater().inflate(R.menu.toolbar_assortment, mOptionsMenu);
        return true;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupSearchBar() {
        mSearchView = findViewById(R.id.searchEditText);
        mSearchView.setOnTouchListener(this);
        mSearchView.setOnEditorActionListener(this);
        mSearchView.setOnFocusChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                return true;
            case R.id.orderButton:
                if (mDataManager.getOrderProvider().getArticleListGroupCount() > 0) {
                    Intent intent = new Intent(this, OrderActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    return true;
                } else {
                    Toast.makeText(mContext, R.string.noOrderAdded, Toast.LENGTH_LONG).show();
                    return false;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        final int DRAWABLE_RIGHT = 2;
        if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if(motionEvent.getRawX() >= (mSearchView.getRight() - mSearchView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width() + 50)) {
                mSearchView.setFocusableInTouchMode(true);
                mSearchView.requestFocus();
                final InputMethodManager inputMethodManager = (InputMethodManager) mContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(mSearchView, InputMethodManager.SHOW_IMPLICIT);
                view.performClick();
            }
        }
        return false;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        //TODO : Implement TaskFragment when the Rest API is implemented.
                /**
                    Is needed when using REST API and not local db. We want to use Taskfragment to hold an AsyncTask process alive if the user decides to
                    turn the phone to landscape/portrait. As this interrupts an normal AsyncTask process as the Activity is onDestroy()-ed, we need a TaskFragment to
                    hold the AsyncTask alive and hold the result until it gets a new reference to the recreated Activity and pass the result to that new reference.

                    This one of the more annoying "perks" of coding Android apps..

                    Implement this:
                    SearchArticleProvider provider = mTaskFragment.startSearchStringTask(textView.getText().toString());
                 */

        SearchArticleProvider provider = mDataManager.submitArticleSearchString(textView.getText().toString());
        FragmentManager fm = getSupportFragmentManager();
        AssortmentFragment mAssortmentFragment = (AssortmentFragment) fm.findFragmentByTag(ASSORTMENT_FRAGMENT_LIST_VIEW);
        if (provider != null) {
            mAssortmentFragment.updateProvider();
            InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(textView.getRootView().getWindowToken(), 0);
        } else {
            Toast.makeText(getApplicationContext(), R.string.searchNoSearchResult,
                    Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    public void onSearchResult() {

    }

    public void onFragmentAttached() {

    }
}
