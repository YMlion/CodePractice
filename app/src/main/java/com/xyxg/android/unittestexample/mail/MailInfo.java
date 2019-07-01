package com.xyxg.android.unittestexample.mail;

import android.util.Log;

import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.QPDecoderStream;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import static android.content.ContentValues.TAG;


public class MailInfo {


    public static String getSubject(MimeMessage msg)
            throws MessagingException, UnsupportedEncodingException {
        return MimeUtility.decodeText(msg.getSubject());
    }

    /**
     * 获得邮件发件人
     *
     * @param msg 邮件内容
     * @return 姓名 <Email地址>
     */
    public static String getFrom(MimeMessage msg)
            throws MessagingException, UnsupportedEncodingException {
        String from;
        Address[] froms = msg.getFrom();
        if (froms.length < 1) {
            throw new MessagingException("没有发件人!");
        }

        InternetAddress address = (InternetAddress) froms[0];
        String person = address.getPersonal();
        if (person != null) {
            person = MimeUtility.decodeText(person) + " ";
        } else {
            person = "";
        }
        from = person + "<" + address.getAddress() + ">";

        return from;
    }

    /**
     * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人
     * <p>Message.RecipientType.TO  收件人</p>
     * <p>Message.RecipientType.CC  抄送</p>
     * <p>Message.RecipientType.BCC 密送</p>
     *
     * @param msg  邮件内容
     * @param type 收件人类型
     * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
     */
    public static String getReceiveAddress(MimeMessage msg, Message.RecipientType type)
            throws MessagingException {
        StringBuilder receiveAddress = new StringBuilder();
        Address[] addresses;
        if (type == null) {
            addresses = msg.getAllRecipients();
        } else {
            addresses = msg.getRecipients(type);
        }

        if (addresses == null || addresses.length < 1) {
            return "";
        }
        for (Address address : addresses) {
            InternetAddress internetAddress = (InternetAddress) address;
            receiveAddress
                    .append(internetAddress.getPersonal())
                    .append(':')
                    .append(internetAddress.getAddress())
                    .append(",");
        }

        receiveAddress.deleteCharAt(receiveAddress.length() - 1); //删除最后一个逗号

        return receiveAddress.toString();
    }

    /**
     * 获得邮件文本内容
     *
     * @param part    邮件体
     * @param content 存储邮件文本内容的字符串
     */
    public static void getMailTextContent(Part part, StringBuilder content)
            throws MessagingException, IOException {
        //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
        Log.e(TAG, "getMailTextContent: 1");
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        Log.e(TAG, "getMailTextContent: 2");
        Object partContent = part.getContent();
        Log.e(TAG, "getMailTextContent: 3");
        if (part.isMimeType("text/*") && !isContainTextAttach) {
            Log.e(TAG, "getMailTextContent: 4");
            if (partContent instanceof String) {
                Log.e(TAG, "getMailTextContent: 5");
                content.append((String) partContent);
                Log.e(TAG, "getMailTextContent: 6");
            } else if (partContent instanceof QPDecoderStream
                    || partContent instanceof BASE64DecoderStream) {
                BufferedInputStream bis = new BufferedInputStream((InputStream) partContent);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while (true) {
                    int c = bis.read();
                    if (c == -1) {
                        break;
                    }
                    baos.write(c);
                }
                baos.flush();
                Log.e(TAG, "getMailTextContent1: " + part.getContentType());
                String contentType = part.getContentType();
                int index = contentType.indexOf("charset");
                String charset = "UTF-8";
                if (index > 0) {
                    charset = contentType.substring(index);
                    if (charset.contains("\"")) {
                        charset = charset.substring(charset.indexOf('"') + 1,
                                charset.lastIndexOf('"'));
                    } else {
                        charset = charset.substring(charset.indexOf('=') + 1);
                    }
                }
                content.append(MimeUtility.decodeText(new String(baos.toByteArray(), charset)));
                bis.close();
                baos.close();
            }
        } else if (part.isMimeType("message/rfc822")) {
            getMailTextContent((Part) partContent, content);
        } else if (part.isMimeType("multipart/*")) {
            if (partContent instanceof Multipart) {
                Multipart multipart = (Multipart) partContent;
                int partCount = multipart.getCount();
                for (int i = 0; i < partCount; i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    getMailTextContent(bodyPart, content);
                }
            } else if (partContent instanceof InputStream) {
                Multipart multipart = new MimeMultipart(
                        new ByteArrayDataSource((InputStream) partContent, "multipart/*"));
                int partCount = multipart.getCount();
                for (int i = 0; i < partCount; i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    getMailTextContent(bodyPart, content);
                }
            }
        }
    }

    /**
     * 判断字符串的编码
     */
    private static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
            encode = "ISO-8859-1";
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
            encode = "UTF-8";
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
            encode = "GBK";
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isRead(MimeMessage message) throws MessagingException {
        return message.getFlags().contains(Flags.Flag.SEEN);
    }

    public static boolean isStar(MimeMessage message) throws MessagingException {
        return message.getFlags().contains(Flags.Flag.FLAGGED);
    }

    /**
     * 判断邮件中是否包含附件
     *
     * @return 邮件中存在附件返回true，不存在返回false
     */
    public static boolean isContainAttachment(Part part) throws MessagingException, IOException {
        boolean flag = false;
        if (part.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT)
                        || disp.equalsIgnoreCase(Part.INLINE))) {
                    String fileName = bodyPart.getFileName();
                    Log.e(TAG, "Mail parse : " + fileName + "; " + getEncoding(fileName));
                    checkLines(bodyPart);
                    if (fileName.equals(MimeUtility.decodeText(fileName))) {
                        Log.e(TAG, "file name is messy " + isMessyCode(fileName));
                        String gbk = new String(fileName.getBytes("iso-8859-1"), "GBK");
                        Log.e(TAG, "Mail parse : " + gbk);
                    }
                    flag = true;
                } else if (bodyPart.isMimeType("multipart/*")) {
                    flag = isContainAttachment(bodyPart);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.contains("application")) {
                        String fileName = bodyPart.getFileName();
                        if (fileName == null) {
                            checkLines(bodyPart);
                        }
                        Log.e(TAG, "Mail parse 2: " + fileName + "; " + getEncoding(fileName));
                        flag = true;
                    }

                    if (contentType.contains("name")) {
                        String fileName = bodyPart.getFileName();
                        Log.e(TAG, "Mail parse 3: " + fileName + "; " + getEncoding(fileName));
                        flag = true;
                    }
                }

                //if (flag) break;
            }
        } else if (part.isMimeType("message/rfc822")) {
            flag = isContainAttachment((Part) part.getContent());
        }
        return flag;
    }

    private static void checkLines(BodyPart bodyPart) throws MessagingException {
        if (bodyPart instanceof MimeBodyPart) {
            Enumeration<String> lines = ((MimeBodyPart) bodyPart).getAllHeaderLines();
            String line = null;
            while (lines.hasMoreElements()) {
                line = lines.nextElement();
                int index = line.indexOf("filename");
                if (index >= 0) {
                    Log.e(TAG, "checkLines: original : " + line);
                    line = line.substring(index + 10);
                    line = line.substring(0, line.indexOf('"'));
                    try {
                        line = MimeUtility.decodeText(line);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            Log.e(TAG, "body part line : " + line);
        }
    }

    private static boolean isMessyCode(String strName) {
        try {
            Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
            Matcher m = p.matcher(strName);
            String after = m.replaceAll("");
            String temp = after.replaceAll("\\p{P}", "");
            char[] ch = temp.trim().toCharArray();

            for (char c : ch) {
                if (!Character.isLetterOrDigit(c)) {
                    String str = "" + c;
                    if (!str.matches("[\u4e00-\u9fa5]+")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
