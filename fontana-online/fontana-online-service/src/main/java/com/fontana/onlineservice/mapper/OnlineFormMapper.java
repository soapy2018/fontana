package com.fontana.onlineservice.mapper;

import com.fontana.db.mapper.BaseDaoMapper;
import com.fontana.onlineservice.entity.OnlineForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 在线表单数据操作访问接口。
 *
 * @author cqf
 * @date 2021-06-06
 */
public interface OnlineFormMapper extends BaseDaoMapper<OnlineForm> {

    /**
     * 获取过滤后的对象列表。
     *
     * @param onlineFormFilter 主表过滤对象。
     * @param orderBy          排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<OnlineForm> getOnlineFormList(
            @Param("onlineFormFilter") OnlineForm onlineFormFilter, @Param("orderBy") String orderBy);
}
