package com.bluetron.nb.common.onlineservice.controller;

import cn.hutool.core.collection.CollUtil;
import com.bluetron.nb.common.base.annotation.MyRequestBody;
import com.bluetron.nb.common.base.result.CallResult;
import com.bluetron.nb.common.base.result.Pagination;
import com.bluetron.nb.common.base.result.Result;
import com.bluetron.nb.common.base.result.ResultCode;
import com.bluetron.nb.common.base.validate.AddGroup;
import com.bluetron.nb.common.base.validate.UpdateGroup;
import com.bluetron.nb.common.util.validate.ValidateUtil;
import com.bluetron.nb.common.db.object.MyOrderParam;
import com.bluetron.nb.common.db.object.MyPageParam;
import com.bluetron.nb.common.db.object.MyRelationParam;
import com.bluetron.nb.common.db.util.MyModelUtil;
import com.bluetron.nb.common.db.util.MyPageUtil;
import com.bluetron.nb.common.onlineapi.dto.OnlineDatasourceRelationDto;
import com.bluetron.nb.common.onlineapi.vo.OnlineDatasourceRelationVo;
import com.bluetron.nb.common.onlineservice.entity.*;
import com.bluetron.nb.common.onlineservice.object.SqlTable;
import com.bluetron.nb.common.onlineservice.object.SqlTableColumn;
import com.bluetron.nb.common.onlineservice.service.*;
import com.bluetron.nb.common.util.lang.ObjectUtil;
import com.github.pagehelper.page.PageMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/**
 * 数据源关联操作控制器类。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Slf4j
@RestController
@RequestMapping("${bluetron-nb-common.common-online.urlPrefix}/onlineDatasourceRelation")
public class OnlineDatasourceRelationController {

    @Autowired
    private OnlineDatasourceRelationService onlineDatasourceRelationService;
    @Autowired
    private OnlineDatasourceService onlineDatasourceService;
    @Autowired
    private OnlineVirtualColumnService onlineVirtualColumnService;
    @Autowired
    private OnlineDblinkService onlineDblinkService;
    @Autowired
    private OnlineFormService onlineFormService;

    /**
     * 新增数据关联数据。
     *
     * @param onlineDatasourceRelationDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @PostMapping("/add")
    public Result<Long> add(@MyRequestBody OnlineDatasourceRelationDto onlineDatasourceRelationDto) {
        String errorMessage = ValidateUtil.getModelValidationError(
                onlineDatasourceRelationDto, Default.class, AddGroup.class);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        OnlineDatasourceRelation onlineDatasourceRelation =
                MyModelUtil.copyTo(onlineDatasourceRelationDto, OnlineDatasourceRelation.class);
        OnlineDatasource onlineDatasource =
                onlineDatasourceService.getById(onlineDatasourceRelationDto.getDatasourceId());
        if (onlineDatasource == null) {
            errorMessage = "数据验证失败，关联的数据源Id不存在！";
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        OnlineDblink onlineDblink = onlineDblinkService.getById(onlineDatasource.getDblinkId());
        SqlTable slaveTable = onlineDblinkService.getDblinkTable(
                onlineDblink, onlineDatasourceRelationDto.getSlaveTableName());
        if (slaveTable == null) {
            errorMessage = "数据验证失败，指定的数据表不存在！";
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        SqlTableColumn slaveColumn = null;
        for (SqlTableColumn column : slaveTable.getColumnList()) {
            if (column.getColumnName().equals(onlineDatasourceRelationDto.getSlaveColumnName())) {
                slaveColumn = column;
                break;
            }
        }
        if (slaveColumn == null) {
            errorMessage = "数据验证失败，指定的数据表字段 [" + onlineDatasourceRelationDto.getSlaveColumnName() + "] 不存在！";
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult =
                onlineDatasourceRelationService.verifyRelatedData(onlineDatasourceRelation, null);
        if (!callResult.isSuccess()) {
            errorMessage = callResult.getErrorMessage();
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        onlineDatasourceRelation = onlineDatasourceRelationService.saveNew(onlineDatasourceRelation, slaveTable, slaveColumn);
        return Result.succeed(onlineDatasourceRelation.getRelationId());
    }

    /**
     * 更新数据关联数据。
     *
     * @param onlineDatasourceRelationDto 更新对象。
     * @return 应答结果对象。
     */
    @PostMapping("/update")
    public Result<Void> update(@MyRequestBody OnlineDatasourceRelationDto onlineDatasourceRelationDto) {
        String errorMessage = ValidateUtil.getModelValidationError(
                onlineDatasourceRelationDto, Default.class, UpdateGroup.class);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        OnlineDatasourceRelation onlineDatasourceRelation =
                MyModelUtil.copyTo(onlineDatasourceRelationDto, OnlineDatasourceRelation.class);
        OnlineDatasourceRelation originalOnlineDatasourceRelation =
                onlineDatasourceRelationService.getById(onlineDatasourceRelation.getRelationId());
        if (originalOnlineDatasourceRelation == null) {
            errorMessage = "数据验证失败，当前数据源关联并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (!onlineDatasourceRelationDto.getRelationType().equals(originalOnlineDatasourceRelation.getRelationType())) {
            errorMessage = "数据验证失败，不能修改关联类型！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (!onlineDatasourceRelationDto.getSlaveTableId().equals(originalOnlineDatasourceRelation.getSlaveTableId())) {
            errorMessage = "数据验证失败，不能修改从表Id！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (!onlineDatasourceRelationDto.getDatasourceId().equals(originalOnlineDatasourceRelation.getDatasourceId())) {
            errorMessage = "数据验证失败，不能修改数据源Id！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult = onlineDatasourceRelationService
                .verifyRelatedData(onlineDatasourceRelation, originalOnlineDatasourceRelation);
        if (!callResult.isSuccess()) {
            errorMessage = callResult.getErrorMessage();
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        if (!onlineDatasourceRelationService.update(onlineDatasourceRelation, originalOnlineDatasourceRelation)) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.succeed();
    }

    /**
     * 删除数据关联数据。
     *
     * @param relationId 删除对象主键Id。
     * @return 应答结果对象。
     */
    @PostMapping("/delete")
    public Result<Void> delete(@MyRequestBody Long relationId) {
        String errorMessage;
        if (ObjectUtil.isBlankOrNull(relationId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        // 验证关联Id的数据合法性
        OnlineDatasourceRelation originalOnlineDatasourceRelation = onlineDatasourceRelationService.getById(relationId);
        if (originalOnlineDatasourceRelation == null) {
            errorMessage = "数据验证失败，当前数据源关联并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        OnlineVirtualColumn virtualColumnFilter = new OnlineVirtualColumn();
        virtualColumnFilter.setRelationId(relationId);
        List<OnlineVirtualColumn> virtualColumnList =
                onlineVirtualColumnService.getOnlineVirtualColumnList(virtualColumnFilter, null);
        if (CollUtil.isNotEmpty(virtualColumnList)) {
            OnlineVirtualColumn virtualColumn = virtualColumnList.get(0);
            errorMessage = "数据验证失败，数据源关联正在被虚拟字段 [" + virtualColumn.getColumnPrompt() + "] 使用，不能被删除！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        List<OnlineForm> formList =
                onlineFormService.getOnlineFormListByTableId(originalOnlineDatasourceRelation.getSlaveTableId());
        if (CollUtil.isNotEmpty(formList)) {
            errorMessage = "数据验证失败，当前数据源关联正在被 [" + formList.get(0).getFormName() + "] 表单占用，请先删除关联数据！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (!onlineDatasourceRelationService.remove(relationId)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        return Result.succeed();
    }

    /**
     * 列出符合过滤条件的数据关联列表。
     *
     * @param onlineDatasourceRelationDtoFilter 过滤对象。
     * @param orderParam                        排序参数。
     * @param pageParam 分                      页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public Result<Pagination<OnlineDatasourceRelationVo>> list(
            @MyRequestBody OnlineDatasourceRelationDto onlineDatasourceRelationDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        OnlineDatasourceRelation onlineDatasourceRelationFilter =
                MyModelUtil.copyTo(onlineDatasourceRelationDtoFilter, OnlineDatasourceRelation.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, OnlineDatasourceRelation.class);
        List<OnlineDatasourceRelation> onlineDatasourceRelationList =
                onlineDatasourceRelationService.getOnlineDatasourceRelationListWithRelation(onlineDatasourceRelationFilter, orderBy);
        return Result.succeed(MyPageUtil.makeResponseData(onlineDatasourceRelationList, OnlineDatasourceRelation.INSTANCE));
    }

    /**
     * 查看指定数据关联对象详情。
     *
     * @param relationId 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public Result<OnlineDatasourceRelationVo> view(@RequestParam Long relationId) {
        if (ObjectUtil.isBlankOrNull(relationId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        OnlineDatasourceRelation onlineDatasourceRelation =
                onlineDatasourceRelationService.getByIdWithRelation(relationId, MyRelationParam.full());
        if (onlineDatasourceRelation == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        OnlineDatasourceRelationVo onlineDatasourceRelationVo =
                OnlineDatasourceRelation.INSTANCE.fromModel(onlineDatasourceRelation);
        return Result.succeed(onlineDatasourceRelationVo);
    }
}
