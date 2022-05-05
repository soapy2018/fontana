package com.fontana.onlineservice.mapper;

import com.fontana.db.mapper.BaseDaoMapper;
import com.fontana.onlineservice.entity.OnlineDatasourceRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据关联数据操作访问接口。
 *
 * @author cqf
 * @date 2021-06-06
 */
public interface OnlineDatasourceRelationMapper extends BaseDaoMapper<OnlineDatasourceRelation> {

    /**
     * 获取过滤后的对象列表。
     *
     * @param filter  主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<OnlineDatasourceRelation> getOnlineDatasourceRelationList(
            @Param("filter") OnlineDatasourceRelation filter, @Param("orderBy") String orderBy);
}
