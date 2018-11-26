package com.xyxg.android.unittestexample.api;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Created by YMlion on 2018/11/22.
 */
public class Http {

    public static <T> ObservableTransformer<T, T> handleResult() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof HttpException) {
                        ResponseBody body = ((HttpException) throwable).response().errorBody();
                        if (body != null) {
                            return Observable.error(new HttpThrowable(
                                    throwable.getMessage() + " " + body.string()));
                        }
                    }
                    return Observable.error(throwable);
                });
    }
}
