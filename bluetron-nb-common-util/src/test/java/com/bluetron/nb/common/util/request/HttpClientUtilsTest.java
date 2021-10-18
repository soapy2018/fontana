package com.bluetron.nb.common.util.request;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class HttpClientUtilsTest {

    @Test
    public void testGet() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("a", "1");

        Map<String, Object> map1 = HttpClientUtils.get("http://192.168.8.84:40914/actuator/health", param, null);
        assertThat(map1.toString()).isEqualTo("{code=200, message=, content={\"status\":\"UP\"}}");

        Map<String, Object> map2 = HttpClientUtils.get("http://192.168.8.84:40914/actuator/health?ss=4", param, null);
        assertThat(map2.toString()).isEqualTo("{code=200, message=, content={\"status\":\"UP\"}}");

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

        HttpClientUtils.postFormData("http://192.168.8.93:48080/api/v1/fs/notice/static/resource",
                inputStream, originalFilename, contentType, Maps.newHashMap());

    }

    @Test
    public void urlBindParamsTest() {
        Map<String, String> params = Maps.newHashMap();
        params.put("a", "a");
        params.put("b", "b");
        params.put("c", "c");
        assertThat(ParameterHelper.urlBindParams("http://www.abc.com",params))
                .isEqualTo("http://www.abc.com?a=a&b=b&c=c");
    }
}
