package com.nemezis.cloth;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Dmitry Kostyrev on 27/09/15
 */
public class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
						.setDefaultFontPath("fonts/Roboto-Regular.ttf")
						.setFontAttrId(R.attr.fontPath)
						.build()
		);
	}
}
