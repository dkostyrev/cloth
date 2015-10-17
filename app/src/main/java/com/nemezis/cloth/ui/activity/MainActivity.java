package com.nemezis.cloth.ui.activity;

import com.nemezis.cloth.di.component.ApplicationComponent;
import com.nemezis.cloth.di.component.DaggerMainActivityComponent;
import com.nemezis.cloth.di.component.MainActivityComponent;
import com.nemezis.cloth.di.module.MainActivityModule;

/**
 * Created by Dmitry Kostyrev on 18/10/15
 */
public class MainActivity extends BaseActivity<MainActivityComponent> {

    @Override
    protected MainActivityComponent setupComponent(ApplicationComponent applicationComponent) {
        return DaggerMainActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .mainActivityModule(new MainActivityModule(applicationComponent))
                .build();
    }
}
