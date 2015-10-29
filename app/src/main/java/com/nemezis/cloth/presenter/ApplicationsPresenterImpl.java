package com.nemezis.cloth.presenter;

import com.nemezis.cloth.di.component.ApplicationComponent;
import com.nemezis.cloth.view.ApplicationsView;

/**
 * Created by Dmitry Kostyrev on 08/10/15
 */
public class ApplicationsPresenterImpl extends BasePresenterImpl<ApplicationsView> implements ApplicationsPresenter {

    public ApplicationsPresenterImpl() {
    }

    @Override
    public void attachView(ApplicationsView view) {
        super.attachView(view);

    }

    @Override public void refreshApplications() {

    }
}
