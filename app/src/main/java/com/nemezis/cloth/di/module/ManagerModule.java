package com.nemezis.cloth.di.module;

import com.nemezis.cloth.App;
import com.nemezis.cloth.di.PerApplication;
import com.nemezis.cloth.di.component.ApplicationComponent;
import com.nemezis.cloth.manager.AuthorizationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */
@Module()
public class ManagerModule {

	@Provides
    @PerApplication
    AuthorizationManager provideAuthorizationManager(App applicationContext) {
		return new AuthorizationManager(applicationContext);
	}
}
