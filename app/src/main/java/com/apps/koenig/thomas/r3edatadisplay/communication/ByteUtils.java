package com.apps.koenig.thomas.r3edatadisplay.communication;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by Thomas on 26.02.2017.
 */
public class ByteUtils {
    public static final String encoding = "UTF-8";
    public static String getString(ByteBuffer buffer, int length) {

        byte[] bytes = new byte[length];
        buffer.get(bytes);

        // look for \0
        int i;
        for (i = 0; i < bytes.length && bytes[i] != 0; i++) { }
        return new String(bytes, 0, i ,Charset.forName(encoding)).trim();
    }
}
