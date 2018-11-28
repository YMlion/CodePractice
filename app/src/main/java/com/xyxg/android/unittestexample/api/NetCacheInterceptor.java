package com.xyxg.android.unittestexample.api;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YMlion on 2018/11/28.
 */
public class NetCacheInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        CacheControl cacheControl = request.cacheControl();
        Log.d("TAG", "intercept: " + request.cacheControl().toString());
        Response response = chain.proceed(request);
        Log.d("TAG", "intercept: " + response.cacheControl().toString());
        if (!cacheControl.toString().isEmpty()) {
            response = response
                    .newBuilder()
                    .header("Cache-Control", cacheControl.toString())
                    .removeHeader("Pragma")
                    .build();
        }
        return response;
    }
}
