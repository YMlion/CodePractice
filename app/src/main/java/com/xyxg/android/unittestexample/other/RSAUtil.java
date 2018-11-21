package com.xyxg.android.unittestexample.other;

import android.util.Base64;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class RSAUtil {

    //指定加密算法为RSA
    private static String ALGORITHM = "RSA";
    //指定key的大小
    private static int KEYSIZE = 2048;
    //指定公钥存放文件和私钥存放文件
    private static String PUBLIC_KEY_FILE = "public.key";
//    private static String PUBLIC_KEY_FILE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/public.key";
//    private static String PRIVATE_KEY_FILE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/private.key";
    private static String PRIVATE_KEY_FILE = "private.key";

    //生成公钥和私钥并分别存放在文件中
    private static void generateKeyPair() throws Exception{
        //生成密钥对
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
        kpg.initialize(KEYSIZE, new SecureRandom());
        KeyPair kp = kpg.generateKeyPair();
        //通过密钥对分别得到公钥和私钥
        Key publicKey = kp.getPublic();
        Key privateKey = kp.getPrivate();
        //将生成的密钥写入文件
        ObjectOutputStream output1 = new ObjectOutputStream(new FileOutputStream(PUBLIC_KEY_FILE));
        ObjectOutputStream output2 = new ObjectOutputStream(new FileOutputStream(PRIVATE_KEY_FILE));
        output1.writeObject(publicKey);
        output2.writeObject(privateKey);
        output1.close();
        output2.close();
    }

    //RSA加密方法
    public static String encrypt(String source, String publicKeyFile) throws Exception {
        //读出文件中的公钥对象
        /*ObjectInputStream ois = new ObjectInputStream(new FileInputStream(publicKeyFile));
        Key key = (Key) ois.readObject();
        ois.close();*/
        /*BASE64Encoder base64Encoder = new BASE64Encoder();
        String publicKeyStr = base64Encoder.encode(key.getEncoded());
        System.out.println("public key : " + publicKeyStr);*/
        Key key1 = loadPublicKeyByStr("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDC" +
                "eJogoPnHy1zQ9mrQHhPddPlUCNGZkREGQ4/pR02pRy+yNpRM5skrg25FsF" +
                "z4vCihQKQuanK2eUtAOtD9MIv18WUtiDsdVp66PMPd7XJXTQ7t5xADaphR" +
                "uYcjoGqkoEJw+w7ey2GF7ULHkoS3gjmnlE405jfgCvo/E5QQJn2anwIDAQAB");
        //得到Cipher对象来实现对源数据的RSA加密
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key1);
        byte[] b = source.getBytes();
        BASE64Encoder encoder = new BASE64Encoder();
        byte[] doFinal = cipher.doFinal(b);
        String cryptograph = encoder.encode(doFinal);
        try {
            String aS = new String(Base64.encode(doFinal, Base64.NO_WRAP), "utf-8");
            System.out.println("android : " + aS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cryptograph;
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr
     *            公钥数据字符串
     * @throws Exception
     *             加载公钥时产生的异常
     */
    public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr)
            throws Exception {
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] buffer = decoder.decodeBuffer(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从字符串中加载公钥
     *
     */
    public static RSAPublicKey loadPublicKeyByBytes(byte[] bytes)
            throws Exception {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    //RSA解密方法
    public static String decrypt(String cryptograph, String privateKeyFile) throws Exception {
        //读出文件中的私钥对象
        ObjectInputStream input = new ObjectInputStream(new FileInputStream(privateKeyFile));
        Key key = (Key) input.readObject();
        input.close();
        //对已经加密的数据进行RSA解密
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b1 = decoder.decodeBuffer(cryptograph);
        //执行解密操作
        byte[] b = cipher.doFinal(b1);
        String source = new String(b);
        return source;
    }

    //调用方法举例
    public static void main(String[] args) {
        String source = "123456";
        System.out.println("明文字符串：[" + source + "]");
        try {
            strEn(source);
            //生成可用的密钥对并分别保存在文件中
            generateKeyPair();
            System.out.println("生成的公钥文件为：" + PUBLIC_KEY_FILE + ", 生成的私钥文件为：" + PRIVATE_KEY_FILE);
            String cryptograph = encrypt(source, PUBLIC_KEY_FILE);//生成的密文
            System.out.println("加密之后的字符串为：[" + cryptograph + "]");
            String text = decrypt(cryptograph, PRIVATE_KEY_FILE);//解密密文
            System.out.println("解密之后的字符串为：[" + text + "]");
        } catch(Exception e){
            System.out.println("加解密过程中发生错误：" + e.getMessage());
            return;
        }
    }

    public static void strEn(String source) throws Exception {
        Key key1 = loadPublicKeyByStr("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDCeJogoPnHy1zQ9mrQH" +
                "hPddPlUCNGZkREGQ4/pR02pRy+yNpRM5skrg25FsFz4vCihQKQuanK2eUtAOtD9MIv18WUtiD" +
                "sdVp66PMPd7XJXTQ7t5xADaphRuYcjoGqkoEJw+w7ey2GF7ULHkoS3gjmnlE405jfgCvo/E5QQJn" +
                "2anwIDAQAB");
        //得到Cipher对象来实现对源数据的RSA加密
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding"/*ALGORITHM*/);
        cipher.init(Cipher.ENCRYPT_MODE, key1);
        byte[] b = source.getBytes();
        BASE64Encoder encoder = new BASE64Encoder();
        byte[] doFinal = cipher.doFinal(b);
        String cryptograph = encoder.encode(doFinal);
        System.out.println(cryptograph);
    }
}