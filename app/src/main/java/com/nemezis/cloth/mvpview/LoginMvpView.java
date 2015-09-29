package com.nemezis.cloth.mvpview;

import android.support.annotation.StringRes;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */
public interface LoginMvpView extends MvpView {

	void showProgressDialog();
	void hideProgressDialog();
	void showErrorMessage(@StringRes int errorMessage);
	void setSignInButtonEnabled(boolean enabled);

}
