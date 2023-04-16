package com.fontana.onlineservice.config;

import com.fontana.base.constant.CommonConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 在线表单的配置对象。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Data
@ConfigurationProperties(prefix = CommonConstants.COMMON_ONLINE_PREFIX)
public class OnlineProperties {

    /**
     * 仅以该前缀开头的数据表才会成为动态表单的候选数据表，如: bn_。如果为空，则所有表均可被选。
     */
    private String tablePrefix;

    /**
     * 在线表单业务操作的URL前缀。
     */
    private String operationUrlPrefix;

    /**
     * 上传文件的根路径。
     */
    private String uploadFileBaseDir;

    /**
     * 在线表单的URL前缀。
     */
    private String urlPrefix;

    /**
     * 在线表单查看权限的URL列表。
     */
    private List<String> viewUrlList;

    /**
     * 在线表单编辑权限的URL列表。
     */
    private List<String> editUrlList;
}
