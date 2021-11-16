package com.bluetron.nb.common.util.tools;

import com.bluetron.nb.common.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * version1 copy from bluetron-core UrlUtils
 * <p>
 * fit RFC3986 specific encoded string
 *
 * @author wuwenli
 */
@Slf4j
public class URLUtil {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    /**
     * RFC3986 specific
     *
     * @param stringToEncode
     * @return
     */
    public static final String urlEncodeWithUTF8(String stringToEncode) {

        if (stringToEncode == null) {
            return null;
        }
        try {
            String urlEncoderStr = URLEncoder.encode(stringToEncode, UTF_8);

            return urlEncoderStr.replace("+", "%20").replace("%7E", "~");

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Url encode with utf-8 failed.", e);
        }
    }

    public static final String urlDecodeWithUTF8(String stringToDecode) {
        if (stringToDecode == null) {
            return null;
        }
        try {
            return URLDecoder.decode(stringToDecode, UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Url decode with utf-8 failed.", e);
        }
    }

    /**
     * host without Protocol
     *
     * @param url eq http://192.168.8.80:8080
     * @return 192.168.8.80
     */
    public static String getHost(String url) {
        if (StringUtil.isBlank(url)) {
            return url;
        }
        try {
            URL urlObj = new URL(url);
            return urlObj.getHost();
        } catch (MalformedURLException e) {
            log.error("", e);
            return "";
        }
    }

    /**
     * host without Protocol
     *
     * @param url eq http://192.168.8.80:8080
     * @return 192.168.8.80, exception then return defaultVal
     */
    public static String getHost(String url, String defaultVal) {
        return StringUtil.isBlank(getHost(url)) ? defaultVal : getHost(url);
    }

    /**
     * host with Protocol
     *
     * @param url eq http://192.168.8.80:8080
     * @return http://192.168.8.80
     */
    public static String getHostWithProtocol(String url) {

        URL toUrl = null;

        try {
            toUrl = cn.hutool.core.util.URLUtil.toUrlForHttp(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return cn.hutool.core.util.URLUtil.getHost(toUrl).toString();
    }

    /**
     * get PORT
     *
     * @param url
     * @return -1 means no port
     */
    public static String getPort(String url) {

        URL toUrl = null;

        try {
            toUrl = cn.hutool.core.util.URLUtil.toUrlForHttp(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return String.valueOf(toUrl.getPort());
    }

    /**
     * get PORT with return default port
     *
     * @param url
     * @return -1 return default port
     */
    public static String getPort(String url, String defaultPort) {
        return "-1".equals(getPort(url)) ? defaultPort : getPort(url);
    }


    /**
     * get protocol
     *
     * @param url
     * @return exception then return defaultVal
     */
    public static String getProtocol(String url, String defaultVal) {
        URL toUrl = null;
        try {
            toUrl = cn.hutool.core.util.URLUtil.toUrlForHttp(url);
        } catch (Exception e) {
            return defaultVal;
        }
        return toUrl.getProtocol();
    }

    /**
     * 返回某个请求的URI
     *
     * @param httpurl http://localhost:80/iam/v1/resource?accountId=2344654656
     * @return /iam/v1/resource
     */
    public static String getPathURI(String httpurl) {
        return cn.hutool.core.util.URLUtil.getPath(httpurl);
    }

    /**
     * 格式化，去除多余的 /
     *
     * @param url
     * @return
     */
    public static String normalize(String url) {
        return cn.hutool.core.util.URLUtil.normalize(url, false, true);
    }


}
