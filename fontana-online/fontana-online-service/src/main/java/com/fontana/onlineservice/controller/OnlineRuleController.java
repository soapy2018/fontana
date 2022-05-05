package com.fontana.onlineservice.controller;

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
import com.fontana.onlineapi.dto.OnlineRuleDto;
import com.fontana.onlineapi.vo.OnlineRuleVo;
import com.fontana.onlineservice.entity.OnlineRule;
import com.fontana.onlineservice.service.OnlineRuleService;
import com.fontana.util.lang.ObjectUtil;
import com.github.pagehelper.page.PageMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/**
 * 验证规则操作控制器类。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Slf4j
@RestController
@RequestMapping("${fontana.common-online.urlPrefix}/onlineRule")
public class OnlineRuleController {

    @Autowired
    private OnlineRuleService onlineRuleService;

    /**
     * 新增验证规则数据。
     *
     * @param onlineRuleDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @PostMapping("/add")
    public Result<Long> add(@MyRequestBody OnlineRuleDto onlineRuleDto) {
        String errorMessage = ValidateUtil.getModelValidationError(onlineRuleDto);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        OnlineRule onlineRule = MyModelUtil.copyTo(onlineRuleDto, OnlineRule.class);
        onlineRule = onlineRuleService.saveNew(onlineRule);
        return Result.succeed(onlineRule.getRuleId());
    }

    /**
     * 更新验证规则数据。
     *
     * @param onlineRuleDto 更新对象。
     * @return 应答结果对象。
     */
    @PostMapping("/update")
    public Result<Void> update(@MyRequestBody OnlineRuleDto onlineRuleDto) {
        String errorMessage = ValidateUtil.getModelValidationError(onlineRuleDto, Default.class, UpdateGroup.class);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        OnlineRule onlineRule = MyModelUtil.copyTo(onlineRuleDto, OnlineRule.class);
        OnlineRule originalOnlineRule = onlineRuleService.getById(onlineRule.getRuleId());
        if (originalOnlineRule == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前在线字段规则并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (!onlineRuleService.update(onlineRule, originalOnlineRule)) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.succeed();
    }

    /**
     * 删除验证规则数据。
     *
     * @param ruleId 删除对象主键Id。
     * @return 应答结果对象。
     */
    @PostMapping("/delete")
    public Result<Void> delete(@MyRequestBody Long ruleId) {
        String errorMessage;
        if (ObjectUtil.isBlankOrNull(ruleId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        // 验证关联Id的数据合法性
        OnlineRule originalOnlineRule = onlineRuleService.getById(ruleId);
        if (originalOnlineRule == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前在线字段规则并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (!onlineRuleService.remove(ruleId)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        return Result.succeed();
    }

    /**
     * 列出符合过滤条件的验证规则列表。
     *
     * @param onlineRuleDtoFilter 过滤对象。
     * @param orderParam          排序参数。
     * @param pageParam           分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public Result<Pagination<OnlineRuleVo>> list(
            @MyRequestBody OnlineRuleDto onlineRuleDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        OnlineRule onlineRuleFilter = MyModelUtil.copyTo(onlineRuleDtoFilter, OnlineRule.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, OnlineRule.class);
        List<OnlineRule> onlineRuleList = onlineRuleService.getOnlineRuleListWithRelation(onlineRuleFilter, orderBy);
        return Result.succeed(MyPageUtil.makeResponseData(onlineRuleList, OnlineRule.INSTANCE));
    }

    /**
     * 查看指定验证规则对象详情。
     *
     * @param ruleId 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public Result<OnlineRuleVo> view(@RequestParam Long ruleId) {
        if (ObjectUtil.isBlankOrNull(ruleId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        OnlineRule onlineRule = onlineRuleService.getByIdWithRelation(ruleId, MyRelationParam.full());
        if (onlineRule == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        OnlineRuleVo onlineRuleVo = OnlineRule.INSTANCE.fromModel(onlineRule);
        return Result.succeed(onlineRuleVo);
    }
}
