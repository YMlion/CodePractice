package com.xyxg.android.unittestexample.api;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by YMlion on 2018/11/7.
 */
public interface IRequest {

    @POST("books/upload")
    Observable<String> upload(@Body MultipartBody body);
    @POST("books/upload2")
    Observable<String> upload2(@Body MultipartBody body);

    @GET("books/download")
    Observable<Response<ResponseBody>> download(@Query("fileName") String fileName);
}
