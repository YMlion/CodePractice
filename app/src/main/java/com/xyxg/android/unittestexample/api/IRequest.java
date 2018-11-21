package com.xyxg.android.unittestexample.api;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by YMlion on 2018/11/7.
 */
public interface IRequest {

    @POST("books/upload")
    Observable<Response<ResponseBody>> upload(@Body MultipartBody body);
    @POST("books/upload2")
    Observable<Response<ResponseBody>> upload2(@Body MultipartBody body);
}
