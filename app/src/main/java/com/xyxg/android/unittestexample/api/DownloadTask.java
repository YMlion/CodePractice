package com.xyxg.android.unittestexample.api;

import com.xyxg.android.unittestexample.AppContext;
import com.xyxg.android.unittestexample.util.FileUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by YMlion on 2018/11/20.
 */
public class DownloadTask {
    private static final String BASE_URL = "http://10.160.2.126:56789/api/";
    private static IRequest request;
    private static DownloadTask downloadTask;

    private DownloadTask(NetInterceptor.ProgressListener listener) {
        if (request == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .addNetworkInterceptor(new NetInterceptor()
                            .addHeader("authentication", "authentication")
                            .setListener(listener))
                    .addNetworkInterceptor(new NetCacheInterceptor())
                    .cache(new Cache(AppContext.getContext().getExternalCacheDir(),
                            1024 * 1024 * 100))
                    .build();
            request = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(IRequest.class);
        }
    }

    public static DownloadTask build(NetInterceptor.ProgressListener listener) {
        if (downloadTask == null) {
            downloadTask = new DownloadTask(listener);
        }
        return downloadTask;
    }

    public Observable<File> download(String fileName, String path) {
        return request
                .download(fileName)
                .map(response -> FileUtil.saveFile(response.body().byteStream(),
                        path + File.separator + fileName));
    }

    public Observable<String> getBooks() {
        return request.getBooks().map(response -> response.body().string());
    }
}
