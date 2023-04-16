package com.fontana.util.updownload;

import java.lang.annotation.*;

/**
 * 用于标记支持数据上传和下载的字段。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UploadFlagColumn {

    /**
     * 上传数据存储类型。
     *
     * @return 上传数据存储类型。
     */
    UploadStoreTypeEnum storeType();
}
