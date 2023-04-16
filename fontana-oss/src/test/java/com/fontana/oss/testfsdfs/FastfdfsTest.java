package com.fontana.oss.testfsdfs;

import com.fontana.oss.model.ObjectInfo;
import com.fontana.oss.template.FdfsTemplate;
import com.fontana.util.tools.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
public class FastfdfsTest {

    @Autowired
    FdfsTemplate fdfsTemplate;

    @Test
    public void testfastdfs() throws IOException {


        ObjectInfo objectInfo = fdfsTemplate.upload("美女1.jpeg", new FileInputStream("src/test/resources/美女.jpeg"));
        fdfsTemplate.delete(objectInfo.getObjectPath());

        File file = new File("src/test/resources/美女.jpeg");

        MultipartFile mulFile2 = new MockMultipartFile(
                "美女2.jpeg", //文件名
                "美女2.jpeg", //originalName 相当于上传文件在客户机上的文件名
                ContentType.APPLICATION_OCTET_STREAM.toString(), //文件类型
                new FileInputStream(file) //文件流
        );

        objectInfo = fdfsTemplate.upload(mulFile2);
        //fdfsTemplate.delete(objectInfo.getObjectPath());

        fdfsTemplate.download(objectInfo.getObjectPath(),"src/test/resources/美女3.jpeg");

        byte[] bytes = fdfsTemplate.download(objectInfo.getObjectPath());

        MockHttpServletResponse response = new MockHttpServletResponse();
        // 设置强制下载不打开
        response.setContentType("application/force-download");

        String fileName = "test.jpeg";
        fileName = URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8));
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        IoUtil.copy(bytes, response.getOutputStream());
        log.info("第一个方法： {}", response.getContentAsString());

        fdfsTemplate.download(objectInfo.getObjectPath(), response.getOutputStream());
        log.info("第二个方法： {}", response.getOutputStream());

    }
}


