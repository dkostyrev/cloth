package com.nemezis.cloth.di.module;

import com.nemezis.cloth.App;
import com.nemezis.cloth.di.PerApplication;
import com.nemezis.cloth.network.SessionCookieHandler;
import com.nemezis.cloth.service.FabricService;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */
@Module(includes = AppModule.class)
public class ApiModule {

    @Provides
    @PerApplication
    SessionCookieHandler provideSessionCookieHandler(App applicationContext) {
        return new SessionCookieHandler(applicationContext);
    }

    @Provides
    @PerApplication
	OkHttpClient provideOkHttpClient(SessionCookieHandler sessionCookieHandler) {
		OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setCookieHandler(sessionCookieHandler);
		return okHttpClient;
	}

	@Provides
    @PerApplication
	Retrofit provideRetrofit(OkHttpClient okHttpClient) {
		return new Retrofit.Builder()
				.baseUrl(FabricService.BASE_URL)
				.client(okHttpClient)
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.build();
	}

	@Provides
    @PerApplication
	FabricService provideFabricService(Retrofit retrofit) {
		return retrofit.create(FabricService.class);
	}
}