package com.nemezis.cloth;

import android.app.Application;
import android.content.Context;

import com.nemezis.cloth.di.component.ApplicationComponent;
import com.nemezis.cloth.di.component.DaggerApplicationComponent;
import com.nemezis.cloth.di.module.AppModule;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Dmitry Kostyrev on 27/09/15
 */
public class App extends Application {

	private ApplicationComponent applicationComponent;

	public static App get(Context applicationContext) {
		return (App) applicationContext.getApplicationContext();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
						.setDefaultFontPath("fonts/Roboto-Regular.ttf")
						.setFontAttrId(R.attr.fontPath)
						.build()
		);

		applicationComponent = DaggerApplicationComponent.builder()
				.appModule(new AppModule(this))
				.build();

	}

	public ApplicationComponent getApplicationComponent() {
		return applicationComponent;
	}
}
