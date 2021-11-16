package com.bluetron.nb.common.util.tools;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class URLUtilTest {

    private String baseUrl = "http://192.168.8.80:8081";
    private String body = "10 - 汉+字?p=k.png&q=a_b#1";
    private String decodeStr = "10%20-%20%E6%B1%89%2B%E5%AD%97%3Fp%3Dk.png%26q%3Da_b%231";


    @Test
    public void urlEncodeWithUTF8Test() {
        assertThat(URLUtil.urlEncodeWithUTF8(body))
                .isEqualTo(decodeStr);
    }

    @Test
    public void urlDecodeWithUTF8Test() {
        assertThat(URLUtil.urlDecodeWithUTF8(decodeStr)).isEqualTo(body);
    }

    @Test
    public void getHostTest() {
        String host = URLUtil.getHost(baseUrl);
        assertThat(host).isEqualTo("192.168.8.80");
    }

    @Test
    public void getHostWithProtocolTest() {
        String host = URLUtil.getHostWithProtocol(baseUrl);
        assertThat(host).isEqualTo("http://192.168.8.80");

        String protocol = URLUtil.getProtocol("https://192.168.8.80", "http");
        assertThat(protocol).isEqualTo("https");
    }

    @Test
    public void getProtocolTest() {
        String protocol = URLUtil.getProtocol("https://192.168.8.80", "http");
        assertThat(protocol).isEqualTo("https");

        String protocol1 = URLUtil.getProtocol("192.168.8.80", "http");
        assertThat(protocol1).isEqualTo("http");
    }


    @Test
    public void getPortTest() {
        String port = URLUtil.getPort(baseUrl);
        assertThat(port).isEqualTo("8081");

        String port1 = URLUtil.getPort("http://192.168.8.80", "90");
        assertThat(port1).isEqualTo("90");

        String port2 = URLUtil.getPort("http://192.168.8.80:9090", "90");
        assertThat(port2).isEqualTo("9090");

        String noPort = URLUtil.getPort("http://192.168.8.80");
        assertThat(noPort).isEqualTo("-1");
    }

    @Test
    public void getHttpURITest() {
        String URL = "http://dt.57.nb.dev.supos.net/iam/v1/resource?accountId=2344654656";
        assertThat(URLUtil.getPathURI(URL)).isEqualTo("/iam/v1/resource");
    }

    @Test
    public void testNormalize() {
        String URL = "http://dt.57.nb.dev.supos.net/iam//v1/resource?accountId=2344654656";
        assertThat(URLUtil.normalize(URL)).isEqualTo("http://dt.57.nb.dev.supos.net/iam/v1/resource?accountId=2344654656");
    }

}
