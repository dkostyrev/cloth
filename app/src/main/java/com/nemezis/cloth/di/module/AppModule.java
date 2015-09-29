package com.nemezis.cloth.di.module;

import android.content.Context;

import com.nemezis.cloth.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */
@Module
public class AppModule {

	private final App app;

	public AppModule(App app) {
		this.app = app;
	}

	@Provides
	@Singleton
	App provideApplicationContext() {
		return app;
	}

}
