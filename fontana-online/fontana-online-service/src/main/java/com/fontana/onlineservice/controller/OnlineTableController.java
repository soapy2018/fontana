package com.fontana.onlineservice.controller;

import cn.jimmyshi.beanquery.BeanQuery;
import com.fontana.base.annotation.MyRequestBody;
import com.fontana.base.result.Pagination;
import com.fontana.base.result.Result;
import com.fontana.base.result.ResultCode;
import com.fontana.base.validate.UpdateGroup;
import com.fontana.util.validate.ValidateUtil;
import com.fontana.db.object.MyOrderParam;
import com.fontana.db.object.MyPageParam;
import com.fontana.db.object.MyRelationParam;
import com.fontana.db.util.MyModelUtil;
import com.fontana.db.util.MyPageUtil;
import com.fontana.onlineapi.dto.OnlineTableDto;
import com.fontana.onlineapi.vo.OnlineTableVo;
import com.fontana.onlineservice.entity.OnlineTable;
import com.fontana.onlineservice.service.OnlineTableService;
import com.fontana.util.lang.ObjectUtil;
import com.github.pagehelper.page.PageMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;
import java.util.Map;

/**
 * 数据表操作控制器类。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Slf4j
@RestController
@RequestMapping("${fontana.common-online.urlPrefix}/onlineTable")
public class OnlineTableController {

    @Autowired
    private OnlineTableService onlineTableService;

    /**
     * 更新数据表数据。
     *
     * @param onlineTableDto 更新对象。
     * @return 应答结果对象。
     */
    @PostMapping("/update")
    public Result<Void> update(@MyRequestBody OnlineTableDto onlineTableDto) {
        String errorMessage = ValidateUtil.getModelValidationError(onlineTableDto, Default.class, UpdateGroup.class);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        OnlineTable onlineTable = MyModelUtil.copyTo(onlineTableDto, OnlineTable.class);
        OnlineTable originalOnlineTable = onlineTableService.getById(onlineTable.getTableId());
        if (originalOnlineTable == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前在线数据表并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (!onlineTableService.update(onlineTable, originalOnlineTable)) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.succeed();
    }

    /**
     * 列出符合过滤条件的数据表列表。
     *
     * @param onlineTableDtoFilter 过滤对象。
     * @param orderParam           排序参数。
     * @param pageParam            分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public Result<Pagination<OnlineTableVo>> list(
            @MyRequestBody OnlineTableDto onlineTableDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        OnlineTable onlineTableFilter = MyModelUtil.copyTo(onlineTableDtoFilter, OnlineTable.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, OnlineTable.class);
        List<OnlineTable> onlineTableList =
                onlineTableService.getOnlineTableListWithRelation(onlineTableFilter, orderBy);
        return Result.succeed(MyPageUtil.makeResponseData(onlineTableList, OnlineTable.INSTANCE));
    }

    /**
     * 查看指定数据表对象详情。
     *
     * @param tableId 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public Result<OnlineTableVo> view(@RequestParam Long tableId) {
        if (ObjectUtil.isBlankOrNull(tableId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        OnlineTable onlineTable = onlineTableService.getByIdWithRelation(tableId, MyRelationParam.full());
        if (onlineTable == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        OnlineTableVo onlineTableVo = OnlineTable.INSTANCE.fromModel(onlineTable);
        return Result.succeed(onlineTableVo);
    }

    /**
     * 以字典形式返回全部数据表数据集合。字典的键值为[tableId, modelName]。
     * 白名单接口，登录用户均可访问。
     *
     * @param filter 过滤对象。
     * @return 应答结果对象，包含的数据为 List<Map<String, String>>，map中包含两条记录，key的值分别是id和name，value对应具体数据。
     */
    @GetMapping("/listDict")
    public Result<List<Map<String, Object>>> listDict(OnlineTable filter) {
        List<OnlineTable> resultList = onlineTableService.getListByFilter(filter);
        return Result.succeed(BeanQuery.select(
                "tableId as id", "modelName as name").executeFrom(resultList));
    }
}
