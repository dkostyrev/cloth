package com.nemezis.cloth.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nemezis.cloth.App;
import com.nemezis.cloth.di.component.ApplicationComponent;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Dmitry Kostyrev on 27/09/15
 */
public abstract class BaseActivity<ActivityComponent> extends AppCompatActivity {

	private ActivityComponent component;

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		component = setupComponent(getApp().getApplicationComponent());
	}

	public ActivityComponent getComponent() {
		return component;
	}

	protected abstract ActivityComponent setupComponent(ApplicationComponent applicationComponent);
	protected final App getApp() {
		return (App) getApplication();
	}
}
