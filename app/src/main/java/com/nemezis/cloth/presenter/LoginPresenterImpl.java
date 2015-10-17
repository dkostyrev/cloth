package com.nemezis.cloth.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.nemezis.cloth.R;
import com.nemezis.cloth.di.component.ApplicationComponent;
import com.nemezis.cloth.manager.AuthorizationManager;
import com.nemezis.cloth.model.User;
import com.nemezis.cloth.utils.ObservableUtils;
import com.nemezis.cloth.utils.UiUtils;
import com.nemezis.cloth.view.LoginView;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */
public class LoginPresenterImpl extends BasePresenterImpl<LoginView> implements LoginPresenter {
	private Subscription subscription;
	@Inject AuthorizationManager authorizationManager;

    private boolean emailEmpty;
    private boolean passwordEmpty;

	public LoginPresenterImpl(ApplicationComponent applicationContext) {
		applicationContext.inject(this);
	}

	@Override
	public void attachView(LoginView view) {
		super.attachView(view);
        view.setSignInButtonEnabled(false);
	}

    @Override
	public void detachView() {
		super.detachView();
		if (subscription != null) {
			subscription.unsubscribe();
		}
	}

	public void signIn(@NonNull String email, @NonNull String password) {
        emailEmpty = TextUtils.isEmpty(email);
        passwordEmpty = TextUtils.isEmpty(password);
        boolean emailIsValid = !emailEmpty && UiUtils.isEmailValid(email);
        if (emailEmpty) {
            view.showEmailErrorMessage(R.string.email_is_required);
        } else if (!emailIsValid) {
            view.showEmailErrorMessage(R.string.email_is_not_valid);
        }

        if (passwordEmpty) {
            view.showPasswordErrorMessage(R.string.password_is_required);
        }

        if (!emailEmpty && !passwordEmpty && emailIsValid) {
            authorize(email, password);
        }
	}

    public void onEmailChanged(@NonNull String email) {
        emailEmpty = TextUtils.isEmpty(email);
        updateLoginButtonState();
	}

	public void onPasswordChanged(@NonNull String password) {
        passwordEmpty = TextUtils.isEmpty(password);
        updateLoginButtonState();
	}

    private void authorize(@NonNull String email, @NonNull String password) {
        view.showProgressDialog();
        this.subscription = authorizationManager.login(email, password)
                .compose(ObservableUtils.<User>schedulers())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        view.hideProgressDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable t) {
                        t.printStackTrace();
                        view.hideProgressDialog();
                        view.showErrorMessage(R.string.failed_to_authorize);
                    }
                });
    }

    private void updateLoginButtonState() {
        view.setSignInButtonEnabled(!emailEmpty && !passwordEmpty);
    }
}
