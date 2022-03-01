package com.bluetron.nb.common.flowservice.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.jimmyshi.beanquery.BeanQuery;
import com.bluetron.nb.common.base.annotation.MyRequestBody;
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
import com.bluetron.nb.common.flowapi.dict.FlowEntryStatus;
import com.bluetron.nb.common.flowapi.dto.FlowCategoryDto;
import com.bluetron.nb.common.flowapi.vo.FlowCategoryVo;
import com.bluetron.nb.common.flowservice.entity.FlowCategory;
import com.bluetron.nb.common.flowservice.entity.FlowEntry;
import com.bluetron.nb.common.flowservice.service.FlowCategoryService;
import com.bluetron.nb.common.flowservice.service.FlowEntryService;
import com.bluetron.nb.common.util.lang.ObjectUtil;
import com.github.pagehelper.page.PageMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 工作流分类操作控制器类。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Slf4j
@RestController
@RequestMapping("${bluetron-nb-common.common-flow.urlPrefix}/flowCategory")
public class FlowCategoryController {

    @Autowired
    private FlowCategoryService flowCategoryService;
    @Autowired
    private FlowEntryService flowEntryService;

    /**
     * 新增FlowCategory数据。
     *
     * @param flowCategoryDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @PostMapping("/add")
    public Result<Long> add(@MyRequestBody FlowCategoryDto flowCategoryDto) {
        String errorMessage = ValidateUtil.getModelValidationError(flowCategoryDto);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        FlowCategory flowCategory = MyModelUtil.copyTo(flowCategoryDto, FlowCategory.class);
        flowCategory = flowCategoryService.saveNew(flowCategory);
        return Result.succeed(flowCategory.getCategoryId());
    }

    /**
     * 更新FlowCategory数据。
     *
     * @param flowCategoryDto 更新对象。
     * @return 应答结果对象。
     */
    @PostMapping("/update")
    public Result<Void> update(@MyRequestBody FlowCategoryDto flowCategoryDto) {
        String errorMessage = ValidateUtil.getModelValidationError(flowCategoryDto, Default.class, UpdateGroup.class);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        FlowCategory flowCategory = MyModelUtil.copyTo(flowCategoryDto, FlowCategory.class);
        FlowCategory originalFlowCategory = flowCategoryService.getById(flowCategory.getCategoryId());
        if (originalFlowCategory == null) {
            errorMessage = "数据验证失败，当前流程分类并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (!StrUtil.equals(flowCategory.getCode(), originalFlowCategory.getCode())) {
            FlowEntry filter = new FlowEntry();
            filter.setCategoryId(flowCategory.getCategoryId());
            filter.setStatus(FlowEntryStatus.PUBLISHED);
            List<FlowEntry> flowEntryList = flowEntryService.getListByFilter(filter);
            if (CollUtil.isNotEmpty(flowEntryList)) {
                errorMessage = "数据验证失败，当前流程分类存在已经发布的流程数据，因此分类标识不能修改！";
                return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
            }
        }
        if (!flowCategoryService.update(flowCategory, originalFlowCategory)) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.succeed();
    }

    /**
     * 删除FlowCategory数据。
     *
     * @param categoryId 删除对象主键Id。
     * @return 应答结果对象。
     */
    @PostMapping("/delete")
    public Result<Void> delete(@MyRequestBody Long categoryId) {
        String errorMessage;
        if (ObjectUtil.isBlankOrNull(categoryId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        // 验证关联Id的数据合法性
        FlowCategory originalFlowCategory = flowCategoryService.getById(categoryId);
        if (originalFlowCategory == null) {
            errorMessage = "数据验证失败，当前流程分类并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        FlowEntry filter = new FlowEntry();
        filter.setCategoryId(categoryId);
        List<FlowEntry> flowEntryList = flowEntryService.getListByFilter(filter);
        if (CollUtil.isNotEmpty(flowEntryList)) {
            errorMessage = "数据验证失败，请先删除当前流程分类关联的流程数据！";
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        if (!flowCategoryService.remove(categoryId)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        return Result.succeed();
    }

    /**
     * 列出符合过滤条件的FlowCategory列表。
     *
     * @param flowCategoryDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public Result<Pagination<FlowCategoryVo>> list(
            @MyRequestBody FlowCategoryDto flowCategoryDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        FlowCategory flowCategoryFilter = MyModelUtil.copyTo(flowCategoryDtoFilter, FlowCategory.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, FlowCategory.class);
        List<FlowCategory> flowCategoryList = flowCategoryService.getFlowCategoryListWithRelation(flowCategoryFilter, orderBy);
        return Result.succeed(MyPageUtil.makeResponseData(flowCategoryList, FlowCategory.INSTANCE));
    }

    /**
     * 查看指定FlowCategory对象详情。
     *
     * @param categoryId 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public Result<FlowCategoryVo> view(@RequestParam Long categoryId) {
        if (ObjectUtil.isBlankOrNull(categoryId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        FlowCategory flowCategory = flowCategoryService.getByIdWithRelation(categoryId, MyRelationParam.full());
        if (flowCategory == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        FlowCategoryVo flowCategoryVo = FlowCategory.INSTANCE.fromModel(flowCategory);
        return Result.succeed(flowCategoryVo);
    }

    /**
     * 以字典形式返回全部FlowCategory数据集合。字典的键值为[categoryId, name]。
     * 白名单接口，登录用户均可访问。
     *
     * @param filter 过滤对象。
     * @return 应答结果对象，包含的数据为 List<Map<String, String>>，map中包含两条记录，key的值分别是id和name，value对应具体数据。
     */
    @GetMapping("/listDict")
    public Result<List<Map<String, Object>>> listDict(FlowCategory filter) {
        List<FlowCategory> resultList = flowCategoryService.getListByFilter(filter);
        return Result.succeed(BeanQuery.select(
                "categoryId as id", "name as name").executeFrom(resultList));
    }

    /**
     * 根据字典Id集合，获取查询后的字典数据。
     *
     * @param dictIds 字典Id集合。
     * @return 应答结果对象，包含字典形式的数据集合。
     */
    @PostMapping("/listDictByIds")
    public Result<List<Map<String, Object>>> listDictByIds(
            @MyRequestBody(elementType = Long.class) List<Long> dictIds) {
        List<FlowCategory> resultList = flowCategoryService.getInList(new HashSet<>(dictIds));
        return Result.succeed(BeanQuery.select(
                "categoryId as id", "name as name").executeFrom(resultList));
    }
}
