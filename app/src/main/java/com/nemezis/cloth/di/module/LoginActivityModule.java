package com.nemezis.cloth.di.module;

import com.nemezis.cloth.di.PerActivity;
import com.nemezis.cloth.di.component.ApplicationComponent;
import com.nemezis.cloth.manager.AuthorizationManager;
import com.nemezis.cloth.presenter.LoginPresenter;
import com.nemezis.cloth.presenter.LoginPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */
@Module
public class LoginActivityModule {

	private ApplicationComponent applicationComponent;
	public LoginActivityModule(ApplicationComponent applicationComponent) {
		this.applicationComponent = applicationComponent;
	}

	@Provides
    @PerActivity
    LoginPresenter provideLoginPresenter(AuthorizationManager authorizationManager) {
		return new LoginPresenterImpl(authorizationManager);
	}
}
