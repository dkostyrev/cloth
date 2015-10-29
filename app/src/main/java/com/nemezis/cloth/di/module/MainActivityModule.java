package com.nemezis.cloth.di.module;

import com.nemezis.cloth.di.PerActivity;
import com.nemezis.cloth.di.component.ApplicationComponent;
import com.nemezis.cloth.presenter.ApplicationsPresenter;
import com.nemezis.cloth.presenter.ApplicationsPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Dmitry Kostyrev on 08/10/15
 */
@Module
public class MainActivityModule {

    private ApplicationComponent applicationComponent;
    public MainActivityModule(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    @Provides
    @PerActivity
    ApplicationsPresenter provideApplicationsPresenter() {
        return new ApplicationsPresenterImpl(applicationComponent);
    }
}
