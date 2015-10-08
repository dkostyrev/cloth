package com.nemezis.cloth.di.component;

import com.nemezis.cloth.di.PerApplication;
import com.nemezis.cloth.di.module.ApiModule;
import com.nemezis.cloth.di.module.AppModule;
import com.nemezis.cloth.di.module.GsonModule;
import com.nemezis.cloth.di.module.ManagerModule;
import com.nemezis.cloth.manager.AuthorizationManager;
import com.nemezis.cloth.presenter.LoginPresenter;
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
	void inject(LoginActivity loginActivity);
	void inject(AuthorizationManager authorizationManager);
	void inject(LoginPresenter loginPresenter);
}
