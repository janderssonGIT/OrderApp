package com.itintegration.orderapp.di.component;


import android.app.Application;
import android.content.Context;

import com.itintegration.orderapp.OrderApp;
import com.itintegration.orderapp.data.DataManager;
import com.itintegration.orderapp.data.DbHelper;
import com.itintegration.orderapp.data.SharedPrefsHelper;
import com.itintegration.orderapp.data.test.MsSQLConnection;
import com.itintegration.orderapp.di.ApplicationContext;
import com.itintegration.orderapp.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(OrderApp orderApp);

    @ApplicationContext
    Context getContext();

    Application getApplication();

    DataManager getDataManager();

    SharedPrefsHelper getPreferenceHelper();

    DbHelper getDbHelper();

}
