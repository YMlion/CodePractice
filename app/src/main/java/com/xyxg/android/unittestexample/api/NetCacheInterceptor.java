package com.xyxg.android.unittestexample.api;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

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
        String cacheControl = request.cacheControl().toString();
        Log.d("TAG", "intercept: " + cacheControl);
        Response response = chain.proceed(request);
        Log.d("TAG", "intercept: " + response.cacheControl().toString());
        if (cacheControl.isEmpty()) {
            if (!response.cacheControl().noStore() && (response.cacheControl().noCache()
                    || response.cacheControl().maxAgeSeconds() <= 0)) {
                response = response
                        .newBuilder()
                        .header("Cache-Control", cacheControl + ", no-store")
                        .build();
            }
        } else {
            response = response
                    .newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        }
        return response;
    }
}
