package com.fontana.onlineservice.mapper;

import com.fontana.db.mapper.BaseDaoMapper;
import com.fontana.onlineservice.entity.OnlineVirtualColumn;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 虚拟字段数据操作访问接口。
 *
 * @author cqf
 * @date 2021-06-06
 */
public interface OnlineVirtualColumnMapper extends BaseDaoMapper<OnlineVirtualColumn> {

    /**
     * 获取过滤后的对象列表。
     *
     * @param onlineVirtualColumnFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<OnlineVirtualColumn> getOnlineVirtualColumnList(
            @Param("onlineVirtualColumnFilter") OnlineVirtualColumn onlineVirtualColumnFilter, @Param("orderBy") String orderBy);
}
