package com.xyxg.android.unittestexample.mail;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author duoyi
 * @date 2016/12/26
 */

public class Encrypt {

    public static String encodeAES(String plaintext, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128, new SecureRandom(key.getBytes()));
        SecretKey secretKey = keyGenerator.generateKey();
        System.out.println(Base64.encodeToString(secretKey.getEncoded(), Base64.NO_WRAP));
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new SecureRandom());
        byte desBytes[] = cipher.doFinal(plaintext.getBytes("UTF-8"));
        String encodeString = Base64.encodeToString(desBytes, Base64.NO_WRAP);
        System.out.println(encodeString);
        return encodeString;
    }
}
