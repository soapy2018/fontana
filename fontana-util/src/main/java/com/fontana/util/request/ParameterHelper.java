package com.fontana.util.request;

import java.util.Map;

/**
 * @program: suplink-parent
 * @description:
 * @author: wuwenli
 * @date: 2021-08-13 16:01
 **/
public class ParameterHelper {

    private ParameterHelper() {

    }

    /**
     * @param url
     * @param params
     * @return
     */
    public static String urlBindParams(String url, Map<String, String> params) {
        return url + parameterMapToString(params);
    }

    /**
     * @return ?xxx=ss&pp=ss&oo=oo
     */
    public static String parameterMapToString(Map<String, String> params) {

        String urlAppender = "";
        StringBuilder builder = new StringBuilder();

        if (params != null && !params.isEmpty()) {
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                if (i == 0) {
                    builder.append("?");
                } else {
                    builder.append("&");
                }
                i++;
                builder.append(key).append("=").append(entry.getValue());
            }
        }
        urlAppender = builder.toString();
        return urlAppender;
    }
}
