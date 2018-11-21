package com.xyxg.android.unittestexample.mail;

import android.util.Log;
import com.sun.mail.util.MailSSLSocketFactory;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * @author Yao Limin
 * @date 2016/12/8
 */

public class MailLogin {

    private static final String SSL_FACTORY = "com.sun.mail.util.MailSSLSocketFactory";

    public static Store imapConnect(String host, String username, String pwd, boolean isSSL) throws MessagingException {
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
        Properties props = new Properties();
//        username = "testtx@henhaoji.com";
//        pwd = "33280484";
        if (isSSL) {
            props.put("mail.imaps.starttls.enable", true);
//        props.put("mail.imaps.starttls.required", true);
            props.put("mail.imaps.auth", true);
            props.put("mail.imaps.port", "993");
//        props.put("mail.imaps.socketFactory.port", "993");
            props.put("mail.store.protocol", "imaps");
            if (host.equals("outlook.com")) {
                props.put("mail.imaps.host", "imap-mail." + host);
            } else if (host.equals("henhaoji.com")) {
                props.put("mail.imaps.host", "mail2.henhaoji.com");
            } else {
                props.put("mail.imaps.host", /*"mail2.henhaoji.com"*/"imap." + host);
            }
            props.put("mail.imaps.ssl.enable", true);
            props.put("mail.imaps.ssl.socketFactory.port", "993");
            props.put("mail.imaps.ssl.socketFactory.class", SSL_FACTORY);
        } else {
            props.put("mail.imap.port", "143");
            props.put("mail.store.protocol", "imap");
            props.put("mail.imap.host", "imap." + host);
        }
        props.put("mail.debug", "true");
        Session session;
        if (username.endsWith("gmail.com")) {
            props.put("mail.imaps.auth.mechanisms", "XOAUTH2");
            session = Session.getInstance(props);
        } else {
            session = Session.getInstance(props, new UserAuthenticator(username, pwd));
        }
        return session.getStore();
    }

    public static Store pop3Connect(String host, String username, String pwd, boolean isSSL) throws NoSuchProviderException {
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
        Properties props = new Properties();
        if (isSSL) {
            props.put("mail.store.protocol", "pop3");
            props.put("mail.pop3.host", "pop3." + host);
            props.put("mail.pop3.ssl.enable", true);
            props.put("mail.pop3.ssl.socketFactory.port", "995");
            MailSSLSocketFactory socketFactory = null;
            try {
                socketFactory = new MailSSLSocketFactory();
                socketFactory.setTrustAllHosts(true);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            if (socketFactory != null) {
                props.put("mail.pop3.ssl.socketFactory", socketFactory);
            } else {
                props.put("mail.pop3.ssl.socketFactory.class", SSL_FACTORY);
            }
            props.put("mail.pop3.socketFactory.fallback", false);
            props.put("mail.pop3.ssl.checkserveridentity", "false");
        } else {
            props.put("mail.pop3.port", "110");
            props.put("mail.store.protocol", "pop3");
            props.put("mail.pop3.host", "pop3." + host);
        }
        props.put("mail.debug", "true");

        Session session = Session.getInstance(props, new UserAuthenticator(username, pwd));

        return session.getStore();
    }

    public static Session smtpConnect(String host, String username, String pwd, boolean isSSL) {
        /*MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart*//*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);*/
        Properties props = new Properties();
//        username = "testtx@henhaoji.com";
//        pwd = "33280484";
        boolean isOut = false;
        if (host.equals("outlook.com")) {
            isOut = true;
        }
        if (isOut) {
            props.put("mail.smtp.host", "smtp-mail." + host);
        } else if (host.equals("henhaoji.com")) {
            props.put("mail.smtp.host", "mail2.henhaoji.com");
        } else {
            props.put("mail.smtp.host", /*"121.201.116.215"*/"smtp." + host);
        }
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        if (isSSL) {
            String port = "465";
            if (isOut) {
                port = "587";
                props.put("mail.smtp.starttls.enable", true);
                props.put("mail.smtp.starttls.required", true);
            } else {
                props.put("mail.smtp.socketFactory.port", port);
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.ssl.enable", true);
            }
            props.put("mail.smtp.port", port);
        } else {
            props.put("mail.smtp.port", "25");
        }
        if (host.endsWith("gmail.com")) {
            return Session.getDefaultInstance(props);
        } else {
            return Session.getInstance(props, new UserAuthenticator(username, pwd));
        }
    }
}
