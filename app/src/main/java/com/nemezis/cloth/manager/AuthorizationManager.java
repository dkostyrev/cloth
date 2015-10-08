package com.nemezis.cloth.manager;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.nemezis.cloth.App;
import com.nemezis.cloth.model.AuthorizationInfo;
import com.nemezis.cloth.model.User;
import com.nemezis.cloth.service.FabricService;
import com.nemezis.cloth.utils.IOUtils;
import com.nemezis.cloth.utils.ObservableUtils;
import com.squareup.okhttp.ResponseBody;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpCookie;
import java.util.NoSuchElementException;

import javax.inject.Inject;

import retrofit.Response;
import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */
public class AuthorizationManager {

	private static final String LOG_TAG = "AuthorizationManager";
	private static final String CSRF_TOKEN = "csrf-token";
	private static final String CONTENT = "content";
    private static final String AUTHORIZATION_INFO_FILE = "authorizationInfo";

	public static class AuthorizationException extends RuntimeException {

		public AuthorizationException(String detailMessage) {
			super(detailMessage);
		}

		public AuthorizationException(String detailMessage, Throwable throwable) {
			super(detailMessage, throwable);
		}
	}

	@Inject
	FabricService fabricService;

    private File authorizationInfoFile;
    private volatile AuthorizationInfo authorizationInfo;

	public AuthorizationManager(App applicationContext) {
		applicationContext.getApplicationComponent().inject(this);
        authorizationInfoFile = new File(applicationContext.getFilesDir(), AUTHORIZATION_INFO_FILE);
	}

    public Observable<AuthorizationInfo> getAuthorizationInfo() {
        if (authorizationInfo != null) {
            return Observable.just(authorizationInfo);
        } else {
            return ObservableUtils.createSimpleObservable(new LoadAuthorizationInfo());
        }
    }

	public Observable<User> login(String email, String password) {
		return fabricService.getLoginPage()
				.map(new AuthorizationInfoFromResponse())
				.map(new UserFromAuthorizationInfo(email, password));
	}

    @WorkerThread
    private class AuthorizationInfoFromResponse implements Func1<Response<ResponseBody>, AuthorizationInfo> {

        @Override
        public AuthorizationInfo call(Response<ResponseBody> responseBodyResponse) {
            return getAuthorizationInfo(responseBodyResponse);
        }

        private @NonNull
        AuthorizationInfo getAuthorizationInfo(Response<ResponseBody> response) {
            if (response.isSuccess()) {
                String csrfToken = null;
                HttpCookie cookie = null;

                Document document;
                try {
                    document = Jsoup.parse(response.body().string());
                } catch (IOException e) {
                    throw new AuthorizationException("Failed to parse login page", e);
                }

                Elements elements = document.getElementsByTag("meta");
                for (Element element : elements) {
                    if (element.attr("name").equals(CSRF_TOKEN)) {
                        csrfToken = element.attr(CONTENT);
                        break;
                    }
                }

                String cookieValue = response.headers().get("Set-Cookie");
                if (!TextUtils.isEmpty(cookieValue)) {
                    cookie = new HttpCookie("_fabric_session", cookieValue);
                }

                if (csrfToken == null) {
                    throw new AuthorizationException("Failed to get csrf token");
                }

                if (cookie == null) {
                    throw new AuthorizationException("Failed to get cookie");
                }
                Log.i(LOG_TAG, "Got CRSF token " + csrfToken);
                Log.i(LOG_TAG, "Got Cookie " + cookie.getValue());
                return new AuthorizationInfo(cookie, csrfToken);
            } else {
                throw new AuthorizationException("Failed to get authorization page");
            }
        }
    }

    @WorkerThread
    private class UserFromAuthorizationInfo implements Func1<AuthorizationInfo, User> {

        private String email;
        private String password;

        public UserFromAuthorizationInfo(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        public User call(AuthorizationInfo authorizationInfo) {
            User user = getUserFromAuthorizationInfo(authorizationInfo);
            persistAuthorizationInfo(authorizationInfo);
            return user;
        }

        private @NonNull
        User getUserFromAuthorizationInfo(AuthorizationInfo authorizationInfo) throws AuthorizationException {
            try {
                return fabricService.getUser(email, password, authorizationInfo.cookie.getValue(), authorizationInfo.csrfToken);
            } catch (NoSuchElementException e) {
                throw new AuthorizationException("Failed to get user", e);
            }
        }
    }

    @WorkerThread
    private void persistAuthorizationInfo(AuthorizationInfo authorizationInfo) {
        this.authorizationInfo = authorizationInfo;
        if (authorizationInfoFile.exists() && !authorizationInfoFile.delete()) {
            throw new RuntimeException("Failed to delete existing authorization info");
        }
        Writer writer = null;
        try {
            String json = new Gson().toJson(authorizationInfo);
            writer = new BufferedWriter(new FileWriter(authorizationInfoFile));
            writer.write(json);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save authorization info", e);
        } finally {
            if (writer != null) {
                IOUtils.closeSilently(writer);
            }
        }
    }

    @WorkerThread
    private class LoadAuthorizationInfo implements Func0<AuthorizationInfo> {

        @Override
        public AuthorizationInfo call() {
            if (authorizationInfoFile.exists()) {
                Reader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(authorizationInfoFile));
                    return new Gson().fromJson(reader, AuthorizationInfo.class);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("Failed to load authorization info", e);
                } finally {
                    if (reader != null) {
                        IOUtils.closeSilently(reader);
                    }
                }
            }
            return null;
        }
    }

}
