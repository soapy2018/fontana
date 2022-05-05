package com.fontana.JeeOnline.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fontana.JeeOnline.entity.OnlCgformIndex;
import org.apache.ibatis.annotations.Param;

/**
 * @interfaceName: OnlCgformIndexMapper
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/12 15:50
 */
public interface OnlCgformIndexMapper extends BaseMapper<OnlCgformIndex> {
    int queryIndexCount(@Param("sqlStr") String sqlStr);
}
