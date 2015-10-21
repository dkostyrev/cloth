package com.nemezis.cloth.manager;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.nemezis.cloth.App;
import com.nemezis.cloth.model.User;
import com.nemezis.cloth.network.SessionCookieHandler;
import com.nemezis.cloth.service.FabricService;
import com.squareup.okhttp.ResponseBody;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import javax.inject.Inject;

import retrofit.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */
public class AuthorizationManager {

    private static final String LOG_TAG = "AuthorizationManager";
    private static final String CSRF_TOKEN = "csrf-token";
    private static final String CONTENT = "content";

    public static class AuthorizationException extends RuntimeException {

        public AuthorizationException(String detailMessage) {
            super(detailMessage);
        }

        public AuthorizationException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }
    }

    @Inject FabricService fabricService;
    @Inject SessionCookieHandler sessionCookieHandler;
    @Inject DatabaseManager databaseManager;
    @Inject UserManager userManager;

    public AuthorizationManager(App context) {
        context.getApplicationComponent().inject(this);
    }

    public Observable<User> login(String email, String password) {
        userManager.logOut();
        sessionCookieHandler.eraseCookie();
        return fabricService.getLoginPage()
                .map(new AuthorizationInfoFromResponse())
                .map(new UserFromAuthorizationInfo(email, password))
                .doOnNext(new Action1<User>() {
                    @Override public void call(User user) {
                        userManager.logIn(user);
                    }
                });
    }

    @WorkerThread
    private class AuthorizationInfoFromResponse implements Func1<Response<ResponseBody>, AuthorizationInfo> {

        @Override
        public AuthorizationInfo call(Response<ResponseBody> responseBodyResponse) {
            return getAuthorizationInfo(responseBodyResponse);
        }

        private @NonNull AuthorizationInfo getAuthorizationInfo(Response<ResponseBody> response) {
            if (response.isSuccess()) {
                String csrfToken = null;

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
                if (csrfToken == null) {
                    throw new AuthorizationException("Failed to get csrf token");
                }
                Log.i(LOG_TAG, "Got CRSF token " + csrfToken);
                return new AuthorizationInfo(csrfToken);
            } else {
                throw new AuthorizationException("Failed to get login page");
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
            return getUserFromAuthorizationInfo(authorizationInfo);
        }

        private @NonNull User getUserFromAuthorizationInfo(AuthorizationInfo authorizationInfo) throws AuthorizationException {
            Observable<User> observable = fabricService.getUser(email, password, authorizationInfo.csrfToken);
            User user = observable.toBlocking().first();
            if (user != null) {
                return user;
            } else {
                throw new AuthorizationException("Failed to get user");
            }
        }
    }

    private static class AuthorizationInfo {
        public final String csrfToken;

        public AuthorizationInfo(String csrfToken) {
            this.csrfToken = csrfToken;
        }
    }


}
