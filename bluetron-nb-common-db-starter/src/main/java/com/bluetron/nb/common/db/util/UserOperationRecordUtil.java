package com.bluetron.nb.common.db.util;

import com.bluetron.nb.common.base.dto.ILoginAccountDTO;
import com.bluetron.nb.common.base.entity.IUserOperationRecordEntity;
import java.util.Date;

/**
 * 设置创建/更新的一些字段
 * @author genx
 * @date 2021/4/8 10:45
 */
public class UserOperationRecordUtil {

    /**
     * 添加创建记录
     * @param entity
     * @param loginUserDTO
     */
    public static void recordCreate(IUserOperationRecordEntity entity, ILoginAccountDTO loginUserDTO) {
        Date now = new Date();
        entity.setCreateUsername(loginUserDTO.getUsername());
        entity.setCreateRealname(loginUserDTO.getRealname());
        entity.setCreateTime(now);
        entity.setUpdateUsername(loginUserDTO.getUsername());
        entity.setUpdateRealname(loginUserDTO.getRealname());
        entity.setUpdateTime(now);
    }


    /**
     * 添加修改记录
     * @param entity
     * @param loginUserDTO
     */
    public static void recordUpdate(IUserOperationRecordEntity entity, ILoginAccountDTO loginUserDTO) {
        entity.setUpdateUsername(loginUserDTO.getUsername());
        entity.setUpdateRealname(loginUserDTO.getRealname());
        entity.setUpdateTime(new Date());
    }


}
