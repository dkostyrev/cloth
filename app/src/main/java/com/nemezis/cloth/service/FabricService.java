package com.nemezis.cloth.service;

import com.nemezis.cloth.model.User;
import com.squareup.okhttp.ResponseBody;

import retrofit.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */
public interface FabricService {

	String BASE_URL = "https://fabric.io";

	@GET("login")
	Observable<Response<ResponseBody>> getLoginPage();

	@FormUrlEncoded
	@POST("api/v2/session")
	Observable<User> getUser(@Field("email") String email, @Field("password") String password, @Header("Cookie") String cookie, @Header("X-CSRF-Token") String csrfToken);
}
