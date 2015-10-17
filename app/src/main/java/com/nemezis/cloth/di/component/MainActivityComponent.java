package com.nemezis.cloth.di.component;

import com.nemezis.cloth.di.PerActivity;
import com.nemezis.cloth.di.module.MainActivityModule;
import com.nemezis.cloth.ui.applications.ApplicationsFragment;

import dagger.Component;

/**
 * Created by Dmitry Kostyrev on 08/10/15
 */
@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = MainActivityModule.class
)
public interface MainActivityComponent {

    void inject(ApplicationsFragment applicationsFragment);
}
