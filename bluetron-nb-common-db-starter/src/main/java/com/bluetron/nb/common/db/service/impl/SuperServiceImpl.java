package com.bluetron.nb.common.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bluetron.nb.common.base.exception.GeneralException;
import com.bluetron.nb.common.base.lock.DistributedLock;
import com.bluetron.nb.common.base.lock.ZLock;
import com.bluetron.nb.common.db.service.ISuperService;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * service实现父类
 *
 * @author cqf
 * @date 2021/10/10
 * <p>
 */
public class SuperServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements ISuperService<T> {
    @Override
    public boolean saveIdempotency(T entity, DistributedLock locker, String lockKey, Wrapper<T> countWrapper, String msg) throws Exception {
        if (locker == null) {
            throw new GeneralException("DistributedLock is null");
        }
        if (StringUtils.isEmpty(lockKey)) {
            throw new GeneralException("lockKey is null");
        }
        try (
                ZLock lock = locker.tryLock(lockKey, 10, 60, TimeUnit.SECONDS);
        ) {
            if (lock != null) {
                //判断记录是否已存在
                int count = super.count(countWrapper);
                if (count == 0) {
                    return super.save(entity);
                } else {
                    if (StringUtils.isEmpty(msg)) {
                        msg = "已存在";
                    }
                    throw new GeneralException(msg);
                }
            } else {
                throw new GeneralException("锁等待超时");
            }
        }
    }

    @Override
    public boolean saveIdempotency(T entity, DistributedLock lock, String lockKey, Wrapper<T> countWrapper) throws Exception {
        return saveIdempotency(entity, lock, lockKey, countWrapper, null);
    }

    @Override
    public boolean saveOrUpdateIdempotency(T entity, DistributedLock lock, String lockKey, Wrapper<T> countWrapper, String msg) throws Exception {
        if (null != entity) {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            if (null != tableInfo && StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
                Object idVal = ReflectionKit.getFieldValue(entity, tableInfo.getKeyProperty());
                if (null == idVal || Objects.isNull(getById((Serializable) idVal))) {
                    if (StringUtils.isEmpty(msg)) {
                        msg = "已存在";
                    }
                    return this.saveIdempotency(entity, lock, lockKey, countWrapper, msg);
                } else {
                    return updateById(entity);
                }
            } else {
                throw ExceptionUtils.mpe("Error:  Can not execute. Could not find @TableId.");
            }
        }
        return false;
    }

    @Override
    public boolean saveOrUpdateIdempotency(T entity, DistributedLock lock, String lockKey, Wrapper<T> countWrapper) throws Exception {
        return this.saveOrUpdateIdempotency(entity, lock, lockKey, countWrapper, null);
    }
}
