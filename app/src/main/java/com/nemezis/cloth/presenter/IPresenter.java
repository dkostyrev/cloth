package com.nemezis.cloth.presenter;

import com.nemezis.cloth.view.IView;

/**
 * Created by Dmitry Kostyrev on 12/10/15
 */
public interface IPresenter<T extends IView> {

    void attachView(T view);

    void detachView();
}
