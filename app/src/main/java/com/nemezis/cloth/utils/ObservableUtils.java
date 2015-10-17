package com.nemezis.cloth.utils;

import com.nemezis.cloth.R;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by Dmitry Kostyrev on 07/10/15
 */
public class ObservableUtils {

    public static <T> Observable.Transformer<T, T> schedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> Observable<T> createSimpleObservable(final Func0<T> func) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onStart();
                try {
                    subscriber.onNext(func.call());
                    subscriber.onCompleted();
                } catch (Throwable t) {
                    subscriber.onError(t);
                }
            }
        });
    }

    ;
}
