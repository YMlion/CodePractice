package com.xyxg.android.unittestexample.util;

import android.util.Log;

import org.reactivestreams.Subscription;

import io.reactivex.FlowableSubscriber;

/**
 * Created by YMlion on 2018/11/2.
 */
public class SubscriberWrap<T> implements FlowableSubscriber<T> {
    private static final String TAG = "SubscriberWrap";

    @Override
    public void onSubscribe(Subscription s) {
        Log.d(TAG, "onSubscribe.");
        // 执行过这个方法之后，会立即调用onNext，所以在该方法之后的代码会在onComplete之后执行。
        // 正常情况下，该方法应该不能使用最大值，这样就默认和Observable类似了。
        s.request(Integer.MAX_VALUE);
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
