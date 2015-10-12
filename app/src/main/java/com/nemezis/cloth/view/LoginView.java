package com.nemezis.cloth.view;

import android.support.annotation.StringRes;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */
public interface LoginView extends IView {

	void showProgressDialog();
	void hideProgressDialog();
	void showErrorMessage(@StringRes int errorMessage);
	void setSignInButtonEnabled(boolean enabled);
    void showEmailErrorMessage(@StringRes int errorMessage);
    void showPasswordErrorMessage(@StringRes int errorMessage);
}
