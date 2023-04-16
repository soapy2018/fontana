package com.fontana.util.request;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class HttpClientUtilTest {

    @Test
    public void testGet() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("a", "1");

        Map<String, Object> map1 = HttpClientUtil.get("http://192.168.8.84:40914/actuator/health", param, null);
        assertThat(map1.toString()).isEqualTo("{code=200, message=, content={\"status\":\"UP\"}}");

        Map<String, Object> map2 = HttpClientUtil.get("http://192.168.8.84:40914/actuator/health?ss=4", param, null);
        assertThat(map2.toString()).isEqualTo("{code=200, message=, content={\"status\":\"UP\"}}");

    }

    @Test
    public void testPost() {

        HashMap<String, String> head = new HashMap<String, String>();
        head.put("Authorization", "Basic d2ViQXBwOndlYkFwcA==");
        head.put("Content-Type", "application/x-www-form-urlencoded");

        StringEntity requestBody = new StringEntity("grant_type=password_code&deviceId=C28FE8EB-4DF9-4234-8429-07A125AD63A5&validCode=fctu&username=bcloud&password=admin", "UTF-8");
        Map<String, Object> map = HttpClientUtil.post("http://192.168.8.119:9901/api-uaa/oauth/token", requestBody, head);

        log.info(map.toString());
        //assertThat(map.toString()).isEqualTo("{code=200, message=, content={\"status\":\"UP\"}}");

    }

    @Test
    public void postFormData() {

        File pngFile = new File("src/test/resources/google.png");

        String originalFilename = pngFile.getName();

        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(pngFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }

        String contentType = "multipart/form-data";

        HttpClientUtil.postFormData("http://192.168.8.93:48080/api/v1/fs/notice/static/resource",
                inputStream, originalFilename, contentType, Maps.newHashMap());

    }

    @Test
    public void urlBindParamsTest() {
        Map<String, String> params = Maps.newHashMap();
        params.put("a", "a");
        params.put("b", "b");
        params.put("c", "c");
        assertThat(ParameterHelper.urlBindParams("http://www.abc.com", params))
                .isEqualTo("http://www.abc.com?a=a&b=b&c=c");
    }
}
