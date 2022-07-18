package com.fontana.log.auditLog;

import com.fontana.base.constant.CommonConstants;
import com.fontana.base.log.Audit;
import com.fontana.base.log.IAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

/**
 * 审计日志实现类-打印日志
 *
 * @author cqf
 * @date 2020/2/3
 * <p>
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = CommonConstants.AUDITLOG_PREFIX, name = "type", havingValue = "logger", matchIfMissing = true)
public class LoggerAuditServiceImpl implements IAuditService {
    private static final String MSG_PATTERN = "{}|{}|{}|{}|{}|{}|{}|{}|{}";

    /**
     * 格式为：{时间}|{应用名}|{类名}|{方法名}|{用户id}|{用户名}|{租户id}|{操作耗时}|{操作信息}
     * 例子：2020-02-04 09:13:34.650|user-center|com.bluetron.app.bcloud.user.controller.SysUserController|saveOrUpdate|1|admin|webApp|8ms|新增用户:admin
     */
    @Override
    public void save(Audit audit) {
        log.info(MSG_PATTERN
                , audit.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
                , audit.getApplicationName(), audit.getClassName(), audit.getMethodName()
                , audit.getUserId(), audit.getUserName(), audit.getTenantId(), audit.getTimeConsume()
                , audit.getOperation());
    }
}
