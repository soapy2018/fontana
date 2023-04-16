package com.fontana.base.log;

/**
 * 审计日志接口
 *
 * @author cqf
 * @date 2021/9/3
 * <p>
 */
public interface IAuditService {
    void save(Audit audit);
}
