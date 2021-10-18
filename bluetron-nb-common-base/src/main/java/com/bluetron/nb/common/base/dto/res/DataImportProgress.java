package com.bluetron.nb.common.base.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 
 * @author genx
 * @date 2021/4/9 12:10
 */
@Getter
@Setter
public class DataImportProgress<T> implements Serializable {

    /**
     * 导入的ID 用于后续查询
     */
    private String uploadId;

    /**
     * 导入时的头
     */
    private List<ICodeAndName> headers;

    /**
     * 步骤
     * 0 上传成功
     * 1 校验完毕
     * 2 完成导入
     */
    private int step;

    /**
     * 业务类型
     * 导入数据针对的是哪个实体
     */
    private String businessTypeCode;

    /**
     * 校验成功数
     */
    private int verifySuccess;

    /**
     * 需要校验总数
     */
    private int verifyTotal;

    /**
     * 导入成功数
     */
    private int importSuccess;

    /**
     * 需要导入的总数
     * 需要导入的总数 = 需要校验的总数 - 导入数据种的重复数
     */
    private int importTotal;

    @JsonIgnore
    private transient List<UploadData<T>> data;

}
