package com.xyxg.android.unittestexample.mail;

import com.sun.mail.imap.IMAPFolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 * 第三方邮箱邮件操作类
 *
 * @author Yao Limin
 * @date 2016/12/8
 */

public class MailOperate {

    public static Folder[] getFolders(String host, String username, String pwd, boolean isSSL) throws MessagingException {
        Store store = MailLogin.imapConnect(host, username, pwd, isSSL);
        if (host.endsWith("gmail.com")) {
            store.connect("imap.gmail.com", username, "ya29.GltjBA-nw7BUTDxed8Ivld9RqGDhZD15KkeM5neDbg4NzSt_pv8y5QJYHLJGsT-UyOp4rtSbtoFIkfXa8ksvWLzEH6o1d7kM1fmhqkvwJ7MOOwnlcgY9VHDbPld3");
        } else {
            store.connect();
        }
        List<Folder> list = new ArrayList<>();
        Folder[] base = store.getDefaultFolder().list();
        for (Folder folder : base) {
            Folder[] folders1 = folder.listSubscribed();
            if (folders1 != null && folders1.length > 0) {
                list.addAll(Arrays.asList(folders1));
            } else {
                list.add(folder);
            }
        }
        return list.toArray(new Folder[list.size()]);
    }

    public static Message[] getFolderMails(String host, String username, String pwd, boolean isSSL, String folderName) throws MessagingException {
        Store store = MailLogin.imapConnect(host, username, pwd, isSSL);
        store.connect();
        Folder folder = store.getFolder(folderName);
        folder.open(Folder.READ_ONLY);
        Message[] messages = folder.getMessages();
        return messages;
    }

    /**
     * 标记为已读
     *
     * @param host       邮箱域名
     * @param username   邮箱地址
     * @param pwd        邮箱密码
     * @param isSSL      是否SSL连接
     * @param folderName 文件夹名称
     * @param messageIds 邮件的UIDs
     * @param isRead     标记为已读/未读
     */
    public static void markRead(String host, String username,
                                String pwd, boolean isSSL, String folderName, long[] messageIds, boolean isRead) throws MessagingException {
        Store store = MailLogin.imapConnect(host, username, pwd, isSSL);
        store.connect();

        IMAPFolder folder = (IMAPFolder) store.getFolder(folderName);
        folder.open(Folder.READ_WRITE);
        Message[] messages = folder.getMessagesByUID(messageIds);
        folder.setFlags(messages, new Flags(Flags.Flag.SEEN), isRead);
        folder.close(true);
        store.close();
    }

    public static void markFlag(String host, String username,
                                String pwd, boolean isSSL, String folderName, long[] messageIds, boolean isFlagged) throws MessagingException {
        Store store = MailLogin.imapConnect(host, username, pwd, isSSL);
        store.connect();

        IMAPFolder folder = (IMAPFolder) store.getFolder(folderName);
        folder.open(Folder.READ_WRITE);
        Message[] messages = folder.getMessagesByUID(messageIds);
        folder.setFlags(messages, new Flags(Flags.Flag.FLAGGED), isFlagged);
        folder.close(true);
        store.close();
    }

    public static void move(String host, String username,
                            String pwd, boolean isSSL, String source, String des, long[] messageIds) throws MessagingException {
        Store store = MailLogin.imapConnect(host, username, pwd, isSSL);
        store.connect();

        IMAPFolder sourceFolder = (IMAPFolder) store.getFolder(source);
        sourceFolder.open(Folder.READ_WRITE);
        Message[] messages = sourceFolder.getMessagesByUID(messageIds);
        IMAPFolder desFolder = (IMAPFolder) store.getFolder(des);
        desFolder.open(Folder.READ_WRITE);
        sourceFolder.copyMessages(messages, desFolder);
        sourceFolder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
        sourceFolder.close(true);
        desFolder.close(true);
        store.close();
    }

    public static void delete(String host, String username,
                              String pwd, boolean isSSL, String source, long[] messageIds) throws MessagingException {
        Store store = MailLogin.imapConnect(host, username, pwd, isSSL);
        store.connect();

        IMAPFolder sourceFolder = (IMAPFolder) store.getFolder(source);
        sourceFolder.open(Folder.READ_WRITE);
        Message[] messages = sourceFolder.getMessagesByUID(messageIds);
        sourceFolder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
        sourceFolder.close(true);
        store.close();
    }
}
