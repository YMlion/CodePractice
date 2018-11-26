package com.xyxg.android.unittestexample.api;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by YMlion on 2018/11/20.
 */
public class UploadTask {
    private static final String BASE_URL = "http://10.160.2.126:56789/api/";
    private static IRequest request;
    private static UploadTask uploadTask;

    private UploadTask(NetInterceptor.ProgressListener listener) {
        if (request == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .addNetworkInterceptor(new NetInterceptor()
                            .addHeader("authentication", "authentication")
                            .setListener(listener))
                    .build();
            request = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(IRequest.class);
        }
    }

    public static UploadTask build(NetInterceptor.ProgressListener listener) {
        if (uploadTask == null) {
            uploadTask = new UploadTask(listener);
        }
        return uploadTask;
    }

    public Observable<String> upload(String path) {
        File file = new File(path);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addPart(MultipartBody.Part.createFormData("file", file.getName(),
                RequestBody.create(MediaType.parse("multipart/form-data"), file)));
        return request.upload(builder.build()).compose(Http.handleResult());
    }

    public Observable<String> upload2(String type, String path) {
        File file = new File(path);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("type", type);
        builder.addPart(MultipartBody.Part.createFormData("file", file.getName(),
                RequestBody.create(MediaType.parse("multipart/form-data"), file)));
        return request.upload2(builder.build()).compose(Http.handleResult());
    }
}
