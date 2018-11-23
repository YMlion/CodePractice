package com.xyxg.android.unittestexample.api;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
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
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .addNetworkInterceptor(new NetInterceptor().addHeader("authentication",
                            "authentication")
                                                               .setListener(listener))
                    .build();
            request =
                    new Retrofit.Builder()
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
        return request.download(fileName)
                .map(response -> {
                    File file = new File(path, fileName);
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                    BufferedInputStream in = new BufferedInputStream(response.body().byteStream());
                    byte[] bytes = new byte[4096];
                    int l;
                    while ((l = in.read(bytes)) != -1) {
                        out.write(bytes, 0, l);
                        out.flush();
                    }
                    in.close();
                    out.close();
                    return file;
                });
    }
}
