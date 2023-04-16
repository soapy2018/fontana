package com.fontana.log.requestlog.config;

import com.fontana.util.lang.StringUtil;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

/**
 * @author chenqf
 * @description 请求体InputStream只能读取一次，所以需要此包装类缓存InputStream，同时重写getInputStream方法，并将此包装类通过filter传递
 * 后续需要获取InputStream直接使用包装类的getInputStream方法拿到包装类的缓存，包括spring框架获取方法参数、自己调用等
 * 参数有请求头参数和请求体参数，获取请求体参数（getParameterMap或者getParameter）也会调用getInputStream方法，因此请求体参数解析后InputStream也会为空
 * 当post请求且content-type为x-www-form-urlencoded或者multipart/form-data，且请求体有内容时， getInputStream必须在getParameterMap或者getParameter（方法会将url和请求体参数都拿出来放到ParameterMap里，注意文件参数是拿不出来的）之后，否则controller方法参数就获取不到
 * 原因见： https://www.jianshu.com/p/0586e757e0af
 * 解决方案见：https://www.jb51.net/article/216193.htm
 * 思路是：对于这类请求，在包装filter里就用getParameterMap方法将参数（包括url和请求体参数）拿出来放到ParameterMap（这是spring框架干的）和包装类的body里（这是包装类干的），此时InputStream已置空，controller参数绑定时会直接从ParameterMap里取值
 * 其实对于这类请求，不用包装也行，可以在需要记录参数时用getParameterMap拿一下就行
 * @date 2022/6/20 14:08
 */
public class RequestWrapper extends HttpServletRequestWrapper {

//    private final String body;
//
//    public RequestWrapper(HttpServletRequest request) {
//        super(request);
//        StringBuilder stringBuilder = new StringBuilder();
//        BufferedReader bufferedReader = null;
//        InputStream inputStream = null;
//        try {
//            inputStream = request.getInputStream();
//            if (inputStream != null) {
//                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                char[] charBuffer = new char[128];
//                int bytesRead = -1;
//                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
//                    stringBuilder.append(charBuffer, 0, bytesRead);
//                }
//            } else {
//            }
//        } catch (IOException ex) {
//
//        } finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (bufferedReader != null) {
//                try {
//                    bufferedReader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        body = stringBuilder.toString();
//    }
//
//    @Override
//    public ServletInputStream getInputStream() throws IOException {
//        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
//        ServletInputStream servletInputStream = new ServletInputStream() {
//            @Override
//            public boolean isFinished() {
//                return false;
//            }
//
//            @Override
//            public boolean isReady() {
//                return false;
//            }
//
//            @Override
//            public void setReadListener(ReadListener readListener) {
//            }
//
//            @Override
//            public int read() throws IOException {
//                return byteArrayInputStream.read();
//            }
//        };
//        return servletInputStream;
//
//    }
//
//    @Override
//    public BufferedReader getReader() throws IOException {
//        return new BufferedReader(new InputStreamReader(this.getInputStream()));
//    }
//
//    public String getBody() {
//        return this.body;
//    }

    private final String body;

    public RequestWrapper(HttpServletRequest request) {
        super(request);
        this.body = getBodyString(request);
    }

    public String getBody() {
        return body;
    }

    @SneakyThrows
    public String getBodyString(final HttpServletRequest request) {
        String contentType = request.getContentType();
        if (StringUtil.isNotBlank(contentType) && (contentType.contains("multipart/form-data") || contentType.contains("x-www-form-urlencoded"))) {
            return parseParameter(request);
        } else {
            return IOUtils.toString(request.getInputStream(), Charset.defaultCharset());
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public int read() {
                return bais.read();
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    private String parseParameter(final HttpServletRequest request) {
        String bodyString = "";
        StringBuilder sb = new StringBuilder();
        //ps：对于multipart/form-data,文件参数获取不到
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> next : parameterMap.entrySet()) {
            String[] values = next.getValue();
            String value = null;
            if (values != null) {
                if (values.length == 1) {
                    value = values[0];
                } else {
                    value = Arrays.toString(values);
                }
            }
            sb.append(next.getKey()).append("=").append(value).append("&");
        }
        if (sb.length() > 0) {
            bodyString = sb.substring(0, sb.toString().length() - 1);
        }
        return bodyString;
    }

}
