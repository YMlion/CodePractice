package com.xyxg.android.unittestexample;

/**
 * @author YML
 * @date 2016/9/6
 */

public class Request {
    public boolean login(String un, String pwd) {
        return "name".equals(un) && "123".equals(pwd);
    }

    public String check(String pwd) {
        return pwd;
    }
}
