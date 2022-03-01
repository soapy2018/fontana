package com.bluetron.nb.common.upmsservice.controller;

import com.bluetron.nb.common.base.annotation.MyRequestBody;
import com.bluetron.nb.common.base.result.Pagination;
import com.bluetron.nb.common.base.result.Result;
import com.bluetron.nb.common.base.result.ResultCode;
import com.bluetron.nb.common.base.validate.UpdateGroup;
import com.bluetron.nb.common.util.validate.ValidateUtil;
import com.bluetron.nb.common.base.result.CallResult;
import com.bluetron.nb.common.db.object.MyPageParam;
import com.bluetron.nb.common.db.util.MyModelUtil;
import com.bluetron.nb.common.db.util.MyPageUtil;
import com.bluetron.nb.common.upmsapi.dto.SysPermDto;
import com.bluetron.nb.common.upmsapi.vo.SysPermVo;
import com.bluetron.nb.common.upmsservice.entity.SysPerm;
import com.bluetron.nb.common.upmsservice.service.SysPermService;
import com.bluetron.nb.common.util.lang.ObjectUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.groups.Default;
import java.util.List;
import java.util.Map;

/**
 * 权限资源管理接口控制器类。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Api(tags = "权限资源管理接口")
@Slf4j
@RestController
@RequestMapping("/sysPerm")
public class SysPermController {

    @Autowired
    private SysPermService sysPermService;

    /**
     * 新增权限资源操作。
     *
     * @param sysPermDto 新增权限资源对象。
     * @return 应答结果对象，包含新增权限资源的主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {"sysPermDto.permId"})
    //@OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public Result<Long> add(@MyRequestBody SysPermDto sysPermDto) {
        String errorMessage = ValidateUtil.getModelValidationError(sysPermDto);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysPerm sysPerm = MyModelUtil.copyTo(sysPermDto, SysPerm.class);
        CallResult result = sysPermService.verifyRelatedData(sysPerm, null);
        if (!result.isSuccess()) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, result.getErrorMessage());
        }
        sysPerm = sysPermService.saveNew(sysPerm);
        return Result.succeed(sysPerm.getPermId());
    }

    /**
     * 更新权限资源操作。
     *
     * @param sysPermDto 更新权限资源对象。
     * @return 应答结果对象，包含更新权限资源的主键Id。
     */
    //@OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public Result<Void> update(@MyRequestBody SysPermDto sysPermDto) {
        String errorMessage = ValidateUtil.getModelValidationError(sysPermDto, Default.class, UpdateGroup.class);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysPerm originalPerm = sysPermService.getById(sysPermDto.getPermId());
        if (originalPerm == null) {
            errorMessage = "数据验证失败，当前权限资源并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        SysPerm sysPerm = MyModelUtil.copyTo(sysPermDto, SysPerm.class);
        CallResult result = sysPermService.verifyRelatedData(sysPerm, originalPerm);
        if (!result.isSuccess()) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, result.getErrorMessage());
        }
        sysPermService.update(sysPerm, originalPerm);
        return Result.succeed();
    }

    /**
     * 删除指定权限资源操作。
     *
     * @param permId 指定的权限资源主键Id。
     * @return 应答结果对象。
     */
    //@OperationLog(type = SysOperationLogType.DELETE)
    @PostMapping("/delete")
    public Result<Void> delete(@MyRequestBody Long permId) {
        if (ObjectUtil.isAnyBlankOrNull(permId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        if (!sysPermService.remove(permId)) {
            String errorMessage = "数据操作失败，权限不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        return Result.succeed();
    }

    /**
     * 查看权限资源对象详情。
     *
     * @param permId 指定权限资源主键Id。
     * @return 应答结果对象，包含权限资源对象详情。
     */
    @GetMapping("/view")
    public Result<SysPermVo> view(@RequestParam Long permId) {
        if (ObjectUtil.isAnyBlankOrNull(permId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        SysPerm perm = sysPermService.getById(permId);
        if (perm == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        SysPermVo permVo = MyModelUtil.copyTo(perm, SysPermVo.class);
        return Result.succeed(permVo);
    }

    /**
     * 查看权限资源列表。
     *
     * @param sysPermDtoFilter 过滤对象。
     * @param pageParam         分页参数。
     * @return 应答结果对象，包含权限资源列表。
     */
    @PostMapping("/list")
    public Result<Pagination<SysPermVo>> list(
            @MyRequestBody SysPermDto sysPermDtoFilter, @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        SysPerm filter = MyModelUtil.copyTo(sysPermDtoFilter, SysPerm.class);
        List<SysPerm> permList = sysPermService.getPermListWithRelation(filter);
        List<SysPermVo> permVoList = MyModelUtil.copyCollectionTo(permList, SysPermVo.class);
        long totalCount = 0L;
        if (permList instanceof Page) {
            totalCount = ((Page<SysPerm>) permList).getTotal();
        }
        return Result.succeed(MyPageUtil.makeResponseData(permVoList, totalCount));
    }

    /**
     * 查询权限资源地址的用户列表。同时返回详细的分配路径。
     *
     * @param permId    权限资源Id。
     * @param loginName 登录名。
     * @return 应答对象。包含从权限资源到用户的完整权限分配路径信息的查询结果列表。
     */
    @GetMapping("/listSysUserWithDetail")
    public Result<List<Map<String, Object>>> listSysUserWithDetail(Long permId, String loginName) {
        if (ObjectUtil.isBlankOrNull(permId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        return Result.succeed(sysPermService.getSysUserListWithDetail(permId, loginName));
    }

    /**
     * 查询权限资源地址的角色列表。同时返回详细的分配路径。
     *
     * @param permId   权限资源Id。
     * @param roleName 角色名。
     * @return 应答对象。包含从权限资源到角色的权限分配路径信息的查询结果列表。
     */
    @GetMapping("/listSysRoleWithDetail")
    public Result<List<Map<String, Object>>> listSysRoleWithDetail(Long permId, String roleName) {
        if (ObjectUtil.isBlankOrNull(permId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        return Result.succeed(sysPermService.getSysRoleListWithDetail(permId, roleName));
    }

    /**
     * 查询权限资源地址的菜单列表。同时返回详细的分配路径。
     *
     * @param permId   权限资源Id。
     * @param menuName 菜单名。
     * @return 应答对象。包含从权限资源到菜单的权限分配路径信息的查询结果列表。
     */
    @GetMapping("/listSysMenuWithDetail")
    public Result<List<Map<String, Object>>> listSysMenuWithDetail(Long permId, String menuName) {
        if (ObjectUtil.isBlankOrNull(permId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        return Result.succeed(sysPermService.getSysMenuListWithDetail(permId, menuName));
    }  
}
