package com.nemezis.cloth.presenter;

import com.nemezis.cloth.manager.ApplicationsManager;
import com.nemezis.cloth.model.Application;
import com.nemezis.cloth.utils.ObservableUtils;
import com.nemezis.cloth.view.ApplicationsView;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Dmitry Kostyrev on 08/10/15
 */
public class ApplicationsPresenterImpl extends BasePresenterImpl<ApplicationsView> implements ApplicationsPresenter {

    private ApplicationsManager applicationsManager;

    public ApplicationsPresenterImpl(ApplicationsManager applicationsManager) {
        this.applicationsManager = applicationsManager;
    }

    @Override
    public void attachView(ApplicationsView view) {
        super.attachView(view);
        refreshApplications();
    }

    @Override public void refreshApplications() {
        applicationsManager.getApplications()
                .compose(ObservableUtils.<List<Application>>schedulers())
                .subscribe(new Action1<List<Application>>() {
                    @Override public void call(List<Application> applications) {
                        if (view != null) {
                            view.displayApplications(applications);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }
}
