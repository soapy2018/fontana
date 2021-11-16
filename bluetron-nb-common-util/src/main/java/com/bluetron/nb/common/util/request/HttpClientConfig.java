package com.bluetron.nb.common.util.request;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * @author wenshun
 * @date 2020/4/22 9:45
 */
@Slf4j
@Getter
public class HttpClientConfig {

    private String HTTP = "http";
    private String HTTPS = "https";
    private Integer CONNECT_TIMEOUT = 10000;
    private Integer CONNECT_REQUEST_TIMEOUT = 2000;
    private Integer SOCKET_TIMEOUT = 10000;

    /**
     * connect pools
     **/
    private int maxRequests = 64;
    private int maxRequestsPerHost = 64;

    private RequestConfig requestConfig;
    private SSLConnectionSocketFactory sslConnectionSocketFactory;


    private HttpClientConfig() {

        requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT).build();

        sslConnectionSocketFactory = this.createIgnoreSSLsocketFactory();
    }


    public static HttpClientConfig getDefault() {
        HttpClientConfig config = new HttpClientConfig();
        return config;
    }

    public RequestConfig getRequestConfig() {
        return this.requestConfig;
    }

    /**
     * 自定义sslcontext绕过ssl认证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public SSLContext createNotVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[]{trustManager}, null);
        return sslContext;
    }

    /**
     * 创建https对应的ssl socket
     *
     * @return
     */
    public SSLConnectionSocketFactory createIgnoreSSLsocketFactory() {
        try {
            return new SSLConnectionSocketFactory(
                    createNotVerifySSL(), NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
