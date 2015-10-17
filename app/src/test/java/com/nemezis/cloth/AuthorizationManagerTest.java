package com.nemezis.cloth;

import com.nemezis.cloth.di.component.ApplicationComponent;
import com.nemezis.cloth.di.component.DaggerApplicationComponent;
import com.nemezis.cloth.di.module.ApiModule;
import com.nemezis.cloth.di.module.AppModule;
import com.nemezis.cloth.manager.AuthorizationManager;
import com.nemezis.cloth.model.User;
import com.nemezis.cloth.network.SessionCookieHandler;
import com.nemezis.cloth.service.FabricService;
import com.nemezis.cloth.utils.ObservableUtils;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import okio.BufferedSource;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Observable;
import rx.functions.Action1;
import rx.plugins.RxJavaSchedulersHookResetRule;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Dmitry Kostyrev on 12/10/15
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class, manifest = Config.NONE)
public class AuthorizationManagerTest {

    private static final String HTML_WITH_TOKEN = "<html>" +
            "<meta name=\"csrf-token\" content=\"some_token\"</meta>" +
            "</html>";

    private static final String EMAIL = "correct@email.com";
    private static final String PASSWORD = "some-password";

    @Rule public RxJavaSchedulersHookResetRule rxJavaSchedulersHookResetRule = new RxJavaSchedulersHookResetRule();
    AuthorizationManager authorizationManager;
    FabricService fabricService;
    Action1<User> onNext;
    Action1<Throwable> onError;
    SessionCookieHandler sessionCookieHandler;


    @SuppressWarnings("unchecked")
    @Before public void setUp() {
        App app = mock(App.class);
        onNext = mock(Action1.class);
        onError = mock(Action1.class);
        sessionCookieHandler = mock(SessionCookieHandler.class);

        ApplicationComponent applicationComponent = DaggerApplicationComponent.builder()
                .appModule(new AppModule(app))
                .apiModule(new TestApiModule())
                .build();

        when(app.getApplicationComponent()).thenReturn(applicationComponent);

        fabricService = mock(FabricService.class);

        authorizationManager = new AuthorizationManager(app);
    }

    @Test public void testFailureResponse() throws Exception {
        ResponseBody responseBody = ResponseBody.create(MediaType.parse("plain/text"), "Bad response");
        Response<ResponseBody> response = Response.error(500, responseBody);
        when(fabricService.getLoginPage()).thenReturn(Observable.just(response));
        login();

        verify(onNext, never()).call(any(User.class));
        assertOnErrorWasCalledWithMessageContaining("login page");
    }

    @Test public void testFailedToParseLoginPage() throws Exception {
        BufferedSource bufferedSource = mock(BufferedSource.class);
        when(bufferedSource.readByteArray()).thenThrow(new IOException());
        ResponseBody responseBody = ResponseBody.create(MediaType.parse("plain/text"), -1, bufferedSource);
        Response <ResponseBody> response = Response.success(responseBody);
        when(fabricService.getLoginPage()).thenReturn(Observable.just(response));
        login();

        verify(onNext, never()).call(any(User.class));
        assertOnErrorWasCalledWithMessageContaining("parse login page");
    }

    @Test public void testNoCsrfTokenAuthorization() throws Exception {
        String html = "<html></html>";
        mockResponse(html);
        login();

        verify(onNext, never()).call(any(User.class));
        assertOnErrorWasCalledWithMessageContaining("csrf");
    }

    @Test public void testHasCsrfTokenNoUser() throws Exception {
        mockResponse(HTML_WITH_TOKEN);
        Observable<User> userObservable = Observable.just(null);
        when(fabricService.getUser(anyString(), anyString(), anyString())).thenReturn(userObservable);
        login();

        verify(onNext, never()).call(any(User.class));
        assertOnErrorWasCalledWithMessageContaining("user");
    }

    @Test public void testSignInNoUser() throws Exception {
        mockResponse(HTML_WITH_TOKEN);
        Observable<User> userObservable = Observable.just(null);
        when(fabricService.getUser(anyString(), anyString(), anyString())).thenReturn(userObservable);
        login();

        verify(onNext, never()).call(any(User.class));
        assertOnErrorWasCalledWithMessageContaining("user");
    }

    @Test public void testCorrectSignIn() throws Exception {
        mockResponse(HTML_WITH_TOKEN);
        Observable<User> userObservable = Observable.just(new User());
        when(fabricService.getUser(anyString(), anyString(), anyString())).thenReturn(userObservable);
        login();

        verify(onNext).call(any(User.class));
        verify(onNext).call(notNull(User.class));
        verify(onError, never()).call(any(Throwable.class));
    }

    private void login() {
        authorizationManager.login(EMAIL, PASSWORD)
                .compose(ObservableUtils.<User>schedulers())
                .subscribe(onNext, onError);

        verify(sessionCookieHandler).eraseCookie();
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void assertOnErrorWasCalledWithMessageContaining(String substring) {
        ArgumentCaptor<Throwable> argumentCaptor = ArgumentCaptor.forClass(Throwable.class);
        verify(onError).call(argumentCaptor.capture());
        assertNotNull(argumentCaptor.getValue());
        assertNotNull(argumentCaptor.getValue().getMessage());
        assertEquals(true, argumentCaptor.getValue().getMessage().contains(substring));
    }

    private void mockResponse(String html) {
        ResponseBody responseBody = ResponseBody.create(MediaType.parse("text/html; charset=utf-8"), html);
        Response<ResponseBody> response = Response.success(responseBody);
        when(fabricService.getLoginPage()).thenReturn(Observable.just(response));
    }

    private class TestApiModule extends ApiModule {

        @Override
        public FabricService provideFabricService(Retrofit retrofit) {
            return fabricService;
        }

        @Override
        public SessionCookieHandler provideSessionCookieHandler(App applicationContext) {
            return sessionCookieHandler;
        }
    }
}
