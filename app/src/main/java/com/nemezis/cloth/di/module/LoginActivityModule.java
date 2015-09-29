package com.nemezis.cloth.di.module;

import com.nemezis.cloth.App;
import com.nemezis.cloth.di.component.ApplicationComponent;
import com.nemezis.cloth.presenter.LoginPresenter;

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
	LoginPresenter provideLoginPresenter() {
		return new LoginPresenter(applicationComponent);
	}
}
