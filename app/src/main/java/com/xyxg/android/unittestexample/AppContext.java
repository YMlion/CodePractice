package com.xyxg.android.unittestexample;

import android.app.Application;

import com.xyxg.android.unittestexample.mail.User;

/**
 * @author duoyi
 * @date 2016/11/30
 */

public class AppContext extends Application {

    private static AppContext context;
    private User user;

    public static AppContext getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
