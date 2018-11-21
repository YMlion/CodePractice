package com.xyxg.android.unittestexample.mail;

/**
 * @author duoyi
 * @date 2016/12/9
 */

public class User {
    private String host;
    private String account;
    private String pwd;

    public User(String host, String account, String pwd) {
        this.host = host;
        this.account = account;
        this.pwd = pwd;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
