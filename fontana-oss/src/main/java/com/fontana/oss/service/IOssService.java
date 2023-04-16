package com.fontana.oss.service;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.fontana.oss.model.ObjectInfo;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author cqf
 * @date 2021/2/9

 */
public interface IOssService {
    /**
     * 上传对象
     * @param objectName 对象名
     * @param is 对象流
     */
    ObjectInfo upload(String objectName, InputStream is);

    /**
     * 上传对象
     * @param file 对象
     */
    ObjectInfo upload(MultipartFile file);

    /**
     * 删除对象
     * @param objectKey 对象标识
     */
    void delete(String objectKey);

    /**
     * 输出对象
     * @param objectName 对象名
     * @param os 输出流
     */
    void download(String objectName, OutputStream os);

    /**
     * 输出对象
     * @param objectName 对象名
     * @param response 响应
     */

    void download(String objectName, HttpServletResponse response);

//    /**
//     * 查看文件
//     * @param objectPath 对象路径
//     * @param os 输出流
//     */
//    void view(String objectPath, OutputStream os);
}
