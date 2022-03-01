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
import com.bluetron.nb.common.onlineapi.dto.OnlineDictDto;
import com.bluetron.nb.common.onlineapi.vo.OnlineDictVo;
import com.bluetron.nb.common.onlineservice.entity.OnlineDict;
import com.bluetron.nb.common.onlineservice.service.OnlineDictService;
import com.bluetron.nb.common.util.lang.ObjectUtil;
import com.github.pagehelper.page.PageMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/**
 * 在线表单字典操作控制器类。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Slf4j
@RestController
@RequestMapping("${bluetron-nb-common.common-online.urlPrefix}/onlineDict")
public class OnlineDictController {

    @Autowired
    private OnlineDictService onlineDictService;

    /**
     * 新增在线表单字典数据。
     *
     * @param onlineDictDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @PostMapping("/add")
    public Result<Long> add(@MyRequestBody OnlineDictDto onlineDictDto) {
        String errorMessage = ValidateUtil.getModelValidationError(onlineDictDto);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        OnlineDict onlineDict = MyModelUtil.copyTo(onlineDictDto, OnlineDict.class);
        // 验证关联Id的数据合法性
        CallResult callResult = onlineDictService.verifyRelatedData(onlineDict, null);
        if (!callResult.isSuccess()) {
            errorMessage = callResult.getErrorMessage();
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        onlineDict = onlineDictService.saveNew(onlineDict);
        return Result.succeed(onlineDict.getDictId());
    }

    /**
     * 更新在线表单字典数据。
     *
     * @param onlineDictDto 更新对象。
     * @return 应答结果对象。
     */
    @PostMapping("/update")
    public Result<Void> update(@MyRequestBody OnlineDictDto onlineDictDto) {
        String errorMessage = ValidateUtil.getModelValidationError(onlineDictDto, Default.class, UpdateGroup.class);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        OnlineDict onlineDict = MyModelUtil.copyTo(onlineDictDto, OnlineDict.class);
        OnlineDict originalOnlineDict = onlineDictService.getById(onlineDict.getDictId());
        if (originalOnlineDict == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前在线字典并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult = onlineDictService.verifyRelatedData(onlineDict, originalOnlineDict);
        if (!callResult.isSuccess()) {
            errorMessage = callResult.getErrorMessage();
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        if (!onlineDictService.update(onlineDict, originalOnlineDict)) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.succeed();
    }

    /**
     * 删除在线表单字典数据。
     *
     * @param dictId 删除对象主键Id。
     * @return 应答结果对象。
     */
    @PostMapping("/delete")
    public Result<Void> delete(@MyRequestBody Long dictId) {
        String errorMessage;
        if (ObjectUtil.isBlankOrNull(dictId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        // 验证关联Id的数据合法性
        OnlineDict originalOnlineDict = onlineDictService.getById(dictId);
        if (originalOnlineDict == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前在线字典并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (!onlineDictService.remove(dictId)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        return Result.succeed();
    }

    /**
     * 列出符合过滤条件的在线表单字典列表。
     *
     * @param onlineDictDtoFilter 过滤对象。
     * @param orderParam          排序参数。
     * @param pageParam           分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public Result<Pagination<OnlineDictVo>> list(
            @MyRequestBody OnlineDictDto onlineDictDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        OnlineDict onlineDictFilter = MyModelUtil.copyTo(onlineDictDtoFilter, OnlineDict.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, OnlineDict.class);
        List<OnlineDict> onlineDictList = onlineDictService.getOnlineDictListWithRelation(onlineDictFilter, orderBy);
        return Result.succeed(MyPageUtil.makeResponseData(onlineDictList, OnlineDict.INSTANCE));
    }

    /**
     * 查看指定在线表单字典对象详情。
     *
     * @param dictId 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public Result<OnlineDictVo> view(@RequestParam Long dictId) {
        if (ObjectUtil.isBlankOrNull(dictId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        OnlineDict onlineDict = onlineDictService.getByIdWithRelation(dictId, MyRelationParam.full());
        if (onlineDict == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        OnlineDictVo onlineDictVo = OnlineDict.INSTANCE.fromModel(onlineDict);
        return Result.succeed(onlineDictVo);
    }
}
