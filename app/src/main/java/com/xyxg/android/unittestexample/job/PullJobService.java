package com.xyxg.android.unittestexample.job;

import android.annotation.TargetApi;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

@TargetApi(21)
public class PullJobService extends JobService {

    private static final String TAG = "PullJobService";
    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: " + Thread.currentThread());
        return false;
    }

    @Override public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "onStopJob: ");
        return false;
    }
}
