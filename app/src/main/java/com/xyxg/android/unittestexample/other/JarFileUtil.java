package com.xyxg.android.unittestexample.other;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static android.content.ContentValues.TAG;

/**
 * Created by duoyi on 2017/2/10.
 */


public class JarFileUtil {

    public static boolean verifyPatchMetaSignature(File path) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(path);
            final Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                // no code
                if (jarEntry == null) {
                    continue;
                }

                final String name = jarEntry.getName();
                if (name.startsWith("META-INF/")) {
                    continue;
                }
                //for faster, only check the meta.txt files
                //we will check other files's mad5 written in meta files
                /*if (!name.endsWith(ShareConstants.META_SUFFIX)) {
                    continue;
                }
                metaContentMap.put(name, SharePatchFileUtil.loadDigestes(jarFile, jarEntry));
                Certificate[] certs = jarEntry.getCertificates();
                if (certs == null) {
                    return false;
                }
                if (!check(path, certs)) {
                    return false;
                }*/
            }
        } catch (Exception e) {
        } finally {
            try {
                if (jarFile != null) {
                    jarFile.close();
                }
            } catch (IOException e) {
                Log.e(TAG, path.getAbsolutePath(), e);
            }
        }
        return true;
    }


    public static void main(String[] args) {
        verifyPatchMetaSignature(new File("patch_signed_7zip.apk"));
    }
}
