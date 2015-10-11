package com.nemezis.cloth.presenter;

import android.support.annotation.NonNull;

import com.nemezis.cloth.view.LoginView;

/**
 * Created by Dmitry Kostyrev on 12/10/15
 */
public interface LoginPresenter extends IPresenter<LoginView> {

    void signIn(@NonNull String email, @NonNull String password);

    void onEmailChanged(@NonNull String email);

    void onPasswordChanged(@NonNull String email);
}
