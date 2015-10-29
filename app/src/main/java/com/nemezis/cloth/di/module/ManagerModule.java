package com.nemezis.cloth.di.module;

import com.nemezis.cloth.App;
import com.nemezis.cloth.di.PerApplication;
import com.nemezis.cloth.di.component.ApplicationComponent;
import com.nemezis.cloth.manager.AuthorizationManager;
import com.nemezis.cloth.manager.DatabaseManager;
import com.nemezis.cloth.manager.UserManager;

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
    public AuthorizationManager provideAuthorizationManager(App app) {
		return new AuthorizationManager(app);
	}

    @Provides
    @PerApplication
    public DatabaseManager provideDatabaseManager(App app) {
        return new DatabaseManager(app);
    }

    @Provides
    @PerApplication
    public UserManager provideUserManager(App app) {
        return new UserManager(app);
    }
}
