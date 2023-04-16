package com.fontana.onlineservice.controller;

import cn.hutool.core.collection.CollUtil;
import com.fontana.base.annotation.MyRequestBody;
import com.fontana.base.result.Pagination;
import com.fontana.base.result.Result;
import com.fontana.base.result.ResultCode;
import com.fontana.base.validate.AddGroup;
import com.fontana.base.validate.UpdateGroup;
import com.fontana.util.validate.ValidateUtil;
import com.fontana.db.object.MyOrderParam;
import com.fontana.db.object.MyPageParam;
import com.fontana.db.object.MyRelationParam;
import com.fontana.db.util.MyModelUtil;
import com.fontana.db.util.MyPageUtil;
import com.fontana.onlineapi.dict.PageType;
import com.fontana.onlineapi.dto.OnlineDatasourceDto;
import com.fontana.onlineapi.vo.OnlineDatasourceVo;
import com.fontana.onlineservice.entity.*;
import com.fontana.onlineservice.object.SqlTable;
import com.fontana.onlineservice.object.SqlTableColumn;
import com.fontana.onlineservice.service.*;
import com.fontana.util.lang.ObjectUtil;
import com.github.pagehelper.page.PageMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/**
 * 数据模型操作控制器类。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Slf4j
@RestController
@RequestMapping("${fontana.common-online.urlPrefix}/onlineDatasource")
public class OnlineDatasourceController {

    @Autowired
    private OnlineDatasourceService onlineDatasourceService;
    @Autowired
    private OnlineFormService onlineFormService;
    @Autowired
    private OnlinePageService onlinePageService;
    @Autowired
    private OnlineTableService onlineTableService;
    @Autowired
    private OnlineDblinkService onlineDblinkService;

    /**
     * 新增数据模型数据。
     *
     * @param onlineDatasourceDto 新增对象。
     * @param pageId              关联的页面Id。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @PostMapping("/add")
    public Result<Long> add(
            @MyRequestBody OnlineDatasourceDto onlineDatasourceDto,
            @MyRequestBody(required = true) Long pageId) {
        String errorMessage = ValidateUtil.getModelValidationError(onlineDatasourceDto, Default.class, AddGroup.class);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        OnlinePage onlinePage = onlinePageService.getById(pageId);
        if (onlinePage == null) {
            errorMessage = "数据验证失败，页面Id不存在！";
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        OnlineDatasource onlineDatasource = MyModelUtil.copyTo(onlineDatasourceDto, OnlineDatasource.class);
        OnlineDblink onlineDblink = onlineDblinkService.getById(onlineDatasourceDto.getDblinkId());
        if (onlineDblink == null) {
            errorMessage = "数据验证失败，关联的数据库链接Id不存在！";
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        SqlTable sqlTable = onlineDblinkService.getDblinkTable(onlineDblink, onlineDatasourceDto.getMasterTableName());
        if (sqlTable == null) {
            errorMessage = "数据验证失败，指定的数据表名不存在！";
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        // 流程表单的主表主键，不能是自增主键。
        if (onlinePage.getPageType().equals(PageType.FLOW)) {
            for (SqlTableColumn tableColumn : sqlTable.getColumnList()) {
                if (tableColumn.getPrimaryKey()) {
                    if (tableColumn.getAutoIncrement()) {
                        errorMessage = "数据验证失败，流程页面所关联的主表主键，不能是自增主键！";
                        return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
                    }
                    break;
                }
            }
        }
        onlineDatasource = onlineDatasourceService.saveNew(onlineDatasource, sqlTable, pageId);
        return Result.succeed(onlineDatasource.getDatasourceId());
    }

    /**
     * 更新数据模型数据。
     *
     * @param onlineDatasourceDto 更新对象。
     * @return 应答结果对象。
     */
    @PostMapping("/update")
    public Result<Void> update(@MyRequestBody OnlineDatasourceDto onlineDatasourceDto) {
        String errorMessage = ValidateUtil.getModelValidationError(onlineDatasourceDto, Default.class, UpdateGroup.class);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        OnlineDatasource onlineDatasource = MyModelUtil.copyTo(onlineDatasourceDto, OnlineDatasource.class);
        OnlineDatasource originalOnlineDatasource = onlineDatasourceService.getById(onlineDatasource.getDatasourceId());
        if (originalOnlineDatasource == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前在线数据源并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (!onlineDatasource.getDblinkId().equals(originalOnlineDatasource.getDatasourceId())) {
            errorMessage = "数据验证失败，不能修改数据库链接Id！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (!onlineDatasource.getMasterTableId().equals(originalOnlineDatasource.getDatasourceId())) {
            errorMessage = "数据验证失败，不能修改主表Id！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (!onlineDatasourceService.update(onlineDatasource, originalOnlineDatasource)) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.succeed();
    }

    /**
     * 删除数据模型数据。
     *
     * @param datasourceId 删除对象主键Id。
     * @return 应答结果对象。
     */
    @PostMapping("/delete")
    public Result<Void> delete(@MyRequestBody Long datasourceId) {
        String errorMessage;
        if (ObjectUtil.isBlankOrNull(datasourceId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        // 验证关联Id的数据合法性
        OnlineDatasource originalOnlineDatasource = onlineDatasourceService.getById(datasourceId);
        if (originalOnlineDatasource == null) {
            errorMessage = "数据验证失败，当前数据源并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        List<OnlineForm> formList = onlineFormService.getOnlineFormListByDatasourceId(datasourceId);
        if (CollUtil.isNotEmpty(formList)) {
            errorMessage = "数据验证失败，当前数据源正在被 [" + formList.get(0).getFormName() + "] 表单占用，请先删除关联数据！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (!onlineDatasourceService.remove(datasourceId)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        return Result.succeed();
    }

    /**
     * 列出符合过滤条件的数据模型列表。
     *
     * @param onlineDatasourceDtoFilter 过滤对象。
     * @param orderParam                排序参数。
     * @param pageParam                 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public Result<Pagination<OnlineDatasourceVo>> list(
            @MyRequestBody OnlineDatasourceDto onlineDatasourceDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        OnlineDatasource onlineDatasourceFilter = MyModelUtil.copyTo(onlineDatasourceDtoFilter, OnlineDatasource.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, OnlineDatasource.class);
        List<OnlineDatasource> onlineDatasourceList =
                onlineDatasourceService.getOnlineDatasourceListWithRelation(onlineDatasourceFilter, orderBy);
        return Result.succeed(MyPageUtil.makeResponseData(onlineDatasourceList, OnlineDatasource.INSTANCE));
    }

    /**
     * 查看指定数据模型对象详情。
     *
     * @param datasourceId 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public Result<OnlineDatasourceVo> view(@RequestParam Long datasourceId) {
        if (ObjectUtil.isBlankOrNull(datasourceId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        OnlineDatasource onlineDatasource =
                onlineDatasourceService.getByIdWithRelation(datasourceId, MyRelationParam.full());
        if (onlineDatasource == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        OnlineDatasourceVo onlineDatasourceVo = OnlineDatasource.INSTANCE.fromModel(onlineDatasource);
        List<OnlineTable> tableList = onlineTableService.getOnlineTableListByDatasourceId(datasourceId);
        if (CollUtil.isNotEmpty(tableList)) {
            onlineDatasourceVo.setTableList(OnlineTable.INSTANCE.fromModelList(tableList));
        }
        return Result.succeed(onlineDatasourceVo);
    }
}
