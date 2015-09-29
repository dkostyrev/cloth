package com.nemezis.cloth.presenter;

import com.nemezis.cloth.mvpview.MvpView;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */
public class Presenter<T extends MvpView> {

	protected T view;

	public void attachView(T view) {
		this.view = view;
	}

	public void detachView() {
		this.view = null;
	}
}
