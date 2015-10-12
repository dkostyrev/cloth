package com.nemezis.cloth.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nemezis.cloth.R;
import com.nemezis.cloth.di.component.LoginActivityComponent;
import com.nemezis.cloth.presenter.LoginPresenter;
import com.nemezis.cloth.presenter.LoginPresenterImpl;
import com.nemezis.cloth.view.LoginView;
import com.nemezis.cloth.ui.fragment.BaseFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */
public class LoginFragment extends BaseFragment<LoginActivityComponent> implements LoginView {

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

    @Inject
    LoginPresenter loginPresenter;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        getActivityComponent().inject(this);
        loginPresenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        loginPresenter.detachView();
        ButterKnife.unbind(this);
        super.onDestroyView();
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
        progressDialog = ProgressDialog.show(getActivity(), getString(R.string.loading), getString(R.string.please_wait));
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showErrorMessage(@StringRes int errorMessage) {
        new AlertDialog.Builder(getActivity())
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
    public void showEmailErrorMessage(@StringRes int errorMessage) {
        emailTextInputLayout.setError(getString(errorMessage));
    }

    @Override
    public void showPasswordErrorMessage(@StringRes int errorMessage) {
        passwordTextInputLayout.setError(getString(errorMessage));
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
