package com.xyxg.android.unittestexample.mail;

import javax.mail.Message;
import javax.mail.internet.MimeUtility;

/**
 * Created by YMlion on 2018/12/10.
 */
public class MailParse {
    public static String parseSubject(Message msg) throws Exception {
        String subject = msg.getSubject();
        return subject == null ? "" : MimeUtility.decodeText(subject);
    }
}
