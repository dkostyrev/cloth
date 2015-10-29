package com.nemezis.cloth.di.component;

import com.nemezis.cloth.di.PerApplication;
import com.nemezis.cloth.di.module.ApiModule;
import com.nemezis.cloth.di.module.AppModule;
import com.nemezis.cloth.di.module.GsonModule;
import com.nemezis.cloth.di.module.LoginActivityModule;
import com.nemezis.cloth.di.module.MainActivityModule;
import com.nemezis.cloth.di.module.ManagerModule;
import com.nemezis.cloth.manager.ApplicationsManager;
import com.nemezis.cloth.manager.AuthorizationManager;
import com.nemezis.cloth.manager.UserManager;
import com.nemezis.cloth.presenter.ApplicationsPresenter;
import com.nemezis.cloth.presenter.ApplicationsPresenterImpl;
import com.nemezis.cloth.presenter.LoginPresenter;
import com.nemezis.cloth.presenter.LoginPresenterImpl;
import com.nemezis.cloth.ui.activity.StartupActivity;
import com.nemezis.cloth.ui.login.LoginActivity;

import dagger.Component;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */
@PerApplication
@Component(modules = {
        AppModule.class,
        ApiModule.class,
        ManagerModule.class,
        GsonModule.class
})
public interface ApplicationComponent {

    LoginActivityComponent plus(LoginActivityModule loginActivityModule);

    MainActivityComponent plus(MainActivityModule mainActivityModule);

    void inject(AuthorizationManager authorizationManager);

    void inject(LoginPresenterImpl loginPresenter);

    void inject(ApplicationsPresenterImpl applicationsPresenter);

    void inject(ApplicationsManager applicationsManager);

    void inject(UserManager userManager);

    void inject(StartupActivity startupActivity);
}
