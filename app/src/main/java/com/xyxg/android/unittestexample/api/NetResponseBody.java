package com.xyxg.android.unittestexample.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by YMlion on 2018/11/23.
 */
public class NetResponseBody extends ResponseBody {

    private ResponseBody mProxy;
    private NetInterceptor.ProgressListener mListener;

    NetResponseBody(ResponseBody body, NetInterceptor.ProgressListener listener) {
        mProxy = body;
        mListener = listener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mProxy.contentType();
    }

    @Override
    public long contentLength() {
        return mProxy.contentLength();
    }

    @NonNull
    @Override
    public BufferedSource source() {
        return Okio.buffer(source(mProxy.source()));
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            private long byteRead;

            @Override
            public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                long count = super.read(sink, byteCount);
                byteRead += count;
                long total = contentLength();
                if (mListener != null && total > 0) {
                    mListener.onProgress(total, byteRead, 100F * byteRead / total);
                }

                return count;
            }
        };
    }
}
