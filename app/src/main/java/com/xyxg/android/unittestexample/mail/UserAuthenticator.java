package com.xyxg.android.unittestexample.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author duoyi
 * @date 2016/12/1
 */

public class UserAuthenticator extends Authenticator {

    private String username;
    private String password;

    public UserAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
