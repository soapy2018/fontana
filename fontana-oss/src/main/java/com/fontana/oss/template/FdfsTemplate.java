package com.fontana.oss.template;

import com.fontana.base.constant.CommonConstants;
import com.fontana.base.constant.StringPool;
import com.fontana.oss.model.ObjectInfo;
import com.fontana.oss.properties.FileServerProperties;
import com.fontana.oss.service.IOssService;
import com.fontana.util.tools.IoUtil;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * FastDFS配置
 *
 * @author cqf
 * @date 2021/2/11

 */
@Slf4j
@ConditionalOnClass(FastFileStorageClient.class)
@ConditionalOnProperty(prefix = CommonConstants.OSS_PREFIX, name = "type", havingValue = FileServerProperties.TYPE_FDFS)
public class FdfsTemplate implements IOssService {

    @Resource
    private FileServerProperties fileProperties;

    @Resource
    private FastFileStorageClient storageClient;

    @Override
    @SneakyThrows
    public ObjectInfo upload(String objectName, InputStream is) {
        return upload(objectName, is, is.available());
    }

    @Override
    @SneakyThrows
    public ObjectInfo upload(MultipartFile file) {
        return upload(file.getOriginalFilename(), file.getInputStream(), file.getSize());
    }

    /**
     * 上传对象
     * @param objectName 对象名
     * @param is 对象流
     * @param size 大小
     */
    private ObjectInfo upload(String objectName, InputStream is, long size) {
        StorePath storePath = storageClient.uploadFile(is, size, FilenameUtils.getExtension(objectName), null);
        ObjectInfo obj = new ObjectInfo();
        obj.setObjectPath(storePath.getFullPath());
        //上传后完整文件名与上传源文件完全不一样，fastdfs按照自己规则构建路径及名称
        log.info("StorePath: {}", storePath.getFullPath());
        obj.setObjectUrl("http://" + fileProperties.getFdfs().getWebUrl() + StringPool.SLASH + storePath.getFullPath());
        return obj;
    }

    /**
     * 删除对象
     * @param objectPath 对象路径
     */
    @Override
    public void delete(String objectPath) {
        if (!StringUtils.isEmpty(objectPath)) {
            StorePath storePath = StorePath.parseFromUrl(objectPath);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        }
    }

    /**
     * 下载对象
     * @param objectPath 对象路径
     * @param callback 回调
     */
    public <T> T download(String objectPath, DownloadCallback<T> callback) {
        if (!StringUtils.isEmpty(objectPath)) {
            StorePath storePath = StorePath.parseFromUrl(objectPath);
            return storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), callback);
        }
        return null;
    }

    /**
     * 下载对象，返回byte[]
     * @param objectPath 对象路径
     * @return byte[]
     */
    public byte[] download(String objectPath) {
        if (!StringUtils.isEmpty(objectPath)) {
            StorePath storePath = StorePath.parseFromUrl(objectPath);
            return storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), IoUtil::toByteArray);
        }
        return null;
    }

    /**
     * 下载对象，返回OutputStream，用于web下载
     * @param objectPath 对象路径
     * @param os 输出流
     */
    @Override
    public void download(String objectPath, OutputStream os) {
        if (!StringUtils.isEmpty(objectPath)) {
            StorePath storePath = StorePath.parseFromUrl(objectPath);
            storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), ins ->
                IoUtil.copy(ins, os));
        }
    }

    @Override
    public void download(String objectName, HttpServletResponse response) {
        String fileName = org.apache.commons.lang3.StringUtils.substringAfterLast(objectName, StringPool.SLASH);
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
    }

    /**
     * 下载对象到服务器指定路径下
     * @param objectPath 对象路径
     * @param fullFileName 完成文件名
     * @return OutputStream
     */
    public void download(String objectPath, String fullFileName) {
        if (!StringUtils.isEmpty(objectPath)) {
            StorePath storePath = StorePath.parseFromUrl(objectPath);
            storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), ins -> {
                FileUtils.copyInputStreamToFile(ins, new File(fullFileName));
                return null;
            });
        }
    }

}
