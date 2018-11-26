package com.xyxg.android.unittestexample.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by YMlion on 2018/11/23.
 */
public class NetRequestBody extends RequestBody {

    private NetInterceptor.ProgressListener mListener;
    private RequestBody mProxy;

    NetRequestBody(RequestBody proxy, NetInterceptor.ProgressListener listener) {
        mProxy = proxy;
        mListener = listener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mProxy.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mProxy.contentLength();
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        ProgressSink progressSink = new ProgressSink(sink);
        BufferedSink bufferedSink = Okio.buffer(progressSink);
        mProxy.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private class ProgressSink extends ForwardingSink {

        private long byteWritten;

        ProgressSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(@NonNull Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            if (byteCount <= 0) {
                return;
            }
            byteWritten += byteCount;
            long total = contentLength();
            if (mListener != null && total > 0 && byteWritten <= total) {
                float p = 100F * byteWritten / total;
                if (byteWritten == total) {
                    p = 100;
                }
                mListener.onProgress(total, byteWritten, p);
            }
        }
    }
}
