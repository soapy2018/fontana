package com.fontana.oss.tests3;

import com.fontana.oss.model.ObjectInfo;
import com.fontana.oss.template.S3Template;
import com.fontana.util.tools.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @className: OssTest
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/4/26 14:13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootApplication(scanBasePackages = "com.fontana.oss.config")
@Slf4j
public class S3Test {

    @Autowired
    S3Template s3Template;

    @Test
    public void testS3() throws IOException {

        ObjectInfo objectInfo;
        s3Template.delete("美女1.jpeg");
        objectInfo = s3Template.upload("美女1.jpeg", new FileInputStream("src/test/resources/美女.jpeg"));
        log.info("ObjectInfo: {}--{}", objectInfo.getObjectPath(), objectInfo.getObjectUrl());


        s3Template.delete("美女2.jpeg");
        File file = new File("src/test/resources/美女.jpeg");
        MultipartFile mulFile2 = new MockMultipartFile(
                "美女2.jpeg", //文件名
                "美女2.jpeg", //originalName 相当于上传文件在客户机上的文件名
                ContentType.APPLICATION_OCTET_STREAM.toString(), //文件类型
                new FileInputStream(file) //文件流
        );
        objectInfo = s3Template.upload(mulFile2);
        log.info("ObjectInfo: {}--{}", objectInfo.getObjectPath(), objectInfo.getObjectUrl());

        s3Template.createBucket("test2");
        objectInfo = s3Template.upload("test2", "/dd/美女kk.jpeg", new FileInputStream("src/test/resources/美女.jpeg"));
        log.info("ObjectInfo: {}--{}", objectInfo.getObjectPath(), objectInfo.getObjectUrl());

        s3Template.download("test2","/dd/美女kk.jpeg", "src/test/resources/美女kk2.jpeg");


        s3Template.download("美女1.jpeg", "src/test/resources/美女1.jpeg");

    }
}


