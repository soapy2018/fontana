package com.bluetron.nb.common.spring.log;

/**
 * 审计日志接口
 *
 * @author bcloud
 * @date 2020/2/3
 * <p>
 * 
 
 */
public interface IAuditService {
    void save(Audit audit);
}
