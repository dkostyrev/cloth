package com.nemezis.cloth.utils;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;

/**
 * Created by Dmitry Kostyrev on 07/10/15
 */
public class ObservableUtils {

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
    };
}
