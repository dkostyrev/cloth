package com.nemezis.cloth.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.nemezis.cloth.R;
import com.nemezis.cloth.mvpview.LoginMvpView;
import com.nemezis.cloth.presenter.LoginPresenter;
import com.nemezis.cloth.ui.activity.BaseActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by Dmitry Kostyrev on 27/09/15
 */
public class LoginActivity extends BaseActivity implements LoginMvpView {

	@Bind(R.id.emailTextInputLayout)
	TextInputLayout emailTextInputLayout;
	@Bind(R.id.emailEditText)
	EditText emailEditText;
	@Bind(R.id.passwordTextInputLayout)
	TextInputLayout passwordTextInputLayout;
	@Bind(R.id.passwordEditText)
	EditText passwordEditText;
	@Bind(R.id.signInButton)
	View signInButton;

	@Inject LoginPresenter loginPresenter;

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		ButterKnife.bind(this);
		getApp().getApplicationComponent().inject(this);
		loginPresenter.attachView(this);
	}

	@Override
	protected void onDestroy() {
		ButterKnife.unbind(this);
		loginPresenter.detachView();
		super.onDestroy();
	}

	@OnClick(R.id.signInButton)
	void onSignInButtonClicked() {
		loginPresenter.signIn(emailEditText.getText().toString(), passwordEditText.getText().toString());
	}

	@OnTextChanged(R.id.passwordEditText)
	void onPasswordTextChanged(CharSequence s, int start, int before, int count) {
		loginPresenter.onEmailChanged(s.toString());
	}

	@OnTextChanged(R.id.emailEditText)
	void onEmailTextChanged(CharSequence s, int start, int before, int count) {
		loginPresenter.onPasswordChanged(s.toString());
	}

	@Override
	public void showProgressDialog() {
		hideProgressDialog();
		progressDialog = ProgressDialog.show(this, getString(R.string.loading), getString(R.string.please_wait));
	}

	@Override
	public void hideProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	@Override
	public void showErrorMessage(@StringRes int errorMessage) {
		new AlertDialog.Builder(this)
				.setTitle(R.string.error)
				.setMessage(errorMessage)
				.setPositiveButton(R.string.ok, null)
				.show();
	}

	@Override
	public void setSignInButtonEnabled(boolean enabled) {
		signInButton.setEnabled(enabled);
	}

	@Override
	public Context getContext() {
		return this;
	}
}
