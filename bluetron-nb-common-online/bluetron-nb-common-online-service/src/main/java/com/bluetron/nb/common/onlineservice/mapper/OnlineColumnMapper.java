package com.bluetron.nb.common.onlineservice.mapper;

import com.bluetron.nb.common.db.mapper.BaseDaoMapper;
import com.bluetron.nb.common.onlineservice.entity.OnlineColumn;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字段数据数据操作访问接口。
 *
 * @author cqf
 * @date 2021-06-06
 */
public interface OnlineColumnMapper extends BaseDaoMapper<OnlineColumn> {

    /**
     * 获取过滤后的对象列表。
     *
     * @param onlineColumnFilter 主表过滤对象。
     * @param orderBy            排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<OnlineColumn> getOnlineColumnList(
            @Param("onlineColumnFilter") OnlineColumn onlineColumnFilter, @Param("orderBy") String orderBy);
}
