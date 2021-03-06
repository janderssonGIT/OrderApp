package com.itintegration.orderapp.data;

import android.content.Context;
import android.content.res.Resources;

import com.itintegration.orderapp.data.model.User;
import com.itintegration.orderapp.data.provider.SearchArticleProvider;
import com.itintegration.orderapp.data.provider.OrderProvider;
import com.itintegration.orderapp.di.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataManager {

    private Context mContext;
    private DbHelper mDbHelper;
    private SharedPrefsHelper mSharedPrefsHelper;
    private SearchArticleProvider SearchArticleProvider;
    private OrderProvider OrderProvider;

    @Inject
    public DataManager(@ApplicationContext Context context,
                       DbHelper dbHelper,
                       SharedPrefsHelper sharedPrefsHelper) {
        mContext = context;
        mDbHelper = dbHelper;
        mSharedPrefsHelper = sharedPrefsHelper;
        SearchArticleProvider = new SearchArticleProvider();
        OrderProvider = new OrderProvider();
    }

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

    public SearchArticleProvider getSearchArticleProvider() {
        return SearchArticleProvider;
    }

    public SearchArticleProvider submitArticleSearchString(String query) {
        SearchArticleProvider = mDbHelper.getArticlesBySearchString(query);
        return SearchArticleProvider;
    }

    public OrderProvider getOrderProvider() {
        return OrderProvider;
    }

    public void setOrderProvider(OrderProvider mOrderProvider) {
        this.OrderProvider = mOrderProvider;
    }

}