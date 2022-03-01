package com.bluetron.nb.common.onlineservice.controller;

import com.bluetron.nb.common.base.annotation.MyRequestBody;
import com.bluetron.nb.common.base.result.CallResult;
import com.bluetron.nb.common.base.result.Pagination;
import com.bluetron.nb.common.base.result.Result;
import com.bluetron.nb.common.base.result.ResultCode;
import com.bluetron.nb.common.base.validate.UpdateGroup;
import com.bluetron.nb.common.util.validate.ValidateUtil;
import com.bluetron.nb.common.db.object.MyOrderParam;
import com.bluetron.nb.common.db.object.MyPageParam;
import com.bluetron.nb.common.db.object.MyRelationParam;
import com.bluetron.nb.common.db.util.MyModelUtil;
import com.bluetron.nb.common.db.util.MyPageUtil;
import com.bluetron.nb.common.onlineapi.dict.VirtualType;
import com.bluetron.nb.common.onlineapi.dto.OnlineVirtualColumnDto;
import com.bluetron.nb.common.onlineapi.vo.OnlineVirtualColumnVo;
import com.bluetron.nb.common.onlineservice.entity.OnlineVirtualColumn;
import com.bluetron.nb.common.onlineservice.service.OnlineVirtualColumnService;
import com.bluetron.nb.common.util.lang.ObjectUtil;
import com.github.pagehelper.page.PageMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/**
 * 虚拟字段操作控制器类。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Slf4j
@RestController
@RequestMapping("${bluetron-nb-common.common-online.urlPrefix}/onlineVirtualColumn")
public class OnlineVirtualColumnController {

    @Autowired
    private OnlineVirtualColumnService onlineVirtualColumnService;

    /**
     * 新增虚拟字段数据。
     *
     * @param onlineVirtualColumnDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @PostMapping("/add")
    public Result<Long> add(@MyRequestBody OnlineVirtualColumnDto onlineVirtualColumnDto) {
        String errorMessage = ValidateUtil.getModelValidationError(onlineVirtualColumnDto);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        OnlineVirtualColumn onlineVirtualColumn =
                MyModelUtil.copyTo(onlineVirtualColumnDto, OnlineVirtualColumn.class);
        Result<Void> verifyResult = this.doVerify(onlineVirtualColumn, null);
        if (!verifyResult.isSuccess()) {
            return Result.failed(verifyResult);
        }
        onlineVirtualColumn = onlineVirtualColumnService.saveNew(onlineVirtualColumn);
        return Result.succeed(onlineVirtualColumn.getVirtualColumnId());
    }

    /**
     * 更新虚拟字段数据。
     *
     * @param onlineVirtualColumnDto 更新对象。
     * @return 应答结果对象。
     */
    @PostMapping("/update")
    public Result<Void> update(@MyRequestBody OnlineVirtualColumnDto onlineVirtualColumnDto) {
        String errorMessage = ValidateUtil.getModelValidationError(
                onlineVirtualColumnDto, Default.class, UpdateGroup.class);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        OnlineVirtualColumn onlineVirtualColumn =
                MyModelUtil.copyTo(onlineVirtualColumnDto, OnlineVirtualColumn.class);
        OnlineVirtualColumn originalOnlineVirtualColumn =
                onlineVirtualColumnService.getById(onlineVirtualColumn.getVirtualColumnId());
        if (originalOnlineVirtualColumn == null) {
            errorMessage = "数据验证失败，当前虚拟字段并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        Result<Void> verifyResult = this.doVerify(onlineVirtualColumn, originalOnlineVirtualColumn);
        if (!verifyResult.isSuccess()) {
            return Result.failed(verifyResult);
        }
        if (!onlineVirtualColumnService.update(onlineVirtualColumn, originalOnlineVirtualColumn)) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.succeed();
    }

    /**
     * 删除虚拟字段数据。
     *
     * @param virtualColumnId 删除对象主键Id。
     * @return 应答结果对象。
     */
    @PostMapping("/delete")
    public Result<Void> delete(@MyRequestBody Long virtualColumnId) {
        String errorMessage;
        if (ObjectUtil.isBlankOrNull(virtualColumnId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        // 验证关联Id的数据合法性
        OnlineVirtualColumn originalOnlineVirtualColumn = onlineVirtualColumnService.getById(virtualColumnId);
        if (originalOnlineVirtualColumn == null) {
            errorMessage = "数据验证失败，当前虚拟字段并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (!onlineVirtualColumnService.remove(virtualColumnId)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        return Result.succeed();
    }

    /**
     * 列出符合过滤条件的虚拟字段列表。
     *
     * @param onlineVirtualColumnDtoFilter 过滤对象。
     * @param orderParam                   排序参数。
     * @param pageParam                    分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public Result<Pagination<OnlineVirtualColumnVo>> list(
            @MyRequestBody OnlineVirtualColumnDto onlineVirtualColumnDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        OnlineVirtualColumn onlineVirtualColumnFilter =
                MyModelUtil.copyTo(onlineVirtualColumnDtoFilter, OnlineVirtualColumn.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, OnlineVirtualColumn.class);
        List<OnlineVirtualColumn> onlineVirtualColumnList =
                onlineVirtualColumnService.getOnlineVirtualColumnListWithRelation(onlineVirtualColumnFilter, orderBy);
        Pagination<OnlineVirtualColumnVo> pageData =
                MyPageUtil.makeResponseData(onlineVirtualColumnList, OnlineVirtualColumn.INSTANCE);
        return Result.succeed(pageData);
    }

    /**
     * 查看指定虚拟字段对象详情。
     *
     * @param virtualColumnId 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public Result<OnlineVirtualColumnVo> view(@RequestParam Long virtualColumnId) {
        if (ObjectUtil.isBlankOrNull(virtualColumnId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        OnlineVirtualColumn onlineVirtualColumn =
                onlineVirtualColumnService.getByIdWithRelation(virtualColumnId, MyRelationParam.full());
        if (onlineVirtualColumn == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        OnlineVirtualColumnVo onlineVirtualColumnVo =
                OnlineVirtualColumn.INSTANCE.fromModel(onlineVirtualColumn);
        return Result.succeed(onlineVirtualColumnVo);
    }

    private Result<Void> doVerify(
            OnlineVirtualColumn virtualColumn, OnlineVirtualColumn originalVirtualColumn) {
        if (!virtualColumn.getVirtualType().equals(VirtualType.AGGREGATION)) {
            return Result.succeed();
        }
        if (ObjectUtil.isAnyBlankOrNull(
                virtualColumn.getAggregationColumnId(),
                virtualColumn.getAggregationTableId(),
                virtualColumn.getDatasourceId(),
                virtualColumn.getRelationId(),
                virtualColumn.getAggregationType())) {
            String errorMessage = "数据验证失败，数据源、关联关系、聚合表、聚合字段和聚合类型，均不能为空！";
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        CallResult verifyResult = onlineVirtualColumnService.verifyRelatedData(virtualColumn, null);
        if (!verifyResult.isSuccess()) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, verifyResult.getErrorMessage());
        }
        return Result.succeed();
    }
}
