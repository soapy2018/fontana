package com.fontana.oss.config;

import com.fontana.oss.template.FdfsTemplate;
import com.fontana.oss.template.S3Template;
import com.fontana.oss.properties.FileServerProperties;
import com.fontana.oss.updownload.OssUpDownloader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author cqf
 * @date 2021/2/13

 */
@Configuration
@EnableConfigurationProperties(FileServerProperties.class)
@Import({FdfsTemplate.class, S3Template.class, OssUpDownloader.class})
@Slf4j
public class OssAutoConfigure {

    public OssAutoConfigure() {
        log.info(">>>> OSS START >>>> ");
    }

}
