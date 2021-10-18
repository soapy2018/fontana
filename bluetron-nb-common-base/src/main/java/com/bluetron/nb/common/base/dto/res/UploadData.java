package com.bluetron.nb.common.base.dto.res;


import com.bluetron.nb.common.base.constant.ExcelConst;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 导入对象
 *  <T> 是具体的导入请求对象 例如 PersonnelAddReq
 * @author genx
 * @date 2021/4/9 12:02
 */
public class UploadData<T> implements Serializable {

    public enum ImportResult {
        Add,
        Update,
        Error
    }

    /**
     * 校验结果
     */
    private boolean verifyResult;

    /**
     * 检验失败时的错误信息
     */
    private String verifyMsg;

    /**
     * 数据库导入结果
     * 新增、修改、错误
     */
    private ImportResult importResult;

    /**
     * 数据库导入失败时的错误信息
     * 暂时没有填写
     */
    private String importMsg;

    /**
     * 具体业务类型的导入实体对象
     */
    private T data;

    /**
     * excel上传时的原始数据
     */
    private Map<String, String> originalData;

    /**
     * 错误的Map
     * key是字段code
     */
    private Map<String, String> errors = new HashMap(32);

    public UploadData() {
    }

    public UploadData(T data) {
        this.data = data;
    }

    public boolean isVerifyResult() {
        return verifyResult;
    }

    public void setVerifyResult(boolean verifyResult) {
        this.verifyResult = verifyResult;
    }

    public String getVerifyMsg() {
        return this.verifyResult ? ExcelConst.Qualified : (this.verifyMsg != null ? this.verifyMsg : "");
    }

    public void setVerifyMsg(String verifyMsg) {
        this.verifyMsg = verifyMsg;
    }

    public ImportResult getImportResult() {
        return importResult;
    }

    public void setImportResult(ImportResult importResult) {
        this.importResult = importResult;
    }

    public boolean isImportSuccess() {
        return this.importResult == ImportResult.Add || this.importResult == ImportResult.Update;
    }

    public String getImportMsg() {
        if (this.importResult != null) {
            if (this.importResult == ImportResult.Add) {
                return "新增成功";
            } else if (this.importResult == ImportResult.Update) {
                return "修改成功";
            }
        }
        return StringUtils.isNotBlank(importMsg) ? importMsg : "插入失败";
    }

    public void setImportMsg(String importMsg) {
        this.importMsg = importMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Map<String, String> getOriginalData() {
        return originalData;
    }

    public void setOriginalData(Map<String, String> originalData) {
        this.originalData = originalData;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setError(String code, String error) {
        this.verifyResult = false;
        this.verifyMsg = error;
        this.errors.putIfAbsent(code, error);
    }

}
