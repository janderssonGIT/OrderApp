package com.itintegration.orderapp.di.component;

import com.itintegration.orderapp.di.PerActivity;
import com.itintegration.orderapp.di.module.ActivityModule;
import com.itintegration.orderapp.tasks.TaskFragment;
import com.itintegration.orderapp.ui.assortment.AssortmentActivity;
import com.itintegration.orderapp.ui.assortment.AssortmentFragment;
import com.itintegration.orderapp.ui.signin.SignInActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SignInActivity signInActivity);
    void inject(AssortmentActivity assortmentActivity);
    void inject(AssortmentFragment assortmentFragment);
    void inject(TaskFragment taskFragment);
}