package com.nemezis.cloth.di.component;

import com.nemezis.cloth.di.PerActivity;
import com.nemezis.cloth.di.module.LoginActivityModule;
import com.nemezis.cloth.ui.login.LoginActivity;
import com.nemezis.cloth.ui.login.LoginFragment;

import dagger.Component;
import dagger.Subcomponent;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */

@PerActivity
@Subcomponent(modules = LoginActivityModule.class)
public interface LoginActivityComponent {

    void inject(LoginFragment loginFragment);
}
