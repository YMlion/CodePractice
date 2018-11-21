package com.xyxg.android.unittestexample.util;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscription;

/**
 * Created by YMlion on 2018/11/2.
 */
public class RxUtil {

    public static <T> FlowableTransformer<T, T> applySchedulerF() {
        return flowable -> flowable.subscribeOn(Schedulers.io())
                                   .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<T, T> applySchedulerO() {
        return observable -> observable.subscribeOn(Schedulers.io())
                                       .observeOn(AndroidSchedulers.mainThread());
    }

    public static void asyncDo(Runnable r) {
        Schedulers.io().createWorker().schedule(r);
    }
}
