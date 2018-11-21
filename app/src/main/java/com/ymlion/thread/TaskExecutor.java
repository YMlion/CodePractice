package com.ymlion.thread;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskExecutor {

    private static ThreadPoolExecutor mExecutor;
    private static TaskExecutor INSTANCE;
    private static int num = 0;

    public static TaskExecutor build() {
        if (INSTANCE == null) {
            mExecutor = new ThreadPoolExecutor(10, 15, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(10));
            INSTANCE = new TaskExecutor();
        }

        return INSTANCE;
    }

    public void execute(Runnable runnable) {
        mExecutor.execute(runnable);
        System.out.println(mExecutor.getQueue().size());
    }

    public Future<?> submit(Runnable runnable) {
        return mExecutor.submit(runnable);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 25; i++) {
            build().execute(new Runnable() {
                @Override
                public void run() {
                    synchronized (INSTANCE) {
                        num++;
                        System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().toString() + num);
                    }
                    try {
                        Thread.sleep(2000 + num);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
