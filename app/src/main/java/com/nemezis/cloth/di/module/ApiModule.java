package com.nemezis.cloth.di.module;

import com.nemezis.cloth.service.FabricService;
import com.squareup.okhttp.OkHttpClient;

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
@Module
public class ApiModule {

	@Provides
	@Singleton
	OkHttpClient provideOkHttpClient() {
		OkHttpClient okHttpClient = new OkHttpClient();
		okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
		okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
		return okHttpClient;
	}

	@Provides
	@Singleton
	Retrofit provideRetrofit(OkHttpClient okHttpClient) {
		return new Retrofit.Builder()
				.baseUrl(FabricService.BASE_URL)
				.client(okHttpClient)
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.build();
	}

	@Provides
	@Singleton
	FabricService provideFabricService(Retrofit retrofit) {
		return retrofit.create(FabricService.class);
	}
}