package com.fontana.onlineservice.controller;

import com.fontana.base.annotation.MyRequestBody;
import com.fontana.base.result.Pagination;
import com.fontana.base.result.Result;
import com.fontana.base.result.ResultCode;
import com.fontana.db.object.MyOrderParam;
import com.fontana.db.object.MyPageParam;
import com.fontana.db.util.MyModelUtil;
import com.fontana.db.util.MyPageUtil;
import com.fontana.onlineapi.dto.OnlineDblinkDto;
import com.fontana.onlineapi.vo.OnlineDblinkVo;
import com.fontana.onlineservice.entity.OnlineDblink;
import com.fontana.onlineservice.object.SqlTable;
import com.fontana.onlineservice.object.SqlTableColumn;
import com.fontana.onlineservice.service.OnlineDblinkService;
import com.github.pagehelper.page.PageMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据库链接操作控制器类。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Slf4j
@RestController
@RequestMapping("${fontana.common-online.urlPrefix}/onlineDblink")
public class OnlineDblinkController {

    @Autowired
    private OnlineDblinkService onlineDblinkService;

    /**
     * 列出符合过滤条件的数据库链接列表。
     *
     * @param onlineDblinkDtoFilter 过滤对象。
     * @param orderParam            排序参数。
     * @param pageParam             分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public Result<Pagination<OnlineDblinkVo>> list(
            @MyRequestBody OnlineDblinkDto onlineDblinkDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        OnlineDblink onlineDblinkFilter = MyModelUtil.copyTo(onlineDblinkDtoFilter, OnlineDblink.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, OnlineDblink.class);
        List<OnlineDblink> onlineDblinkList =
                onlineDblinkService.getOnlineDblinkListWithRelation(onlineDblinkFilter, orderBy);
        return Result.succeed(MyPageUtil.makeResponseData(onlineDblinkList, OnlineDblink.INSTANCE));
    }

    /**
     * 获取指定数据库链接下的所有动态表单依赖的数据表列表。
     *
     * @param dblinkId 数据库链接Id。
     * @return 所有动态表单依赖的数据表列表
     */
    @GetMapping("/listDblinkTables")
    public Result<List<SqlTable>> listDblinkTables(@RequestParam Long dblinkId) {
        OnlineDblink dblink = onlineDblinkService.getById(dblinkId);
        if (dblink == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.succeed(onlineDblinkService.getDblinkTableList(dblink));
    }

    /**
     * 获取指定数据库链接下，指定数据表的所有字段信息。
     *
     * @param dblinkId  数据库链接Id。
     * @param tableName 表名。
     * @return 该表的所有字段列表。
     */
    @GetMapping("/listDblinkTableColumns")
    public Result<List<SqlTableColumn>> listDblinkTableColumns(
            @RequestParam Long dblinkId, @RequestParam String tableName) {
        OnlineDblink dblink = onlineDblinkService.getById(dblinkId);
        if (dblink == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.succeed(onlineDblinkService.getDblinkTableColumnList(dblink, tableName));
    }
}
