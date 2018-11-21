package com.xyxg.android.unittestexample.util;

import android.util.Log;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by YMlion on 2018/11/5.
 */
public class ObserverWrap<T> implements Observer<T> {
    private static final String TAG = "ObserverWrap";

    @Override
    public void onSubscribe(Disposable d) {
        Log.d(TAG, "onSubscribe.");
    }

    @Override
    public void onNext(T t) {
        Log.d(TAG, "onNext: " + t);
    }

    @Override
    public void onError(Throwable t) {
        Log.d(TAG, "onError: " + t.getMessage());
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete.");
    }
}
