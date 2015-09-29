package com.nemezis.cloth.manager;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.nemezis.cloth.model.User;
import com.nemezis.cloth.service.FabricService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.HttpCookie;
import java.util.NoSuchElementException;

import javax.inject.Inject;

import retrofit.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */
public class AuthorizationManager {

	private static final String CSRF_TOKEN = "csrf-token";
	private static final String CONTENT = "content";

	public static class AuthorizationException extends Exception {

		public AuthorizationException(String detailMessage) {
			super(detailMessage);
		}

		public AuthorizationException(String detailMessage, Throwable throwable) {
			super(detailMessage, throwable);
		}
	}

	@Inject
	FabricService fabricService;

	public Observable<User> login(String email, String password) {
		return fabricService.getLoginPage()
				.flatMap(getAuthorizationInfo())
				.flatMap(getUserFromAuthorizationInfo(email, password));
	}

	private Func1<Response<String>, Observable<AuthorizationInfo>> getAuthorizationInfo() {
		return new Func1<Response<String>, Observable<AuthorizationInfo>>() {
			@Override
			public Observable<AuthorizationInfo> call(final Response<String> loginResponse) {
				return createObservable(loginResponse, new InputCallable<Response<String>, AuthorizationInfo, AuthorizationException>() {
					@Override
					public AuthorizationInfo call(Response<String> input) throws AuthorizationException {
						return getAuthorizationInfo(input);
					}
				});
			}
		};
	}

	private Func1<AuthorizationInfo, Observable<User>> getUserFromAuthorizationInfo(final String email, final String password) {
		return new Func1<AuthorizationInfo, Observable<User>>() {
			@Override
			public Observable<User> call(final AuthorizationInfo authorizationInfo) {
				return createObservable(authorizationInfo, new InputCallable<AuthorizationInfo, User, AuthorizationException>() {
					@Override
					public User call(AuthorizationInfo input) throws AuthorizationException {
						return getUserFromAuthorizationInfo(email, password, input);
					}
				});
			}
		};
	}

	private @NonNull AuthorizationInfo getAuthorizationInfo(Response<String> response) throws AuthorizationException {
		if (response.isSuccess()) {
			String csrfToken = null;
			HttpCookie cookie = null;

			Document document = Jsoup.parse(response.body());
			Elements elements = document.getElementsByTag("meta");
			for (Element element : elements) {
				if (element.attr("name").equals(CSRF_TOKEN)) {
					csrfToken = element.attr(CONTENT);
					break;
				}
			}

			String cookieValue = response.headers().get("Set-Cookie");
			if (TextUtils.isEmpty(cookieValue)) {
				cookie = new HttpCookie("_fabric_session", cookieValue);
			}

			if (csrfToken == null) {
				throw new AuthorizationException("Failed to get csrf token");
			}

			if (cookie == null) {
				throw new AuthorizationException("Failed to get cookie");
			}

			return new AuthorizationInfo(cookie, csrfToken);
		} else {
			throw new AuthorizationException("Failed to get authorization page");
		}
	}

	private @NonNull User getUserFromAuthorizationInfo(String email, String password, AuthorizationInfo authorizationInfo) throws AuthorizationException {
		try {
			return fabricService.getUser(email, password, authorizationInfo.cookie.getValue(), authorizationInfo.csrfToken).toBlocking().first();
		} catch (NoSuchElementException e) {
			throw new AuthorizationException("Failed to get user", e);
		}
	}

	private static <T, I> Observable<T> createObservable(final I input, final InputCallable<I, T, AuthorizationException> callable) {
		return Observable.create(new Observable.OnSubscribe<T>() {
			@Override
			public void call(Subscriber<? super T> subscriber) {
				subscriber.onStart();
				try {
					T result = callable.call(input);
					subscriber.onNext(result);
					subscriber.onCompleted();
				} catch (AuthorizationException e) {
					subscriber.onError(e);
				}
			}
		});
	}

	private interface InputCallable<I, T, E extends Exception> {
		T call(I input) throws E;
	}

	public static class AuthorizationInfo {
		public final String csrfToken;
		public final HttpCookie cookie;

		public AuthorizationInfo(HttpCookie cookie, String csrfToken) {
			this.cookie = cookie;
			this.csrfToken = csrfToken;
		}
	}
}
