package com.fontana.util.request;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wenshun
 * @date 2019/8/14 18:03
 */
@Slf4j
public class HttpClientUtil {

    private HttpClientUtil() {

    }

    /**
     * 没在用 verifySSL，暂时去除
     *
     * @param uri
     * @return
     */
    private static CloseableHttpClient createHttpClient(String uri) {
        if (uri.toLowerCase().contains(HttpClientConfig.getDefault().getHTTPS())) {
            // if (verifySSL) {
            // // 如果需要验证ssl,则返回默认构造对象
            // return HttpClients.createDefault();
            // } else {
            // 如果需要绕过ssl验证,则返回自定义构造对象
            return HttpClients.custom()
                    .setSSLSocketFactory(HttpClientConfig.getDefault().getSslConnectionSocketFactory()).build();
            // }
        } else {
            return HttpClients.createDefault();
        }
    }

    private static Map<String, Object> httpClientExec(HttpUriRequest request, boolean verifySSL) {
        Map<String, Object> responseMap = new HashMap<>(16);
        StringBuilder resContent = new StringBuilder();
        String statusLine = "";
        InputStream iStream = null;
        try (CloseableHttpClient client = createHttpClient(request.getURI().toASCIIString());
             CloseableHttpResponse httpResponse = client.execute(request);) {
            statusLine = httpResponse.getStatusLine().toString();
            Integer code = Integer.valueOf(httpResponse.getStatusLine().getStatusCode());
            responseMap.put("code", code);
            responseMap.put("message", httpResponse.getStatusLine().getReasonPhrase());
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                iStream = httpEntity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(iStream, Consts.UTF_8));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    resContent.append(line);
                }
                responseMap.put("content", resContent.toString());
            }
            if (code != HttpStatus.SC_OK) {
                log.warn("invoke url warn -->" + request.getURI().toString() + ", code=" + code + ", response="
                        + resContent.toString());
            }
        } catch (Exception e) {
            responseMap.put("code", HttpStatus.SC_BAD_REQUEST);
            if (e instanceof ConnectException) {
                responseMap.put("code", HttpStatus.SC_REQUEST_TIMEOUT);
            }
            responseMap.put("message", e.getMessage());
            log.error("invoke url error -->" + request.getURI().toString() + ", response=" + statusLine, e);
        } finally {
            if (iStream != null) {
                try {
                    iStream.close();
                } catch (IOException e) {
                }
            }
        }
        return responseMap;
    }

    /**
     * 未配置ssl证书设置的get
     *
     * @param url
     * @param params
     * @param header
     * @return
     */
    public static Map<String, Object> get(String url, Map<String, String> params, Map<String, String> header) {
        return get(url, params, header, true);
    }

    /**
     * 配置绕过ssl证书设置的get
     *
     * @param url
     * @param params
     * @param header
     * @param verifySSL
     * @return
     */
    public static Map<String, Object> get(String url, Map<String, String> params, Map<String, String> header,
                                          boolean verifySSL) {
        StringBuilder urlParams = new StringBuilder();
        if (params != null) {
            if (url.contains("?")) {
                urlParams.append("&");
            } else {
                urlParams.append("?");
            }
            int paramSize = params.size();
            int i = 0;
            for (String key : params.keySet()) {
                urlParams.append(key + "=" + params.get(key));
                i++;
                if (i < paramSize) {
                    urlParams.append("&");
                }
            }
        }
        String getAppUrl = url + urlParams.toString();
        getAppUrl = getAppUrl.replace(" ", "%20");

        HttpGet httpGet = new HttpGet(getAppUrl);
        setBaseHeader(httpGet, HttpClientConfig.getDefault().getRequestConfig());

        if (header != null) {
            for (String key : header.keySet()) {
                httpGet.setHeader(key, header.get(key));
            }
        }

        return httpClientExec(httpGet, verifySSL);
    }

    /**
     * 带路径参数post请求
     *
     * @param url
     * @param postParams
     * @param params
     * @param header
     * @return
     */
    public static Map<String, Object> post(String url, StringEntity postParams, Map<String, String> params,
                                           Map<String, String> header) {

        return post(ParameterHelper.urlBindParams(url, params),
                postParams, header, true);
    }

    /**
     * 未配置ssl证书设置的post
     *
     * @param url
     * @param postParams
     * @param header
     * @return
     */
    public static Map<String, Object> post(String url, StringEntity postParams, Map<String, String> header) {
        return post(url, postParams, header, true);
    }

    /**
     * 配置绕过ssl验证的post
     *
     * @param url
     * @param postParams
     * @param header
     * @return
     */
    public static Map<String, Object> post(String url, StringEntity postParams, Map<String, String> header,
                                           boolean verifySSL) {
        HttpPost post = new HttpPost(url);
        setBaseHeader(post, HttpClientConfig.getDefault().getRequestConfig());
        if (header != null) {
            for (String key : header.keySet()) {
                post.setHeader(key, header.get(key));
            }
        }
        post.setEntity(postParams);
        return httpClientExec(post, verifySSL);
    }

    public static Map<String, Object> put(String url, StringEntity postParams, Map<String, String> params,
                                          Map<String, String> header) {
        String api = ParameterHelper.urlBindParams(url, params);
        return put(api, postParams, header);
    }

    public static Map<String, Object> put(String url, StringEntity postParams, Map<String, String> header) {

        HttpPut put = new HttpPut(url);
        setBaseHeader(put, HttpClientConfig.getDefault().getRequestConfig());
        if (header != null) {
            for (String key : header.keySet()) {
                put.setHeader(key, header.get(key));
            }
        }
        put.setEntity(postParams);
        return httpClientExec(put, true);
    }

    /**
     * 该方法 内置 APP 2.7版本在用！！
     * <p>
     * 不要删除！
     *
     * @param url
     * @param inputStream
     * @param originalFilename
     * @param contentType
     * @param header
     * @return
     */
    public static Map<String, Object> postFormData(String url, InputStream inputStream, String originalFilename,
                                                   String contentType, Map<String, String> header) {
        ContentType type = ContentType.IMAGE_JPEG;
        if (StringUtils.isNoneBlank(contentType)) {
            type = ContentType.parse(contentType);
        }
        HttpPost post = new HttpPost(url);
        setBaseHeader(post, HttpClientConfig.getDefault().getRequestConfig());

        if (header != null) {
            for (String key : header.keySet()) {
                post.setHeader(key, header.get(key));
            }
        }

        MultipartEntityBuilder mimbuilder = MultipartEntityBuilder.create();
        mimbuilder.setMode(HttpMultipartMode.RFC6532);
        mimbuilder.addBinaryBody("file", inputStream, type, originalFilename);
        post.setEntity(mimbuilder.build());
        post.removeHeaders("Content-type");

        return httpClientExec(post, true);
    }

    public static Map<String, Object> postFormData(String url, InputStream inputStream, String contentType,
                                                   String originalFilename, Map<String, String> params, Map<String, String> bodyParams,
                                                   Map<String, String> header) {

        String realUrl = ParameterHelper.urlBindParams(url, params);
        HttpPost post = new HttpPost(realUrl);
        setBaseHeader(post, HttpClientConfig.getDefault().getRequestConfig());

        if (header != null) {
            for (String key : header.keySet()) {
                post.setHeader(key, header.get(key));
            }
        }

        MultipartEntityBuilder mimbuilder = MultipartEntityBuilder.create();
        mimbuilder.setMode(HttpMultipartMode.RFC6532);
        mimbuilder.addBinaryBody("file", inputStream, ContentType.parse(contentType), originalFilename);
        if (bodyParams != null) {
            for (String key : bodyParams.keySet()) {
                mimbuilder.addTextBody(key, bodyParams.get(key));
            }
        }
        post.setEntity(mimbuilder.build());
        return httpClientExec(post, true);
    }

    public static Map<String, Object> delete(String url, Map<String, String> header) {
        HttpDelete deleteHttp = new HttpDelete(url);
        setBaseHeader(deleteHttp, HttpClientConfig.getDefault().getRequestConfig());
        if (header != null) {
            for (String key : header.keySet()) {
                deleteHttp.setHeader(key, header.get(key));
            }
        }
        return httpClientExec(deleteHttp, true);
    }

    /**
     * 设置基本的http头信息
     *
     * @param requestBase
     * @param config
     */
    private static void setBaseHeader(HttpRequestBase requestBase, RequestConfig config) {
        requestBase.setConfig(config);
        requestBase.setHeader("User-Agent", "Mozilla/5.0");
        requestBase.setHeader("Content-Type", "application/json");
    }

}
