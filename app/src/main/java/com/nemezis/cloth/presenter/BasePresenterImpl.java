package com.nemezis.cloth.presenter;

import com.nemezis.cloth.view.IView;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */
abstract class BasePresenterImpl<T extends IView> implements IPresenter<T> {

	protected T view;

	public void attachView(T view) {
		this.view = view;
	}

	public void detachView() {
		this.view = null;
	}
}
