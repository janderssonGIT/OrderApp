package com.itintegration.orderapp.tasks;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.itintegration.orderapp.data.DataManager;
import com.itintegration.orderapp.di.component.ActivityComponent;
import com.itintegration.orderapp.ui.assortment.AssortmentActivity;

import javax.inject.Inject;

/*
 * This website https://www.androiddesignpatterns.com/2013/04/retaining-objects-across-config-changes.html
 * contains the information you need to understand the purpose of this Fragment.
 */

public class TaskFragment extends Fragment {

    public interface TaskCallbacks {
        void onSearchResult();
    }

    private AssortmentActivity mActivity;
    private TaskCallbacks mCallbacks;
    private SearchStringTask searchStringTask;

    @Inject
    DataManager mDataManager;

    //dagger 2
    public ActivityComponent getActivityComponent() {
        if (mActivity != null) {
            return mActivity.getActivityComponent();
        }
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AssortmentActivity) {
            AssortmentActivity activity = (AssortmentActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (TaskCallbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public void startSearchStringTask(String searchString) {
        searchStringTask = new SearchStringTask();
        searchStringTask.execute(searchString);
    }

    private class SearchStringTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            if (mCallbacks != null) {

            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            String searchTerm = strings[0];
            Object result = mDataManager.submitArticleSearchString(searchTerm);

            /* Do something with result and use mCallbacks to return result in the onPostExecute.
            * other asyntasks can be added in this fragment, and they may have their own interfaces declared inside this class
            */

            return null;
        }

        @Override
        protected void onCancelled() {
            if (mCallbacks != null) {

            }
        }

        @Override
        protected void onPostExecute(Void ignore) {
            if (mCallbacks != null) {
                mCallbacks.onSearchResult();
            }
        }
    }
}