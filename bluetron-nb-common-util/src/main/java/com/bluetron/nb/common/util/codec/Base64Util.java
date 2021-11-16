package com.bluetron.nb.common.util.codec;

import cn.hutool.core.codec.Base64;

import java.io.File;
import java.io.InputStream;

/**
 * @author wenshun
 * @date 2020/8/11 18:21
 */
public class Base64Util {


    public static String fileToBase64(File file) {
        return Base64.encode(file);
    }

    public static String toBase64ofInputStream(InputStream in) {
        return Base64.encode(in);
    }
}
