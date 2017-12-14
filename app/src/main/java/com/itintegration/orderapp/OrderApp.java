package com.itintegration.orderapp;


import android.app.Application;
import android.content.Context;

import com.itintegration.orderapp.data.DataManager;
import com.itintegration.orderapp.di.component.ApplicationComponent;
import com.itintegration.orderapp.di.component.DaggerApplicationComponent;
import com.itintegration.orderapp.di.module.ApplicationModule;

import javax.inject.Inject;

public class OrderApp extends Application {

    protected ApplicationComponent applicationComponent;

    @Inject
    DataManager dataManager;

    public static OrderApp get(Context context) {
        return (OrderApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
    }

    public ApplicationComponent getComponent(){
        return applicationComponent;
    }

}
