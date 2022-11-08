package com.fontana.upmsservice.controller;

import cn.jimmyshi.beanquery.BeanQuery;
import com.fontana.base.annotation.MyRequestBody;
import com.fontana.base.result.Pagination;
import com.fontana.base.result.Result;
import com.fontana.base.result.ResultCode;
import com.fontana.db.controller.AbstractBaseController;
import com.fontana.db.object.*;
import com.fontana.db.service.IBaseService;
import com.fontana.db.util.MyModelUtil;
import com.fontana.db.util.MyPageUtil;
import com.fontana.upmsapi.dto.SysDeptDto;
import com.fontana.upmsapi.dto.SysDeptPostDto;
import com.fontana.upmsapi.dto.SysPostDto;
import com.fontana.upmsapi.vo.SysDeptPostVo;
import com.fontana.upmsapi.vo.SysDeptVo;
import com.fontana.upmsapi.vo.SysPostVo;
import com.fontana.upmsservice.entity.SysDept;
import com.fontana.upmsservice.entity.SysDeptPost;
import com.fontana.upmsservice.entity.SysPost;
import com.fontana.upmsservice.service.SysDeptService;
import com.fontana.upmsservice.service.SysPostService;
import com.fontana.util.lang.ObjectUtil;
import com.fontana.util.validate.ValidateUtil;
import com.github.pagehelper.page.PageMethod;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 部门管理操作控制器类。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Api(tags = "部门管理管理接口")
@Slf4j
@RestController
@RequestMapping("/sysDept")
public class SysDeptController extends AbstractBaseController<SysDept, SysDeptVo, Long> {

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysPostService sysPostService;

    @Override
    protected IBaseService<SysDept, Long> service() {
        return sysDeptService;
    }

    /**
     * 新增部门管理数据。
     *
     * @param sysDeptDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {"sysDeptDto.deptId"})
    //@OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public Result<Long> add(@MyRequestBody SysDeptDto sysDeptDto) {
        String errorMessage = ValidateUtil.getModelValidationError(sysDeptDto, false);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysDept sysDept = MyModelUtil.copyTo(sysDeptDto, SysDept.class);
        // 验证父Id的数据合法性
        SysDept parentSysDept = null;
        if (!ObjectUtil.isBlankOrNull(sysDept.getParentId())) {
            parentSysDept = sysDeptService.getById(sysDept.getParentId());
            if (parentSysDept == null) {
                errorMessage = "数据验证失败，关联的父节点并不存在，请刷新后重试！";
                return Result.failed(ResultCode.DATA_PARENT_ID_NOT_EXIST, errorMessage);
            }
        }
        sysDept = sysDeptService.saveNew(sysDept, parentSysDept);
        return Result.succeed(sysDept.getDeptId());
    }

    /**
     * 更新部门管理数据。
     *
     * @param sysDeptDto 更新对象。
     * @return 应答结果对象。
     */
    //@OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public Result<Void> update(@MyRequestBody SysDeptDto sysDeptDto) {
        String errorMessage = ValidateUtil.getModelValidationError(sysDeptDto, true);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysDept sysDept = MyModelUtil.copyTo(sysDeptDto, SysDept.class);
        SysDept originalSysDept = sysDeptService.getById(sysDept.getDeptId());
        if (originalSysDept == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        // 验证父Id的数据合法性
        if (!ObjectUtil.isBlankOrNull(sysDept.getParentId())
                && ObjectUtils.notEqual(sysDept.getParentId(), originalSysDept.getParentId())) {
            SysDept parentSysDept = sysDeptService.getById(sysDept.getParentId());
            if (parentSysDept == null) {
                errorMessage = "数据验证失败，关联的父节点并不存在，请刷新后重试！";
                return Result.failed(ResultCode.DATA_PARENT_ID_NOT_EXIST, errorMessage);
            }
        }
        if (!sysDeptService.update(sysDept, originalSysDept)) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.succeed();
    }

    /**
     * 删除部门管理数据。
     *
     * @param deptId 删除对象主键Id。
     * @return 应答结果对象。
     */
    //@OperationLog(type = SysOperationLogType.DELETE)
    @PostMapping("/delete")
    public Result<Void> delete(@MyRequestBody Long deptId) {
        String errorMessage;
        if (ObjectUtil.isBlankOrNull(deptId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        // 验证关联Id的数据合法性
        SysDept originalSysDept = sysDeptService.getById(deptId);
        if (originalSysDept == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        if (sysDeptService.hasChildren(deptId)) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象存在子对象]，请刷新后重试！";
            return Result.failed(ResultCode.HAS_CHILDREN_DATA, errorMessage);
        }
        if (sysDeptService.hasChildrenUser(deptId)) {
            errorMessage = "数据验证失败，请先移除部门用户数据后，再删除当前部门！";
            return Result.failed(ResultCode.HAS_CHILDREN_DATA, errorMessage);
        }
        if (!sysDeptService.remove(deptId)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        return Result.succeed();
    }

    /**
     * 列出符合过滤条件的部门管理列表。
     *
     * @param sysDeptDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public Result<Pagination<SysDeptVo>> list(
            @MyRequestBody SysDeptDto sysDeptDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        SysDept sysDeptFilter = MyModelUtil.copyTo(sysDeptDtoFilter, SysDept.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, SysDept.class);
        List<SysDept> sysDeptList = sysDeptService.getSysDeptListWithRelation(sysDeptFilter, orderBy);
        return Result.succeed(MyPageUtil.makeResponseData(sysDeptList, SysDept.INSTANCE));
    }

    /**
     * 查看指定部门管理对象详情。
     *
     * @param deptId 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public Result<SysDeptVo> view(@RequestParam Long deptId) {
        if (ObjectUtil.isAnyBlankOrNull(deptId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        SysDept sysDept = sysDeptService.getByIdWithRelation(deptId, MyRelationParam.full());
        if (sysDept == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        SysDeptVo sysDeptVo = SysDept.INSTANCE.fromModel(sysDept);
        return Result.succeed(sysDeptVo);
    }
    /**
     * 列出不与指定部门管理存在多对多关系的 [岗位管理] 列表数据。通常用于查看添加新 [岗位管理] 对象的候选列表。
     *
     * @param deptId 主表关联字段。
     * @param sysPostDtoFilter [岗位管理] 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，返回符合条件的数据列表。
     */
    @PostMapping("/listNotInSysDeptPost")
    public Result<Pagination<SysPostVo>> listNotInSysDeptPost(
            @MyRequestBody Long deptId,
            @MyRequestBody SysPostDto sysPostDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (ObjectUtil.isBlankOrNull(deptId) && !sysDeptService.existId(deptId)) {
            return Result.failed(ResultCode.INVALID_RELATED_RECORD_ID);
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        SysPost filter = MyModelUtil.copyTo(sysPostDtoFilter, SysPost.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, SysPost.class);
        List<SysPost> sysPostList;
        if (ObjectUtil.isBlankOrNull(deptId)) {
            sysPostList = sysPostService.getNotInSysPostListByDeptId(deptId, filter, orderBy);
        } else {
            sysPostList = sysPostService.getSysPostList(filter, orderBy);
        }
        return Result.succeed(MyPageUtil.makeResponseData(sysPostList, SysPost.INSTANCE));
    }

    /**
     * 列出与指定部门管理存在多对多关系的 [岗位管理] 列表数据。
     *
     * @param deptId 主表关联字段。
     * @param sysPostDtoFilter [岗位管理] 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，返回符合条件的数据列表。
     */
    @PostMapping("/listSysDeptPost")
    public Result<Pagination<SysPostVo>> listSysDeptPost(
            @MyRequestBody(required = true) Long deptId,
            @MyRequestBody SysPostDto sysPostDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (!sysDeptService.existId(deptId)) {
            return Result.failed(ResultCode.INVALID_RELATED_RECORD_ID);
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        SysPost filter = MyModelUtil.copyTo(sysPostDtoFilter, SysPost.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, SysPost.class);
        List<SysPost> sysPostList = sysPostService.getSysPostListByDeptId(deptId, filter, orderBy);
        return Result.succeed(MyPageUtil.makeResponseData(sysPostList, SysPost.INSTANCE));
    }

    /**
     * 批量添加部门管理和 [岗位管理] 对象的多对多关联关系数据。
     *
     * @param deptId 主表主键Id。
     * @param sysDeptPostDtoList 关联对象列表。
     * @return 应答结果对象。
     */
    @PostMapping("/addSysDeptPost")
    public Result<Void> addSysDeptPost(
            @MyRequestBody Long deptId,
            @MyRequestBody(elementType = SysDeptPostDto.class) List<SysDeptPostDto> sysDeptPostDtoList) {
        if (ObjectUtil.isAnyBlankOrNull(deptId, sysDeptPostDtoList)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        String errorMessage = ValidateUtil.getModelValidationError(sysDeptPostDtoList);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        Set<Long> postIdSet = sysDeptPostDtoList.stream().map(SysDeptPostDto::getPostId).collect(Collectors.toSet());
        if (!sysDeptService.existId(deptId) || !sysPostService.existUniqueKeyList("postId", postIdSet)) {
            return Result.failed(ResultCode.INVALID_RELATED_RECORD_ID);
        }
        List<SysDeptPost> sysDeptPostList = MyModelUtil.copyCollectionTo(sysDeptPostDtoList, SysDeptPost.class);
        sysDeptService.addSysDeptPostList(sysDeptPostList, deptId);
        return Result.succeed();
    }

    /**
     * 更新指定部门管理和指定 [岗位管理] 的多对多关联数据。
     *
     * @param sysDeptPostDto 对多对中间表对象。
     * @return 应答结果对象。
     */
    @PostMapping("/updateSysDeptPost")
    public Result<Void> updateSysDeptPost(@MyRequestBody SysDeptPostDto sysDeptPostDto) {
        String errorMessage = ValidateUtil.getModelValidationError(sysDeptPostDto);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysDeptPost sysDeptPost = MyModelUtil.copyTo(sysDeptPostDto, SysDeptPost.class);
        if (!sysDeptService.updateSysDeptPost(sysDeptPost)) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.succeed();
    }

    /**
     * 显示部门管理和指定 [岗位管理] 的多对多关联详情数据。
     *
     * @param deptId 主表主键Id。
     * @param postId 从表主键Id。
     * @return 应答结果对象，包括中间表详情。
     */
    @GetMapping("/viewSysDeptPost")
    public Result<SysDeptPostVo> viewSysDeptPost(@RequestParam Long deptId, @RequestParam Long postId) {
        if (ObjectUtil.isAnyBlankOrNull(deptId, postId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        SysDeptPost sysDeptPost = sysDeptService.getSysDeptPost(deptId, postId);
        if (sysDeptPost == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        SysDeptPostVo sysDeptPostVo = MyModelUtil.copyTo(sysDeptPost, SysDeptPostVo.class);
        return Result.succeed(sysDeptPostVo);
    }

    /**
     * 移除指定部门管理和指定 [岗位管理] 的多对多关联关系。
     *
     * @param deptId 主表主键Id。
     * @param postId 从表主键Id。
     * @return 应答结果对象。
     */
    @PostMapping("/deleteSysDeptPost")
    public Result<Void> deleteSysDeptPost(@MyRequestBody Long deptId, @MyRequestBody Long postId) {
        if (ObjectUtil.isAnyBlankOrNull(deptId, postId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        if (!sysDeptService.removeSysDeptPost(deptId, postId)) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.succeed();
    }

    /**
     * 获取部门岗位多对多关联数据，及其关联的部门和岗位数据。
     *
     * @param deptId 部门Id，如果为空，返回全部数据列表。
     * @return 部门岗位多对多关联数据，及其关联的部门和岗位数据
     */
    @GetMapping("/listSysDeptPostWithRelation")
    public Result<List<Map<String, Object>>> listSysDeptPostWithRelation(
            @RequestParam(required = false) Long deptId) {
        return Result.succeed(sysDeptService.getSysDeptPostListWithRelationByDeptId(deptId));
    }


    /**
     * 以字典形式返回全部部门管理数据集合。字典的键值为[deptId, deptName]。
     * 白名单接口，登录用户均可访问。
     *
     * @param filter 过滤对象。
     * @return 应答结果对象，包含字典形式的数据集合。
     */
    @GetMapping("/listDict")
    public Result<List<Map<String, Object>>> listDict(SysDept filter) {
        List<SysDept> resultList = sysDeptService.getListByFilter(filter);
        return Result.succeed(
                BeanQuery.select("parentId as parentId", "deptId as id", "deptName as name").executeFrom(resultList));
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
        List<SysDept> resultList = sysDeptService.getInList(new HashSet<>(dictIds));
        return Result.succeed(
                BeanQuery.select("parentId as parentId", "deptId as id", "deptName as name").executeFrom(resultList));
    }

    /**
     * 根据父主键Id，以字典的形式返回其下级数据列表。
     * 白名单接口，登录用户均可访问。
     *
     * @param parentId 父主键Id。
     * @return 按照字典的形式返回下级数据列表。
     */
    @GetMapping("/listDictByParentId")
    public Result<List<Map<String, Object>>> listDictByParentId(@RequestParam(required = false) Long parentId) {
        List<SysDept> resultList = sysDeptService.getListByParentId("parentId", parentId);
        return Result.succeed(
                BeanQuery.select("parentId as parentId", "deptId as id", "deptName as name").executeFrom(resultList));
    }

    /**
     * 根据主键Id集合，获取数据对象集合。仅限于微服务间远程接口调用。
     *
     * @param deptIds 主键Id集合。
     * @param withDict 是否包含字典关联。
     * @return 应答结果对象，包含主对象集合。
     */
    @ApiOperation(hidden = true, value = "listByIds")
    @PostMapping("/listByIds")
    public Result<List<SysDeptVo>> listByIds(
            @RequestParam Set<Long> deptIds, @RequestParam Boolean withDict) {
        return super.baseListByIds(deptIds, withDict, SysDept.INSTANCE);
    }

    /**
     * 根据主键Id，获取数据对象。仅限于微服务间远程接口调用。
     *
     * @param deptId 主键Id。
     * @param withDict 是否包含字典关联。
     * @return 应答结果对象，包含主对象数据。
     */
    @ApiOperation(hidden = true, value = "getById")
    @PostMapping("/getById")
    public Result<SysDeptVo> getById(
            @RequestParam Long deptId, @RequestParam Boolean withDict) {
        return super.baseGetById(deptId, withDict, SysDept.INSTANCE);
    }

    /**
     * 判断参数列表中指定的主键Id集合，是否全部存在。仅限于微服务间远程接口调用。
     *
     * @param deptIds 主键Id集合。
     * @return 应答结果对象，包含true全部存在，否则false。
     */
    @ApiOperation(hidden = true, value = "existIds")
    @PostMapping("/existIds")
    public Result<Boolean> existIds(@RequestParam Set<Long> deptIds) {
        return super.baseExistIds(deptIds);
    }

    /**
     * 判断参数列表中指定的主键Id是否存在。仅限于微服务间远程接口调用。
     *
     * @param deptId 主键Id。
     * @return 应答结果对象，包含true表示存在，否则false。
     */
    @ApiOperation(hidden = true, value = "existId")
    @PostMapping("/existId")
    public Result<Boolean> existId(@RequestParam Long deptId) {
        return super.baseExistId(deptId);
    }

    /**
     * 根据主键Id删除数据。
     *
     * @param deptId 主键Id。
     * @return 删除数量。
     */
    @ApiOperation(hidden = true, value = "deleteById")
    @PostMapping("/deleteById")
    public Result<Integer> deleteById(@RequestParam Long deptId) throws Exception {
        SysDept filter = new SysDept();
        filter.setDeptId(deptId);
        return super.baseDeleteBy(filter);
    }

    /**
     * 删除符合过滤条件的数据。
     *
     * @param filter 过滤对象。
     * @return 删除数量。
     */
    @ApiOperation(hidden = true, value = "deleteBy")
    @PostMapping("/deleteBy")
    public Result<Integer> deleteBy(@RequestBody SysDeptDto filter) throws Exception {
        return super.baseDeleteBy(MyModelUtil.copyTo(filter, SysDept.class));
    }

    /**
     * 复杂的查询调用，包括(in list)过滤，对象条件过滤，分页和排序等。主要用于微服务间远程过程调用。
     *
     * @param queryParam 查询参数。
     * @return 分页数据集合对象。如MyQueryParam参数的分页属性为空，则不会执行分页操作，只是基于Pagination对象返回数据结果。
     */
    @ApiOperation(hidden = true, value = "listBy")
    @PostMapping("/listBy")
    public Result<Pagination<SysDeptVo>> listBy(@RequestBody MyQueryParam queryParam) {
        return super.baseListBy(queryParam, SysDept.INSTANCE);
    }

    /**
     * 复杂的查询调用，包括(in list)过滤，对象条件过滤，分页和排序等。主要用于微服务间远程过程调用。
     *
     * @param queryParam 查询参数。
     * @return 分页数据集合对象。如MyQueryParam参数的分页属性为空，则不会执行分页操作，只是基于Pagination对象返回数据结果。
     */
    @ApiOperation(hidden = true, value = "listMapBy")
    @PostMapping("/listMapBy")
    public Result<Pagination<Map<String, Object>>> listMapBy(@RequestBody MyQueryParam queryParam) {
        return super.baseListMapBy(queryParam, SysDept.INSTANCE);
    }

    /**
     * 复杂的查询调用，仅返回单体记录。主要用于微服务间远程过程调用。
     *
     * @param queryParam 查询参数。
     * @return 应答结果对象，包含符合查询过滤条件的对象结果集。
     */
    @ApiOperation(hidden = true, value = "getBy")
    @PostMapping("/getBy")
    public Result<SysDeptVo> getBy(@RequestBody MyQueryParam queryParam) {
        return super.baseGetBy(queryParam, SysDept.INSTANCE);
    }

    /**
     * 获取远程主对象中符合查询条件的数据数量。主要用于微服务间远程过程调用。
     *
     * @param queryParam 查询参数。
     * @return 应答结果对象，包含结果数量。
     */
    @ApiOperation(hidden = true, value = "countBy")
    @PostMapping("/countBy")
    public Result<Integer> countBy(@RequestBody MyQueryParam queryParam) {
        return super.baseCountBy(queryParam);
    }

    /**
     * 获取远程对象中符合查询条件的分组聚合计算Map列表。
     *
     * @param aggregationParam 聚合参数。
     * @return 应该结果对象，包含聚合计算后的分组Map列表。
     */
    @ApiOperation(hidden = true, value = "aggregateBy")
    @PostMapping("/aggregateBy")
    public Result<List<Map<String, Object>>> aggregateBy(@RequestBody MyAggregationParam aggregationParam) {
        return super.baseAggregateBy(aggregationParam);
    }
}
