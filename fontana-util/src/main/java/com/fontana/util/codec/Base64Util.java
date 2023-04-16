package com.fontana.util.codec;

import cn.hutool.core.codec.Base64;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author cqf
 * @date 2021/8/11 18:21
 */
public class Base64Util extends org.springframework.util.Base64Utils{


    public static String fileToBase64(File file) {
        return Base64.encode(file);
    }

    public static String toBase64ofInputStream(InputStream in) {
        return Base64.encode(in);
    }

    /**
     * 编码
     *
     * @param value 字符串
     * @return {String}
     */
    public static String encode(String value) {
        return Base64Util.encode(value, StandardCharsets.UTF_8);
    }

    /**
     * 编码
     *
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     */
    public static String encode(String value, java.nio.charset.Charset charset) {
        byte[] val = value.getBytes(charset);
        return new String(Base64Util.encode(val), charset);
    }

    /**
     * 编码URL安全
     *
     * @param value 字符串
     * @return {String}
     */
    public static String encodeUrlSafe(String value) {
        return Base64Util.encodeUrlSafe(value, StandardCharsets.UTF_8);
    }

    /**
     * 编码URL安全
     *
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     */
    public static String encodeUrlSafe(String value, java.nio.charset.Charset charset) {
        byte[] val = value.getBytes(charset);
        return new String(Base64Util.encodeUrlSafe(val), charset);
    }

    /**
     * 解码
     *
     * @param value 字符串
     * @return {String}
     */
    public static String decode(String value) {
        return Base64Util.decode(value, StandardCharsets.UTF_8);
    }

    /**
     * 解码
     *
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     */
    public static String decode(String value, java.nio.charset.Charset charset) {
        byte[] val = value.getBytes(charset);
        byte[] decodedValue = Base64Util.decode(val);
        return new String(decodedValue, charset);
    }

    /**
     * 解码URL安全
     *
     * @param value 字符串
     * @return {String}
     */
    public static String decodeUrlSafe(String value) {
        return Base64Util.decodeUrlSafe(value, StandardCharsets.UTF_8);
    }

    /**
     * 解码URL安全
     *
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     */
    public static String decodeUrlSafe(String value, java.nio.charset.Charset charset) {
        byte[] val = value.getBytes(charset);
        byte[] decodedValue = Base64Util.decodeUrlSafe(val);
        return new String(decodedValue, charset);
    }
}
