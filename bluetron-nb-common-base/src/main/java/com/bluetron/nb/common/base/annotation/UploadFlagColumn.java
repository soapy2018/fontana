package com.bluetron.nb.common.base.annotation;

import com.bluetron.nb.common.base.upload.UploadStoreTypeEnum;
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
