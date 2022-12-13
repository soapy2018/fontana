package com.fontana.oss.template;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.fontana.base.constant.CommonConstants;
import com.fontana.base.constant.StringPool;
import com.fontana.oss.model.ObjectInfo;
import com.fontana.oss.properties.FileServerProperties;
import com.fontana.oss.service.IOssService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Calendar;

/**
 * aws s3配置
 *
 * @author cqf
 * @date 2021/2/11

 */
@Slf4j
@ConditionalOnClass(AmazonS3.class)
@ConditionalOnProperty(prefix = CommonConstants.OSS_PREFIX, name = "type", havingValue = FileServerProperties.TYPE_S3)
public class S3Template implements InitializingBean, IOssService {
    private static final String DEF_CONTEXT_TYPE = "application/octet-stream";

    @Autowired
    private FileServerProperties fileProperties;

    private AmazonS3 amazonS3;

    @Override
    public void afterPropertiesSet() {
        ClientConfiguration config = new ClientConfiguration();
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(fileProperties.getS3().getEndpoint(), fileProperties.getS3().getRegion());
        AWSCredentials credentials = new BasicAWSCredentials(fileProperties.getS3().getAccessKey(), fileProperties.getS3().getAccessKeySecret());
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(credentials);
        this.amazonS3 = AmazonS3Client.builder()
                .withEndpointConfiguration(endpoint)
                .withClientConfiguration(config)
                .withCredentials(awsCredentialsProvider)
                .withPathStyleAccessEnabled(fileProperties.getS3().getPathStyleAccessEnabled())
                .disableChunkedEncoding()
                .build();
        log.info("amazonS3 init success");
    }

    /**
     * 桶不存在，则创建桶
     * @param bucketName bucket名称
     */
    public void createBucket(String bucketName) {
        if (amazonS3.doesBucketExistV2(bucketName)) {
            return;
        }
        amazonS3.createBucket(bucketName);
    }

    @Override
    @SneakyThrows
    public ObjectInfo upload(String fileName, InputStream is) {
        return upload(fileProperties.getS3().getBucketName(), fileName, is, is.available(), DEF_CONTEXT_TYPE);
    }

    @Override
    @SneakyThrows
    public ObjectInfo upload(MultipartFile file) {
        return upload(fileProperties.getS3().getBucketName(), file.getOriginalFilename(), file.getInputStream()
                , ((Long)file.getSize()).intValue(), file.getContentType());
    }

    @SneakyThrows
    public ObjectInfo upload(String bucketName, String fileName, InputStream is) {
        return upload(bucketName, fileName, is, is.available(), DEF_CONTEXT_TYPE);
    }

    /**
     * 上传对象
     * @param bucketName bucket名称
     * @param objectName 对象名
     * @param is 对象流
     * @param size 大小
     * @param contentType 类型
     */
    private ObjectInfo upload(String bucketName, String objectName, InputStream is, int size, String contentType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);
        objectMetadata.setContentType(contentType);
        PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucketName, objectName, is, objectMetadata);
        putObjectRequest.getRequestClientOptions().setReadLimit(size + 1);
        amazonS3.putObject(putObjectRequest);
        ObjectInfo obj = new ObjectInfo();
        obj.setObjectPath(bucketName + StringPool.SLASH + objectName);
        obj.setObjectUrl(fileProperties.getS3().getEndpoint() + StringPool.SLASH + obj.getObjectPath());
        return obj;
    }

    @Override
    public void delete(String objectName) {
        delete(fileProperties.getS3().getBucketName(), objectName);
    }


    public void delete(String bucketName, String objectName) {
        amazonS3.deleteObject(bucketName, objectName);
    }

    /**
     * 获取预览地址
     * @param bucketName bucket名称
     * @param objectName 对象名
     * @param expires 有效时间(分钟)，最大7天有效
     * @return
     */
    public String getViewUrl(String bucketName, String objectName, int expires) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, expires);
        URL url = amazonS3.generatePresignedUrl(bucketName, objectName, cal.getTime());
        return url.toString();
    }

    @Override
    public void download(String objectName, OutputStream os) {
        download(fileProperties.getS3().getBucketName(), objectName, os);
    }

    @Override
    @SneakyThrows
    public void download(String objectName, HttpServletResponse response) {
        String fileName = StringUtils.substringAfterLast(objectName, StringPool.SLASH);
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        download(fileProperties.getS3().getBucketName(), objectName, response.getOutputStream());
    }

    /**
     * 输出对象
     * @param bucketName bucket名称
     * @param objectName 对象名
     * @param os 输出流
     */
    @SneakyThrows
    public void download(String bucketName, String objectName, OutputStream os) {
        S3Object s3Object = amazonS3.getObject(bucketName, objectName);
        try (
                S3ObjectInputStream s3is = s3Object.getObjectContent();
        ) {
            IOUtils.copy(s3is, os);
        }
    }

    /**
     * 输出对象
     * @param objectName 对象名
     * @param path 文件输出目录
     */
    @SneakyThrows
    public void download(String objectName, String path) {
        download(fileProperties.getS3().getBucketName(), objectName, path);
    }

    /**
     * 输出对象到服务器指定目录下
     * @param bucketName bucket名称
     * @param objectName 对象名
     * @param fileFullName 文件完整输出名
     */
    @SneakyThrows
    public void download(String bucketName, String objectName, String fileFullName) {
        S3Object s3Object = amazonS3.getObject(bucketName, objectName);
        try (
                S3ObjectInputStream s3is = s3Object.getObjectContent();
        ) {
            IOUtils.copy(s3is, new FileOutputStream(fileFullName));
        }
    }
}
