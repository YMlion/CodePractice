package com.xyxg.android.unittestexample.api;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YMlion on 2018/11/23.
 */
public class NetInterceptor implements Interceptor {

    private Headers mHeaders;
    private ProgressListener mListener;

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = null;
        if (mHeaders != null) {
            builder = request.newBuilder();
            builder.headers(request.headers()
                                   .newBuilder()
                                   .addAll(mHeaders)
                                   .build());
        }

        if (mListener instanceof UploadProgressListener) {
            if (builder == null) {
                builder = request.newBuilder();
            }
            builder.method(request.method(), new NetRequestBody(request.body(), mListener));
        }
        Response response = chain.proceed(builder == null ? request : builder.build());
        if (mListener instanceof DownloadProgressListener) {
            response = response.newBuilder()
                               .body(new NetResponseBody(response.body(), mListener)
                               )
                               .build();
        }
        return response;
    }

    /**
     * @param listener upload or download listener
     */
    public NetInterceptor setListener(ProgressListener listener) {
        mListener = listener;
        return this;
    }

    /**
     * @param key   key
     * @param value value
     */
    public NetInterceptor addHeader(String key, String value) {
        Headers.Builder builder = new Headers.Builder();
        if (mHeaders != null) {
            builder = mHeaders.newBuilder();
        }
        mHeaders = builder.add(key, value)
                          .build();
        return this;
    }

    public interface DownloadProgressListener extends ProgressListener {
    }

    public interface UploadProgressListener extends ProgressListener {
    }

    interface ProgressListener {
        void onProgress(long total, long writtenByte, float progress);
    }
}
