package com.itintegration.orderapp.data;

import android.content.Context;
import android.content.res.Resources;

import com.itintegration.orderapp.data.model.User;
import com.itintegration.orderapp.di.ApplicationContext;
import com.itintegration.orderapp.ui.assortmentitemprovider.AbstractItemProvider;
import com.itintegration.orderapp.ui.assortmentitemprovider.ItemProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataManager {

    private Context mContext;
    private DbHelper mDbHelper;
    private SharedPrefsHelper mSharedPrefsHelper;
    private ItemProvider mItemProvider;

    @Inject
    public DataManager(@ApplicationContext Context context,
                       DbHelper dbHelper,
                       SharedPrefsHelper sharedPrefsHelper) {
        mContext = context;
        mDbHelper = dbHelper;
        mSharedPrefsHelper = sharedPrefsHelper;
        mItemProvider = new ItemProvider();
    }

    //SharedPrefs seem to be some sort of global HashMap?
    public void saveAccessToken(String accessToken) {
        mSharedPrefsHelper.put(SharedPrefsHelper.PREF_KEY_ACCESS_TOKEN, accessToken);
    }

    public String getAccessToken(){
        return mSharedPrefsHelper.get(SharedPrefsHelper.PREF_KEY_ACCESS_TOKEN, null);
    }

    public Long createUser(User user) throws Exception {
        return mDbHelper.insertUser(user);
    }

    public User getUser(Long userId) throws Resources.NotFoundException, NullPointerException {
        return mDbHelper.getUser(userId);
    }

    //TODO : Remove. Used for early hardcoded view testing.
    public AbstractItemProvider getDataProvider() {
        return mItemProvider;
    }
}