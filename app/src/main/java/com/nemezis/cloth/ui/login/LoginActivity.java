package com.nemezis.cloth.ui.login;

import android.os.Bundle;

import com.nemezis.cloth.R;
import com.nemezis.cloth.di.component.ApplicationComponent;
import com.nemezis.cloth.di.component.DaggerLoginActivityComponent;
import com.nemezis.cloth.di.component.LoginActivityComponent;
import com.nemezis.cloth.di.module.LoginActivityModule;
import com.nemezis.cloth.ui.activity.BaseActivity;

/**
 * Created by Dmitry Kostyrev on 27/09/15
 */
public class LoginActivity extends BaseActivity<LoginActivityComponent> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_activity);
		getSupportFragmentManager().beginTransaction()
				.add(R.id.container, new LoginFragment())
				.commit();
		getApp().getApplicationComponent().inject(this);
	}

	@Override
	protected LoginActivityComponent setupComponent(ApplicationComponent applicationComponent) {
		return DaggerLoginActivityComponent.builder()
				.applicationComponent(applicationComponent)
				.loginActivityModule(new LoginActivityModule(applicationComponent))
				.build();
	}
}
