package com.xyxg.android.unittestexample;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.xyxg.android.unittestexample.api.DownloadTask;
import com.xyxg.android.unittestexample.api.NetInterceptor;
import com.xyxg.android.unittestexample.api.UploadTask;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FileNetActivity extends AppCompatActivity {
    private static final String TAG = "FileNetActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_net);
    }

    public void upload(View view) {
        UploadTask.build(null)
                  .upload(Environment.getExternalStorageDirectory()
                                     .getAbsolutePath() +
                          File.separator + "download" + File.separator + /*"chinamap_2063x1458.jpg"*/"thz.mp3")
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Observer<String>() {
                      @Override
                      public void onSubscribe(Disposable d) {

                      }

                      @Override
                      public void onNext(String s) {
                          Log.d("TAG", "onNext: " + s);
                      }

                      @Override
                      public void onError(Throwable e) {
                          e.printStackTrace();
                      }

                      @Override
                      public void onComplete() {
                          Log.d("TAG", "onComplete: ");
                      }
                  });
    }

    public void upload2(View view) {
        UploadTask.build((NetInterceptor.UploadProgressListener) (total, writtenByte, progress) -> Log
                .d(TAG, "upload2: " + total + " : " + writtenByte + " : " + progress))
                  .upload2("upload file", Environment
                          .getExternalStorageDirectory()
                          .getAbsolutePath() +
                          File.separator + "download" + File.separator + /*"chinamap_2063x1458.jpg"*/"thz.mp3")
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Observer<String>() {
                      @Override
                      public void onSubscribe(Disposable d) {

                      }

                      @Override
                      public void onNext(String s) {
                          Log.d("TAG", "onNext: " + s);
                      }

                      @Override
                      public void onError(Throwable e) {
                          e.printStackTrace();
                      }

                      @Override
                      public void onComplete() {
                          Log.d("TAG", "onComplete: ");
                      }
                  });
    }

    public void download(View view) {
        DownloadTask.build((NetInterceptor.DownloadProgressListener) (total, writtenByte, progress) -> Log.d(TAG, "onProgress: " + total + " : " + writtenByte + " : " + progress))
                    .download("chinamap_2063x1458.jpg", getExternalCacheDir().getAbsolutePath())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<File>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(File file) {
                            Log.d(TAG, "onNext: " + file.getAbsolutePath());
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete: ");
                        }
                    });
    }
}
