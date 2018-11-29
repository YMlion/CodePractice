package com.xyxg.android.unittestexample.api;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * Created by YMlion on 2018/11/7.
 */
public interface IRequest {

    @POST("books/upload")
    Observable<String> upload(@Body MultipartBody body);

    @POST("books/upload2")
    Observable<String> upload2(@Body MultipartBody body);

    @Streaming
    @GET("books/download")
    @Headers("Cache-Control: max-age=120")
    Observable<Response<ResponseBody>> download(@Query("fileName") String fileName);

    @GET("books")
    // @Headers("Cache-Control: max-age=3600")
    Observable<Response<ResponseBody>> getBooks();
}
