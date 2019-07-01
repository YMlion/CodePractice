package com.xyxg.android.unittestexample.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by YMlion on 2018/11/26.
 */
public class FileUtil {
    public static File saveFile(InputStream inputStream, String filePath) throws IOException {
        File file = new File(filePath);
        Source source = Okio.source(inputStream);
        BufferedSink sink = Okio.buffer(Okio.sink(file));
        sink.writeAll(source);
        source.close();
        sink.close();

        /*BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        BufferedInputStream in = new BufferedInputStream(inputStream);
        byte[] bytes = new byte[8192];
        int l;
        while ((l = in.read(bytes)) != -1) {
            out.write(bytes, 0, l);
            out.flush();
        }
        in.close();
        out.close();*/
        return file;
    }
}
