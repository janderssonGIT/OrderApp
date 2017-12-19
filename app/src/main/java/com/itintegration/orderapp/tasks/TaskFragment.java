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

    public void startSearchStringTask(String query) {
        searchStringTask = new SearchStringTask();
        searchStringTask.execute(query);
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
            Object obj = mDataManager.submitArticleSearchString(searchTerm);

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