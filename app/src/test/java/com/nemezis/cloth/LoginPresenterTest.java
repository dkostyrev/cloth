package com.nemezis.cloth;

import com.nemezis.cloth.di.component.ApplicationComponent;
import com.nemezis.cloth.di.component.DaggerApplicationComponent;
import com.nemezis.cloth.di.module.AppModule;
import com.nemezis.cloth.di.module.ManagerModule;
import com.nemezis.cloth.manager.AuthorizationManager;
import com.nemezis.cloth.model.User;
import com.nemezis.cloth.network.SessionCookieHandler;
import com.nemezis.cloth.presenter.LoginPresenter;
import com.nemezis.cloth.presenter.LoginPresenterImpl;
import com.nemezis.cloth.view.LoginView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.plugins.RxJavaSchedulersHookResetRule;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class, manifest = Config.NONE)
public class LoginPresenterTest {

    @Rule public RxJavaSchedulersHookResetRule rxJavaSchedulersHookResetRule = new RxJavaSchedulersHookResetRule();

    LoginPresenter loginPresenter;
    AuthorizationManager authorizationManager;
    LoginView loginView;
    SessionCookieHandler sessionCookieHandler;

    @Before  public void setUp() throws IOException {
        sessionCookieHandler = mock(SessionCookieHandler.class);
        ApplicationComponent testApplicationComponent = DaggerApplicationComponent.builder()
                .appModule(new AppModule(mock(App.class)))
                .managerModule(new TestManagerModule())
                .build();

        loginView = mock(LoginView.class);
        authorizationManager = mock(AuthorizationManager.class);
        loginPresenter = new LoginPresenterImpl(testApplicationComponent);
        loginPresenter.attachView(loginView);
    }

    @Test public void testEmptyEmail() {
        loginPresenter.signIn("", "not_empty_password");
        verify(loginView).showEmailErrorMessage(R.string.email_is_required);
        verify(authorizationManager, never()).login(anyString(), anyString());
    }

    @Test public void testEmptyPassword() {
        loginPresenter.signIn("some@email.com", "");
        verify(loginView).showPasswordErrorMessage(R.string.password_is_required);
        verify(authorizationManager, never()).login(anyString(), anyString());
    }

    @Test public void testEmptyEmailAndPassword() {
        loginPresenter.signIn("", "");
        verify(loginView).showEmailErrorMessage(R.string.email_is_required);
        verify(loginView).showPasswordErrorMessage(R.string.password_is_required);
        verify(authorizationManager, never()).login(anyString(), anyString());
    }

    @Test public void testEmailNotValid() {
        loginPresenter.signIn("invalid.email.com", "not_empty_password");
        verify(loginView).showEmailErrorMessage(R.string.email_is_not_valid);
        verify(authorizationManager, never()).login(anyString(), anyString());
    }

    @Test public void testSignInButtonState() {
        verify(loginView).setSignInButtonEnabled(eq(false));
        loginPresenter.onEmailChanged("e");
        verify(loginView, atMost(2)).setSignInButtonEnabled(eq(false));
        loginPresenter.onPasswordChanged("p");
        verify(loginView, atMost(3)).setSignInButtonEnabled(eq(true));
        loginPresenter.onEmailChanged("");
        verify(loginView, atMost(4)).setSignInButtonEnabled(eq(false));
        loginPresenter.onEmailChanged("a");
        loginPresenter.onPasswordChanged("");
        verify(loginView, atMost(5)).setSignInButtonEnabled(eq(false));
    }

    @Test public void testCorrectAuthorization() {
        User mockUser = mock(User.class);
        when(authorizationManager.login(anyString(), anyString())).thenReturn(Observable.just(mockUser));

        loginPresenter.signIn("correct@email.com", "not_empty_password");
        verify(loginView).showProgressDialog();
        verify(loginView).hideProgressDialog();
    }

    @Test public void testIncorrectAuthorization() {
        when(authorizationManager.login(anyString(), anyString())).thenReturn(Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> subscriber) {
                subscriber.onStart();
                subscriber.onError(new Exception("Mock exception"));
            }
        }));

        loginPresenter.signIn("not_correct@email.com", "not_empty_password");
        verify(loginView).showProgressDialog();
        verify(loginView).hideProgressDialog();
        verify(loginView).showErrorMessage(eq(R.string.failed_to_authorize));
    }

    @After public void tearDown() {
        loginPresenter.detachView();
    }

        private class TestManagerModule extends ManagerModule {
        @Override
        public AuthorizationManager provideAuthorizationManager(App applicationContext) {
            return authorizationManager;
        }
    }

}