package com.xyxg.android.unittestexample.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by YMlion on 2018/11/26.
 */
public class FileUtil {
    public static File saveFile(InputStream inputStream, String filePath) throws IOException {
        File file = new File(filePath);
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        BufferedInputStream in = new BufferedInputStream(inputStream);
        byte[] bytes = new byte[8192];
        int l;
        while ((l = in.read(bytes)) != -1) {
            out.write(bytes, 0, l);
            out.flush();
        }
        in.close();
        out.close();
        return file;
    }
}
