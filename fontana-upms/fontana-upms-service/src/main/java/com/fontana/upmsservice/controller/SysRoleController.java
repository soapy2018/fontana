package com.fontana.upmsservice.controller;

import com.alibaba.fastjson.TypeReference;
import com.fontana.base.annotation.MyRequestBody;
import com.fontana.db.object.MyRelationParam;
import com.fontana.base.result.Pagination;
import com.fontana.base.result.Result;
import com.fontana.base.result.ResultCode;
import com.fontana.base.validate.UpdateGroup;
import com.fontana.util.validate.ValidateUtil;
import com.fontana.base.result.CallResult;
import com.fontana.db.object.MyOrderParam;
import com.fontana.db.object.MyPageParam;
import com.fontana.db.util.MyModelUtil;
import com.fontana.db.util.MyPageUtil;
import com.fontana.upmsapi.dto.SysRoleDto;
import com.fontana.upmsapi.dto.SysUserDto;
import com.fontana.upmsapi.vo.SysRoleVo;
import com.fontana.upmsapi.vo.SysUserVo;
import com.fontana.upmsservice.entity.SysRole;
import com.fontana.upmsservice.entity.SysUser;
import com.fontana.upmsservice.entity.SysUserRole;
import com.fontana.upmsservice.service.SysRoleService;
import com.fontana.upmsservice.service.SysUserService;
import com.fontana.util.lang.ObjectUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.groups.Default;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色管理接口控制器类。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Api(tags = "角色管理接口")
@Slf4j
@RestController
@RequestMapping("/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 新增角色操作。
     *
     * @param sysRoleDto       新增角色对象。
     * @param menuIdListString 与当前角色Id绑定的menuId列表，多个menuId之间逗号分隔。
     * @return 应答结果对象，包含新增角色的主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {"sysRoleDto.roleId", "sysRoleDto.createTimeStart", "sysRoleDto.createTimeEnd"})
    //@OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public Result<Long> add(
            @MyRequestBody SysRoleDto sysRoleDto, @MyRequestBody String menuIdListString) {
        String errorMessage = ValidateUtil.getModelValidationError(sysRoleDto);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysRole sysRole = MyModelUtil.copyTo(sysRoleDto, SysRole.class);
        CallResult result = sysRoleService.verifyRelatedData(sysRole, null, menuIdListString);
        if (!result.isSuccess()) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, result.getErrorMessage());
        }
        Set<Long> menuIdSet = null;
        if (result.getData() != null) {
            menuIdSet = result.getData().getObject("menuIdSet", new TypeReference<Set<Long>>(){});
        }
        sysRoleService.saveNew(sysRole, menuIdSet);
        return Result.succeed(sysRole.getRoleId());
    }

    /**
     * 更新角色操作。
     *
     * @param sysRoleDto       更新角色对象。
     * @param menuIdListString 与当前角色Id绑定的menuId列表，多个menuId之间逗号分隔。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {"sysRoleDto.createTimeStart", "sysRoleDto.createTimeEnd"})
    //@OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public Result<Void> update(
            @MyRequestBody SysRoleDto sysRoleDto, @MyRequestBody String menuIdListString) {
        String errorMessage = ValidateUtil.getModelValidationError(sysRoleDto, Default.class, UpdateGroup.class);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysRole originalSysRole = sysRoleService.getById(sysRoleDto.getRoleId());
        if (originalSysRole == null) {
            errorMessage = "数据验证失败，当前角色并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        SysRole sysRole = MyModelUtil.copyTo(sysRoleDto, SysRole.class);
        CallResult result = sysRoleService.verifyRelatedData(sysRole, originalSysRole, menuIdListString);
        if (!result.isSuccess()) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, result.getErrorMessage());
        }
        Set<Long> menuIdSet = null;
        if (result.getData() != null) {
            menuIdSet = result.getData().getObject("menuIdSet", new TypeReference<Set<Long>>(){});
        }
        if (!sysRoleService.update(sysRole, originalSysRole, menuIdSet)) {
            errorMessage = "更新失败，数据不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        return Result.succeed();
    }

    /**
     * 删除指定角色操作。
     *
     * @param roleId 指定角色主键Id。
     * @return 应答结果对象。
     */
    //@OperationLog(type = SysOperationLogType.DELETE)
    @PostMapping("/delete")
    public Result<Void> delete(@MyRequestBody Long roleId) {
        if (ObjectUtil.isAnyBlankOrNull(roleId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        if (!sysRoleService.remove(roleId)) {
            String errorMessage = "数据操作失败，角色不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        return Result.succeed();
    }

    /**
     * 查看角色列表。
     *
     * @param sysRoleDtoFilter 角色过滤对象。
     * @param orderParam       排序参数。
     * @param pageParam        分页参数。
     * @return 应答结果对象，包含角色列表。
     */
    @PostMapping("/list")
    public Result<Pagination<SysRoleVo>> list(
            @MyRequestBody SysRoleDto sysRoleDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        SysRole filter = MyModelUtil.copyTo(sysRoleDtoFilter, SysRole.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, SysRole.class);
        List<SysRole> roleList = sysRoleService.getSysRoleList(filter, orderBy);
        List<SysRoleVo> roleVoList = MyModelUtil.copyCollectionTo(roleList, SysRoleVo.class);
        long totalCount = 0L;
        if (roleList instanceof Page) {
            totalCount = ((Page<SysRole>) roleList).getTotal();
        }
        return Result.succeed(MyPageUtil.makeResponseData(roleVoList, totalCount));
    }

    /**
     * 查看角色详情。
     *
     * @param roleId 指定角色主键Id。
     * @return 应答结果对象，包含角色详情对象。
     */
    @GetMapping("/view")
    public Result<SysRoleVo> view(@RequestParam Long roleId) {
        if (ObjectUtil.isAnyBlankOrNull(roleId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        SysRole sysRole = sysRoleService.getByIdWithRelation(roleId, MyRelationParam.full());
        if (sysRole == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        SysRoleVo sysRoleVo = MyModelUtil.copyTo(sysRole, SysRoleVo.class);
        return Result.succeed(sysRoleVo);
    }

    /**
     * 获取不包含指定角色Id的用户列表。
     * 用户和角色是多对多关系，当前接口将返回没有赋值指定RoleId的用户列表。可用于给角色添加新用户。
     *
     * @param roleId           角色主键Id。
     * @param sysUserDtoFilter 用户过滤对象。
     * @param orderParam       排序参数。
     * @param pageParam        分页参数。
     * @return 应答结果对象，包含用户列表数据。
     */
    @PostMapping("/listNotInUserRole")
    public Result<Pagination<SysUserVo>> listNotInUserRole(
            @MyRequestBody Long roleId,
            @MyRequestBody SysUserDto sysUserDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        Result<Void> verifyResult = this.doRoleUserVerify(roleId);
        if (!verifyResult.isSuccess()) {
            return Result.failed(verifyResult);
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        SysUser filter = MyModelUtil.copyTo(sysUserDtoFilter, SysUser.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, SysUser.class);
        List<SysUser> userList = sysUserService.getNotInSysUserListByRoleId(roleId, filter, orderBy);
        List<SysUserVo> userVoList = MyModelUtil.copyCollectionTo(userList, SysUserVo.class);
        return Result.succeed(MyPageUtil.makeResponseData(userVoList));
    }

    /**
     * 拥有指定角色的用户列表。
     *
     * @param roleId           角色主键Id。
     * @param sysUserDtoFilter 用户过滤对象。
     * @param orderParam       排序参数。
     * @param pageParam        分页参数。
     * @return 应答结果对象，包含用户列表数据。
     */
    @PostMapping("/listUserRole")
    public Result<Pagination<SysUserVo>> listUserRole(
            @MyRequestBody Long roleId,
            @MyRequestBody SysUserDto sysUserDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        Result<Void> verifyResult = this.doRoleUserVerify(roleId);
        if (!verifyResult.isSuccess()) {
            return Result.failed(verifyResult);
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        SysUser filter = MyModelUtil.copyTo(sysUserDtoFilter, SysUser.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, SysUser.class);
        List<SysUser> userList = sysUserService.getSysUserListByRoleId(roleId, filter, orderBy);
        List<SysUserVo> userVoList = MyModelUtil.copyCollectionTo(userList, SysUserVo.class);
        return Result.succeed(MyPageUtil.makeResponseData(userVoList));
    }

    private Result<Void> doRoleUserVerify(Long roleId) {
        if (ObjectUtil.isAnyBlankOrNull(roleId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        if (!sysRoleService.existId(roleId)) {
            return Result.failed(ResultCode.INVALID_RELATED_RECORD_ID);
        }
        return Result.succeed();
    }

    /**
     * 为指定角色添加用户列表。该操作可同时给一批用户赋值角色，并在同一事务内完成。
     *
     * @param roleId           角色主键Id。
     * @param userIdListString 逗号分隔的用户Id列表。
     * @return 应答结果对象。
     */
    //@OperationLog(type = SysOperationLogType.ADD_M2M)
    @PostMapping("/addUserRole")
    public Result<Void> addUserRole(
            @MyRequestBody Long roleId, @MyRequestBody String userIdListString) {
        if (ObjectUtil.isAnyBlankOrNull(roleId, userIdListString)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        Set<Long> userIdSet = Arrays.stream(
                userIdListString.split(",")).map(Long::valueOf).collect(Collectors.toSet());
        if (!sysRoleService.existId(roleId)
                || !sysUserService.existUniqueKeyList("userId", userIdSet)) {
            return Result.failed(ResultCode.INVALID_RELATED_RECORD_ID);
        }
        List<SysUserRole> userRoleList = new LinkedList<>();
        for (Long userId : userIdSet) {
            SysUserRole userRole = new SysUserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(userId);
            userRoleList.add(userRole);
        }
        sysRoleService.addUserRoleList(userRoleList);
        return Result.succeed();
    }

    /**
     * 为指定用户移除指定角色。
     *
     * @param roleId 指定角色主键Id。
     * @param userId 指定用户主键Id。
     * @return 应答数据结果。
     */
    //@OperationLog(type = SysOperationLogType.DELETE_M2M)
    @PostMapping("/deleteUserRole")
    public Result<Void> deleteUserRole(
            @MyRequestBody Long roleId, @MyRequestBody Long userId) {
        if (ObjectUtil.isAnyBlankOrNull(roleId, userId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        if (!sysRoleService.removeUserRole(roleId, userId)) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.succeed();
    }

    /**
     * 查询角色的权限资源地址列表。同时返回详细的分配路径。
     *
     * @param roleId 角色Id。
     * @param url    url过滤条件。
     * @return 应答对象，包含从角色到权限资源的完整权限分配路径信息的查询结果列表。
     */
    @GetMapping("/listSysPermWithDetail")
    public Result<List<Map<String, Object>>> listSysPermWithDetail(Long roleId, String url) {
        if (ObjectUtil.isBlankOrNull(roleId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        return Result.succeed(sysRoleService.getSysPermListWithDetail(roleId, url));
    }

    /**
     * 查询角色的权限字列表。同时返回详细的分配路径。
     *
     * @param roleId   角色Id。
     * @param permCode 权限字名称过滤条件。
     * @return 应答对象，包含从角色到权限字的权限分配路径信息的查询结果列表。
     */
    @GetMapping("/listSysPermCodeWithDetail")
    public Result<List<Map<String, Object>>> listSysPermCodeWithDetail(Long roleId, String permCode) {
        if (ObjectUtil.isBlankOrNull(roleId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        return Result.succeed(sysRoleService.getSysPermCodeListWithDetail(roleId, permCode));
    }
}
