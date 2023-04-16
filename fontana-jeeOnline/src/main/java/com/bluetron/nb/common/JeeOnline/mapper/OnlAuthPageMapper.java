package com.fontana.JeeOnline.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fontana.JeeOnline.entity.OnlAuthPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @interfaceName: OnlAuthPageMapper
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/14 14:47
 */
public interface OnlAuthPageMapper extends BaseMapper<OnlAuthPage> {
    List<String> queryRoleNoAuthCode(@Param("userId") Long userId, @Param("cgformId") String cgformId, @Param("control") Integer control, @Param("page") Integer page, @Param("type") Integer type);
}
