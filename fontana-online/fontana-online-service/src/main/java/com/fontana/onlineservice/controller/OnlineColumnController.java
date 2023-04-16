package com.fontana.onlineservice.controller;

import cn.hutool.core.collection.CollUtil;
import cn.jimmyshi.beanquery.BeanQuery;
import com.fontana.base.annotation.MyRequestBody;
import com.fontana.base.result.CallResult;
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
import com.fontana.onlineapi.dto.OnlineColumnDto;
import com.fontana.onlineapi.dto.OnlineColumnRuleDto;
import com.fontana.onlineapi.dto.OnlineRuleDto;
import com.fontana.onlineapi.vo.OnlineColumnRuleVo;
import com.fontana.onlineapi.vo.OnlineColumnVo;
import com.fontana.onlineapi.vo.OnlineRuleVo;
import com.fontana.onlineservice.entity.*;
import com.fontana.onlineservice.object.SqlTableColumn;
import com.fontana.onlineservice.service.*;
import com.fontana.util.lang.ObjectUtil;
import com.github.pagehelper.page.PageMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 字段数据操作控制器类。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Slf4j
@RestController
@RequestMapping("${fontana.common-online.urlPrefix}/onlineColumn")
public class OnlineColumnController {

    @Autowired
    private OnlineColumnService onlineColumnService;
    @Autowired
    private OnlineTableService onlineTableService;
    @Autowired
    private OnlineVirtualColumnService onlineVirtualColumnService;
    @Autowired
    private OnlineDblinkService onlineDblinkService;
    @Autowired
    private OnlineRuleService onlineRuleService;

    /**
     * 根据数据库表字段信息，在指定在线表中添加在线表字段对象。
     *
     * @param dblinkId   数据库链接Id。
     * @param tableName  数据库表名称。
     * @param columnName 数据库表字段名。
     * @param tableId    目的表Id。
     * @return 应答结果对象。
     */
    @PostMapping("/add")
    public Result<Void> add(
            @MyRequestBody Long dblinkId,
            @MyRequestBody String tableName,
            @MyRequestBody String columnName,
            @MyRequestBody Long tableId) {
        OnlineDblink dblink = onlineDblinkService.getById(dblinkId);
        if (dblink == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        String errorMsg;
        SqlTableColumn sqlTableColumn = onlineDblinkService.getDblinkTableColumn(dblink, tableName, columnName);
        if (sqlTableColumn == null) {
            errorMsg = "数据验证失败，指定的数据表字段不存在！";
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMsg);
        }
        if (!onlineTableService.existId(tableId)) {
            errorMsg = "数据验证失败，指定的数据表Id不存在！";
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMsg);
        }
        onlineColumnService.saveNewList(CollUtil.newLinkedList(sqlTableColumn), tableId);
        return Result.succeed();
    }

    /**
     * 更新字段数据数据。
     *
     * @param onlineColumnDto 更新对象。
     * @return 应答结果对象。
     */
    @PostMapping("/update")
    public Result<Void> update(@MyRequestBody OnlineColumnDto onlineColumnDto) {
        String errorMessage = ValidateUtil.getModelValidationError(onlineColumnDto, Default.class, UpdateGroup.class);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        OnlineColumn onlineColumn = MyModelUtil.copyTo(onlineColumnDto, OnlineColumn.class);
        OnlineColumn originalOnlineColumn = onlineColumnService.getById(onlineColumn.getColumnId());
        if (originalOnlineColumn == null) {
            errorMessage = "数据验证失败，当前在线表字段并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult = onlineColumnService.verifyRelatedData(onlineColumn, originalOnlineColumn);
        if (!callResult.isSuccess()) {
            errorMessage = callResult.getErrorMessage();
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        if (!onlineColumnService.update(onlineColumn, originalOnlineColumn)) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.succeed();
    }

    /**
     * 删除字段数据数据。
     *
     * @param columnId 删除对象主键Id。
     * @return 应答结果对象。
     */
    @PostMapping("/delete")
    public Result<Void> delete(@MyRequestBody Long columnId) {
        String errorMessage;
        if (ObjectUtil.isBlankOrNull(columnId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        // 验证关联Id的数据合法性
        OnlineColumn originalOnlineColumn = onlineColumnService.getById(columnId);
        if (originalOnlineColumn == null) {
            errorMessage = "数据验证失败，当前在线表字段并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        OnlineVirtualColumn virtualColumnFilter = new OnlineVirtualColumn();
        virtualColumnFilter.setAggregationColumnId(columnId);
        List<OnlineVirtualColumn> virtualColumnList =
                onlineVirtualColumnService.getOnlineVirtualColumnList(virtualColumnFilter, null);
        if (CollUtil.isNotEmpty(virtualColumnList)) {
            OnlineVirtualColumn virtualColumn = virtualColumnList.get(0);
            errorMessage = "数据验证失败，数据源关联正在被虚拟字段 [" + virtualColumn.getColumnPrompt() + "] 使用，不能被删除！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (!onlineColumnService.remove(originalOnlineColumn.getTableId(), columnId)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        return Result.succeed();
    }

    /**
     * 列出符合过滤条件的字段数据列表。
     *
     * @param onlineColumnDtoFilter 过滤对象。
     * @param orderParam            排序参数。
     * @param pageParam             分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public Result<Pagination<OnlineColumnVo>> list(
            @MyRequestBody OnlineColumnDto onlineColumnDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        OnlineColumn onlineColumnFilter = MyModelUtil.copyTo(onlineColumnDtoFilter, OnlineColumn.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, OnlineColumn.class);
        List<OnlineColumn> onlineColumnList =
                onlineColumnService.getOnlineColumnListWithRelation(onlineColumnFilter, orderBy);
        return Result.succeed(MyPageUtil.makeResponseData(onlineColumnList, OnlineColumn.INSTANCE));
    }

    /**
     * 查看指定字段数据对象详情。
     *
     * @param columnId 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public Result<OnlineColumnVo> view(@RequestParam Long columnId) {
        if (ObjectUtil.isBlankOrNull(columnId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        OnlineColumn onlineColumn = onlineColumnService.getByIdWithRelation(columnId, MyRelationParam.full());
        if (onlineColumn == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        OnlineColumnVo onlineColumnVo = OnlineColumn.INSTANCE.fromModel(onlineColumn);
        return Result.succeed(onlineColumnVo);
    }

    /**
     * 将数据库中的表字段信息刷新到已经导入的在线表字段信息。
     *
     * @param dblinkId   数据库链接Id。
     * @param tableName  数据库表名称。
     * @param columnName 数据库表字段名。
     * @param columnId   被刷新的在线字段Id。
     * @return 应答结果对象。
     */
    @PostMapping("/refresh")
    public Result<Void> refresh(
            @MyRequestBody Long dblinkId,
            @MyRequestBody String tableName,
            @MyRequestBody String columnName,
            @MyRequestBody Long columnId) {
        OnlineDblink dblink = onlineDblinkService.getById(dblinkId);
        if (dblink == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        String errorMsg;
        SqlTableColumn sqlTableColumn = onlineDblinkService.getDblinkTableColumn(dblink, tableName, columnName);
        if (sqlTableColumn == null) {
            errorMsg = "数据验证失败，指定的数据表字段不存在！";
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMsg);
        }
        OnlineColumn onlineColumn = onlineColumnService.getById(columnId);
        if (onlineColumn == null) {
            errorMsg = "数据验证失败，指定的在线表字段Id不存在！";
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMsg);
        }
        onlineColumnService.refresh(sqlTableColumn, onlineColumn);
        return Result.succeed();
    }

    /**
     * 列出不与指定字段数据存在多对多关系的 [验证规则] 列表数据。通常用于查看添加新 [验证规则] 对象的候选列表。
     *
     * @param columnId            主表关联字段。
     * @param onlineRuleDtoFilter [验证规则] 过滤对象。
     * @param orderParam          排序参数。
     * @param pageParam           分页参数。
     * @return 应答结果对象，返回符合条件的数据列表。
     */
    @PostMapping("/listNotInOnlineColumnRule")
    public Result<Pagination<OnlineRuleVo>> listNotInOnlineColumnRule(
            @MyRequestBody Long columnId,
            @MyRequestBody OnlineRuleDto onlineRuleDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        Result<Void> verifyResult = this.doOnlineColumnRuleVerify(columnId);
        if (!verifyResult.isSuccess()) {
            return Result.failed(verifyResult);
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        OnlineRule filter = MyModelUtil.copyTo(onlineRuleDtoFilter, OnlineRule.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, OnlineRule.class);
        List<OnlineRule> onlineRuleList =
                onlineRuleService.getNotInOnlineRuleListByColumnId(columnId, filter, orderBy);
        return Result.succeed(MyPageUtil.makeResponseData(onlineRuleList, OnlineRule.INSTANCE));
    }

    /**
     * 列出与指定字段数据存在多对多关系的 [验证规则] 列表数据。
     *
     * @param columnId            主表关联字段。
     * @param onlineRuleDtoFilter [验证规则] 过滤对象。
     * @param orderParam          排序参数。
     * @param pageParam           分页参数。
     * @return 应答结果对象，返回符合条件的数据列表。
     */
    @PostMapping("/listOnlineColumnRule")
    public Result<Pagination<OnlineRuleVo>> listOnlineColumnRule(
            @MyRequestBody Long columnId,
            @MyRequestBody OnlineRuleDto onlineRuleDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        Result<Void> verifyResult = this.doOnlineColumnRuleVerify(columnId);
        if (!verifyResult.isSuccess()) {
            return Result.failed(verifyResult);
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        OnlineRule filter = MyModelUtil.copyTo(onlineRuleDtoFilter, OnlineRule.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, OnlineRule.class);
        List<OnlineRule> onlineRuleList =
                onlineRuleService.getOnlineRuleListByColumnId(columnId, filter, orderBy);
        return Result.succeed(MyPageUtil.makeResponseData(onlineRuleList, OnlineRule.INSTANCE));
    }

    private Result<Void> doOnlineColumnRuleVerify(Long columnId) {
        if (ObjectUtil.isBlankOrNull(columnId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        if (!onlineColumnService.existId(columnId)) {
            return Result.failed(ResultCode.INVALID_RELATED_RECORD_ID);
        }
        return Result.succeed();
    }

    /**
     * 批量添加字段数据和 [验证规则] 对象的多对多关联关系数据。
     *
     * @param columnId                主表主键Id。
     * @param onlineColumnRuleDtoList 关联对象列表。
     * @return 应答结果对象。
     */
    @PostMapping("/addOnlineColumnRule")
    public Result<Void> addOnlineColumnRule(
            @MyRequestBody Long columnId,
            @MyRequestBody(elementType = OnlineColumnRuleDto.class) List<OnlineColumnRuleDto> onlineColumnRuleDtoList) {
        if (ObjectUtil.isAnyBlankOrNull(columnId, onlineColumnRuleDtoList)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        for (OnlineColumnRuleDto onlineColumnRule : onlineColumnRuleDtoList) {
            String errorMessage = ValidateUtil.getModelValidationError(onlineColumnRule);
            if (errorMessage != null) {
                return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
            }
        }
        Set<Long> ruleIdSet =
                onlineColumnRuleDtoList.stream().map(OnlineColumnRuleDto::getRuleId).collect(Collectors.toSet());
        if (!onlineColumnService.existId(columnId)
                || !onlineRuleService.existUniqueKeyList("ruleId", ruleIdSet)) {
            return Result.failed(ResultCode.INVALID_RELATED_RECORD_ID);
        }
        List<OnlineColumnRule> onlineColumnRuleList =
                MyModelUtil.copyCollectionTo(onlineColumnRuleDtoList, OnlineColumnRule.class);
        onlineColumnService.addOnlineColumnRuleList(onlineColumnRuleList, columnId);
        return Result.succeed();
    }

    /**
     * 更新指定字段数据和指定 [验证规则] 的多对多关联数据。
     *
     * @param onlineColumnRuleDto 对多对中间表对象。
     * @return 应答结果对象。
     */
    @PostMapping("/updateOnlineColumnRule")
    public Result<Void> updateOnlineColumnRule(
            @MyRequestBody OnlineColumnRuleDto onlineColumnRuleDto) {
        String errorMessage = ValidateUtil.getModelValidationError(onlineColumnRuleDto);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        OnlineColumnRule onlineColumnRule = MyModelUtil.copyTo(onlineColumnRuleDto, OnlineColumnRule.class);
        if (!onlineColumnService.updateOnlineColumnRule(onlineColumnRule)) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.succeed();
    }

    /**
     * 显示字段数据和指定 [验证规则] 的多对多关联详情数据。
     *
     * @param columnId 主表主键Id。
     * @param ruleId   从表主键Id。
     * @return 应答结果对象，包括中间表详情。
     */
    @GetMapping("/viewOnlineColumnRule")
    public Result<OnlineColumnRuleVo> viewOnlineColumnRule(
            @RequestParam Long columnId, @RequestParam Long ruleId) {
        if (ObjectUtil.isAnyBlankOrNull(columnId, ruleId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        OnlineColumnRule onlineColumnRule = onlineColumnService.getOnlineColumnRule(columnId, ruleId);
        if (onlineColumnRule == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        OnlineColumnRuleVo onlineColumnRuleVo = MyModelUtil.copyTo(onlineColumnRule, OnlineColumnRuleVo.class);
        return Result.succeed(onlineColumnRuleVo);
    }

    /**
     * 移除指定字段数据和指定 [验证规则] 的多对多关联关系。
     *
     * @param columnId 主表主键Id。
     * @param ruleId   从表主键Id。
     * @return 应答结果对象。
     */
    @PostMapping("/deleteOnlineColumnRule")
    public Result<Void> deleteOnlineColumnRule(
            @MyRequestBody Long columnId, @MyRequestBody Long ruleId) {
        if (ObjectUtil.isAnyBlankOrNull(columnId, ruleId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        if (!onlineColumnService.removeOnlineColumnRule(columnId, ruleId)) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.succeed();
    }

    /**
     * 以字典形式返回全部字段数据数据集合。字典的键值为[columnId, columnName]。
     * 白名单接口，登录用户均可访问。
     *
     * @param filter 过滤对象。
     * @return 应答结果对象，包含的数据为 List<Map<String, String>>，map中包含两条记录，key的值分别是id和name，value对应具体数据。
     */
    @GetMapping("/listDict")
    public Result<List<Map<String, Object>>> listDict(OnlineColumn filter) {
        List<OnlineColumn> resultList = onlineColumnService.getListByFilter(filter);
        return Result.succeed(BeanQuery.select(
                "columnId as id", "columnName as name").executeFrom(resultList));
    }
}
