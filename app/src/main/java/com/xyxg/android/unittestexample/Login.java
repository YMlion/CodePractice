package com.xyxg.android.unittestexample;

import android.widget.Toast;

import com.xyxg.android.unittestexample.dagger.DaggerAppComponent;

import javax.inject.Inject;

/**
 * @author ylm
 * @date 2016/9/6
 */

public class Login {
    @Inject
    Request request;

    public Login() {
        DaggerAppComponent.create().inject(this);
    }

    public void login(String un, String pwd) {
        if (un == null || un.equals(""))
            return;
        if (pwd == null || pwd.equals(""))
            return;

        pwd = request.check(pwd);
        boolean s = request.login(un, pwd);
        if (s) {
            Toast
                    .makeText(AppContext.getContext(), "login successfully", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(AppContext.getContext(), "login failure", Toast.LENGTH_SHORT).show();
        }
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
