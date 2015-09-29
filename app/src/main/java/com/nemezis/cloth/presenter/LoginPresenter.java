package com.nemezis.cloth.presenter;

import android.support.annotation.NonNull;

import com.nemezis.cloth.App;
import com.nemezis.cloth.R;
import com.nemezis.cloth.di.component.ApplicationComponent;
import com.nemezis.cloth.manager.AuthorizationManager;
import com.nemezis.cloth.model.User;
import com.nemezis.cloth.mvpview.LoginMvpView;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */
public class LoginPresenter extends Presenter<LoginMvpView> {
	private Subscription subscription;
	@Inject AuthorizationManager authorizationManager;

	public LoginPresenter(ApplicationComponent applicationContext) {
		applicationContext.inject(this);
	}

	@Override
	public void attachView(LoginMvpView view) {
		super.attachView(view);
	}

	@Override
	public void detachView() {
		super.detachView();
		if (subscription != null) {
			subscription.unsubscribe();
		}
	}

	public void signIn(@NonNull String email, @NonNull String password) {
		view.showProgressDialog();
		this.subscription = authorizationManager.login(email, password)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
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

	public void onEmailChanged(@NonNull String email) {
	}

	public void onPasswordChanged(@NonNull String email) {
	}
}
