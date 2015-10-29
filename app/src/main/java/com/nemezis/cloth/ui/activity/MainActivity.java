package com.nemezis.cloth.ui.activity;

import android.os.Bundle;

import com.nemezis.cloth.R;
import com.nemezis.cloth.di.component.ApplicationComponent;
import com.nemezis.cloth.di.component.MainActivityComponent;
import com.nemezis.cloth.di.module.MainActivityModule;
import com.nemezis.cloth.ui.applications.ApplicationsFragment;

/**
 * Created by Dmitry Kostyrev on 18/10/15
 */
public class MainActivity extends BaseActivity<MainActivityComponent> {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new ApplicationsFragment())
                .commit();
    }

    @Override
    protected MainActivityComponent setupComponent(ApplicationComponent applicationComponent) {
        return applicationComponent.plus(new MainActivityModule(applicationComponent));
    }
}
