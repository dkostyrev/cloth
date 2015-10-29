package com.nemezis.cloth.manager;

import com.nemezis.cloth.App;
import com.nemezis.cloth.model.Application;
import com.nemezis.cloth.service.FabricService;
import com.nemezis.cloth.tables.ApplicationTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.squareup.okhttp.internal.Util;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Dmitry Kostyrev on 18/10/15
 */
public class ApplicationsManager {

    @Inject
    FabricService fabricService;
    @Inject
    DatabaseManager databaseManager;

    public ApplicationsManager(App applicationContext) {
        applicationContext.getApplicationComponent().inject(this);
    }

    public Observable<List<Application>> getApplications() {
        if (databaseManager.getStorIOSQLite() == null) {
            throw new IllegalStateException("Database should be open");
        }

        final Observable<List<Application>> databaseObservable = databaseManager.getStorIOSQLite()
                .get()
                .listOfObjects(Application.class)
                .withQuery(Query.builder().table(ApplicationTable.TABLE).build())
                .prepare()
                .createObservable();

        Observable<List<Application>> networkObservable = fabricService.getApps(true)
                .doOnNext(new Action1<List<Application>>() {
                    @Override public void call(List<Application> applications) {
                        databaseManager.getStorIOSQLite().put().objects(applications).prepare().executeAsBlocking();
                    }
                });
        return Observable.merge(databaseObservable, networkObservable);
    }
}