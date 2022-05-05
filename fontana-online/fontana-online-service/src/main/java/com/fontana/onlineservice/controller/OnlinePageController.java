package com.fontana.onlineservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.fontana.base.annotation.MyRequestBody;
import com.fontana.base.result.Pagination;
import com.fontana.base.result.ResultCode;
import com.fontana.base.validate.UpdateGroup;
import com.fontana.util.validate.ValidateUtil;
import com.fontana.db.object.MyOrderParam;
import com.fontana.db.object.MyPageParam;
import com.fontana.db.object.MyRelationParam;
import com.fontana.db.util.MyModelUtil;
import com.fontana.db.util.MyPageUtil;
import com.fontana.onlineapi.dict.PageStatus;
import com.fontana.onlineapi.dto.OnlineDatasourceDto;
import com.fontana.onlineapi.dto.OnlinePageDatasourceDto;
import com.fontana.onlineapi.dto.OnlinePageDto;
import com.fontana.onlineapi.vo.OnlineDatasourceVo;
import com.fontana.onlineapi.vo.OnlinePageDatasourceVo;
import com.fontana.onlineapi.vo.OnlinePageVo;
import com.fontana.onlineservice.entity.OnlineDatasource;
import com.fontana.onlineservice.entity.OnlinePage;
import com.fontana.onlineservice.entity.OnlinePageDatasource;
import com.fontana.onlineservice.service.OnlineDatasourceService;
import com.fontana.onlineservice.service.OnlineFormService;
import com.fontana.onlineservice.service.OnlinePageService;
import com.fontana.base.result.Result;
import com.fontana.util.lang.ObjectUtil;
import com.github.pagehelper.page.PageMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 在线表单页面操作控制器类。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Slf4j
@RestController
@RequestMapping("${fontana.common-online.urlPrefix}/onlinePage")
public class OnlinePageController {

    @Autowired
    private OnlinePageService onlinePageService;
    @Autowired
    private OnlineFormService onlineFormService;
    @Autowired
    private OnlineDatasourceService onlineDatasourceService;

    /**
     * 新增在线表单页面数据。
     *
     * @param onlinePageDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @PostMapping("/add")
    public Result<Long> add(@MyRequestBody OnlinePageDto onlinePageDto) {
        String errorMessage = ValidateUtil.getModelValidationError(onlinePageDto);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        OnlinePage onlinePage = MyModelUtil.copyTo(onlinePageDto, OnlinePage.class);
        onlinePage = onlinePageService.saveNew(onlinePage);
        return Result.succeed(onlinePage.getPageId());
    }

    /**
     * 更新在线表单页面数据。
     *
     * @param onlinePageDto 更新对象。
     * @return 应答结果对象。
     */
    @PostMapping("/update")
    public Result<Void> update(@MyRequestBody OnlinePageDto onlinePageDto) {
        String errorMessage = ValidateUtil.getModelValidationError(onlinePageDto, Default.class, UpdateGroup.class);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        OnlinePage onlinePage = MyModelUtil.copyTo(onlinePageDto, OnlinePage.class);
        OnlinePage originalOnlinePage = onlinePageService.getById(onlinePage.getPageId());
        if (originalOnlinePage == null) {
            errorMessage = "数据验证失败，当前页面对象并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (!onlinePage.getPageType().equals(originalOnlinePage.getPageType())) {
            errorMessage = "数据验证失败，页面类型不能修改！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (!onlinePageService.update(onlinePage, originalOnlinePage)) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.succeed();
    }

    /**
     * 更新在线表单页面对象的发布状态字段。
     *
     * @param pageId    待更新的页面对象主键Id。
     * @param published 发布状态。
     * @return 应答结果对象。
     */
    @PostMapping("/updatePublished")
    public Result<Void> updateStatus(
            @MyRequestBody(required = true) Long pageId,
            @MyRequestBody(required = true) Boolean published) {
        String errorMessage;
        // 验证关联Id的数据合法性
        OnlinePage originalOnlinePage = onlinePageService.getById(pageId);
        if (originalOnlinePage == null) {
            errorMessage = "数据验证失败，当前页面对象并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (!published.equals(originalOnlinePage.getPublished())) {
            if (published && !originalOnlinePage.getStatus().equals(PageStatus.FORM_DESIGN)) {
                errorMessage = "数据验证失败，当前页面状态不为 [设计] 状态，因此不能发布！";
                return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
            }
            onlinePageService.updatePublished(pageId, published);
        }
        return Result.succeed();
    }

    /**
     * 删除在线表单页面数据。
     *
     * @param pageId 删除对象主键Id。
     * @return 应答结果对象。
     */
    @PostMapping("/delete")
    public Result<Void> delete(@MyRequestBody Long pageId) {
        String errorMessage;
        if (ObjectUtil.isBlankOrNull(pageId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        // 验证关联Id的数据合法性
        OnlinePage originalOnlinePage = onlinePageService.getById(pageId);
        if (originalOnlinePage == null) {
            errorMessage = "数据验证失败，当前页面对象并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (!onlinePageService.remove(pageId)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        return Result.succeed();
    }

    /**
     * 列出符合过滤条件的在线表单页面列表。
     *
     * @param onlinePageDtoFilter 过滤对象。
     * @param orderParam          排序参数。
     * @param pageParam           分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public Result<Pagination<OnlinePageVo>> list(
            @MyRequestBody OnlinePageDto onlinePageDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        OnlinePage onlinePageFilter = MyModelUtil.copyTo(onlinePageDtoFilter, OnlinePage.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, OnlinePage.class);
        List<OnlinePage> onlinePageList = onlinePageService.getOnlinePageListWithRelation(onlinePageFilter, orderBy);
        return Result.succeed(MyPageUtil.makeResponseData(onlinePageList, OnlinePage.INSTANCE));
    }

    /**
     * 获取系统中配置的所有Page和表单的列表。
     *
     * @return 系统中配置的所有Page和表单的列表。
     */
    @PostMapping("/listAllPageAndForm")
    public Result<JSONObject> listAllPageAndForm() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageList", onlinePageService.getAllList());
        jsonObject.put("formList", onlineFormService.getAllList());
        return Result.succeed(jsonObject);
    }

    /**
     * 查看指定在线表单页面对象详情。
     *
     * @param pageId 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public Result<OnlinePageVo> view(@RequestParam Long pageId) {
        if (ObjectUtil.isBlankOrNull(pageId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        OnlinePage onlinePage = onlinePageService.getByIdWithRelation(pageId, MyRelationParam.full());
        if (onlinePage == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        OnlinePageVo onlinePageVo = OnlinePage.INSTANCE.fromModel(onlinePage);
        return Result.succeed(onlinePageVo);
    }

    /**
     * 列出不与指定在线表单页面存在多对多关系的在线数据源列表数据。通常用于查看添加新在线数据源对象的候选列表。
     *
     * @param pageId                    主表关联字段。
     * @param onlineDatasourceDtoFilter 在线数据源过滤对象。
     * @param orderParam                排序参数。
     * @param pageParam                 分页参数。
     * @return 应答结果对象，返回符合条件的数据列表。
     */
    @PostMapping("/listNotInOnlinePageDatasource")
    public Result<Pagination<OnlineDatasourceVo>> listNotInOnlinePageDatasource(
            @MyRequestBody Long pageId,
            @MyRequestBody OnlineDatasourceDto onlineDatasourceDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        Result<Void> verifyResult = this.doOnlinePageDatasourceVerify(pageId);
        if (!verifyResult.isSuccess()) {
            return Result.failed(verifyResult);
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        OnlineDatasource filter = MyModelUtil.copyTo(onlineDatasourceDtoFilter, OnlineDatasource.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, OnlineDatasource.class);
        List<OnlineDatasource> onlineDatasourceList =
                onlineDatasourceService.getNotInOnlineDatasourceListByPageId(pageId, filter, orderBy);
        return Result.succeed(MyPageUtil.makeResponseData(onlineDatasourceList, OnlineDatasource.INSTANCE));
    }

    /**
     * 列出与指定在线表单页面存在多对多关系的在线数据源列表数据。
     *
     * @param pageId                    主表关联字段。
     * @param onlineDatasourceDtoFilter 在线数据源过滤对象。
     * @param orderParam                排序参数。
     * @param pageParam                 分页参数。
     * @return 应答结果对象，返回符合条件的数据列表。
     */
    @PostMapping("/listOnlinePageDatasource")
    public Result<Pagination<OnlineDatasourceVo>> listOnlinePageDatasource(
            @MyRequestBody Long pageId,
            @MyRequestBody OnlineDatasourceDto onlineDatasourceDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        Result<Void> verifyResult = this.doOnlinePageDatasourceVerify(pageId);
        if (!verifyResult.isSuccess()) {
            return Result.failed(verifyResult);
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        OnlineDatasource filter = MyModelUtil.copyTo(onlineDatasourceDtoFilter, OnlineDatasource.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, OnlineDatasource.class);
        List<OnlineDatasource> onlineDatasourceList =
                onlineDatasourceService.getOnlineDatasourceListByPageId(pageId, filter, orderBy);
        return Result.succeed(MyPageUtil.makeResponseData(onlineDatasourceList, OnlineDatasource.INSTANCE));
    }

    /**
     * 批量添加在线表单页面和在线数据源对象的多对多关联关系数据。
     *
     * @param pageId                      主表主键Id。
     * @param onlinePageDatasourceDtoList 关联对象列表。
     * @return 应答结果对象。
     */
    @PostMapping("/addOnlinePageDatasource")
    public Result<Void> addOnlinePageDatasource(
            @MyRequestBody Long pageId,
            @MyRequestBody(value = "onlinePageDatasourceList", elementType = OnlinePageDatasourceDto.class) List<OnlinePageDatasourceDto> onlinePageDatasourceDtoList) {
        if (ObjectUtil.isAnyBlankOrNull(pageId, onlinePageDatasourceDtoList)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        for (OnlinePageDatasourceDto onlinePageDatasource : onlinePageDatasourceDtoList) {
            String errorMessage = ValidateUtil.getModelValidationError(onlinePageDatasource);
            if (errorMessage != null) {
                return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
            }
        }
        Set<Long> datasourceIdSet =
                onlinePageDatasourceDtoList.stream().map(OnlinePageDatasourceDto::getDatasourceId).collect(Collectors.toSet());
        if (!onlinePageService.existId(pageId)
                || !onlineDatasourceService.existUniqueKeyList("datasourceId", datasourceIdSet)) {
            return Result.failed(ResultCode.INVALID_RELATED_RECORD_ID);
        }
        List<OnlinePageDatasource> onlinePageDatasourceList =
                MyModelUtil.copyCollectionTo(onlinePageDatasourceDtoList, OnlinePageDatasource.class);
        onlinePageService.addOnlinePageDatasourceList(onlinePageDatasourceList, pageId);
        return Result.succeed();
    }

    /**
     * 显示在线表单页面和指定数据源的多对多关联详情数据。
     *
     * @param pageId       主表主键Id。
     * @param datasourceId 从表主键Id。
     * @return 应答结果对象，包括中间表详情。
     */
    @GetMapping("/viewOnlinePageDatasource")
    public Result<OnlinePageDatasourceVo> viewOnlinePageDatasource(
            @RequestParam Long pageId, @RequestParam Long datasourceId) {
        if (ObjectUtil.isAnyBlankOrNull(pageId, datasourceId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        OnlinePageDatasource onlinePageDatasource = onlinePageService.getOnlinePageDatasource(pageId, datasourceId);
        if (onlinePageDatasource == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        OnlinePageDatasourceVo onlinePageDatasourceVo = MyModelUtil.copyTo(onlinePageDatasource, OnlinePageDatasourceVo.class);
        return Result.succeed(onlinePageDatasourceVo);
    }

    /**
     * 移除指定在线表单页面和指定数据源的多对多关联关系。
     *
     * @param pageId       主表主键Id。
     * @param datasourceId 从表主键Id。
     * @return 应答结果对象。
     */
    @PostMapping("/deleteOnlinePageDatasource")
    public Result<Void> deleteOnlinePageDatasource(
            @MyRequestBody Long pageId, @MyRequestBody Long datasourceId) {
        if (ObjectUtil.isAnyBlankOrNull(pageId, datasourceId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        if (!onlinePageService.removeOnlinePageDatasource(pageId, datasourceId)) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.succeed();
    }

    private Result<Void> doOnlinePageDatasourceVerify(Long pageId) {
        if (ObjectUtil.isBlankOrNull(pageId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        if (!onlinePageService.existId(pageId)) {
            return Result.failed(ResultCode.INVALID_RELATED_RECORD_ID);
        }
        return Result.succeed();
    }
}
