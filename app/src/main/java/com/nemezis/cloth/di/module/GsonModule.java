package com.nemezis.cloth.di.module;

import com.google.gson.Gson;
import com.nemezis.cloth.di.PerApplication;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Dmitry Kostyrev on 08/10/15
 */
@Module
public class GsonModule {

    @Provides
    @PerApplication
    public Gson provideGson() {
        return new Gson();
    }
}
