package com.bluetron.nb.common.upmsservice.controller;

import com.alibaba.fastjson.TypeReference;
import com.bluetron.nb.common.base.annotation.MyRequestBody;
import com.bluetron.nb.common.db.object.MyRelationParam;
import com.bluetron.nb.common.base.result.Result;
import com.bluetron.nb.common.base.result.ResultCode;
import com.bluetron.nb.common.base.validate.UpdateGroup;
import com.bluetron.nb.common.util.validate.ValidateUtil;
import com.bluetron.nb.common.base.result.CallResult;
import com.bluetron.nb.common.db.util.MyModelUtil;
import com.bluetron.nb.common.upmsapi.dict.SysMenuType;
import com.bluetron.nb.common.upmsapi.dto.SysMenuDto;
import com.bluetron.nb.common.upmsapi.vo.SysMenuVo;
import com.bluetron.nb.common.upmsservice.entity.SysMenu;
import com.bluetron.nb.common.upmsservice.service.SysMenuService;
import com.bluetron.nb.common.util.lang.ObjectUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.groups.Default;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 菜单管理接口控制器类。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Api(tags = "菜单管理接口")
@Slf4j
@RestController
@RequestMapping("/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 添加新菜单操作。
     *
     * @param sysMenuDto           新菜单对象。
     * @param permCodeIdListString 与当前菜单Id绑定的权限Id列表，多个权限之间逗号分隔。
     * @return 应答结果对象，包含新增菜单的主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {"sysMenuDto.menuId"})
    //@OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public Result<Long> add(
            @MyRequestBody SysMenuDto sysMenuDto, @MyRequestBody String permCodeIdListString) {
        String errorMessage = ValidateUtil.getModelValidationError(sysMenuDto);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysMenu sysMenu = MyModelUtil.copyTo(sysMenuDto, SysMenu.class);
        if (sysMenu.getParentId() != null) {
            SysMenu parentSysMenu = sysMenuService.getById(sysMenu.getParentId());
            if (parentSysMenu == null) {
                errorMessage = "数据验证失败，关联的父菜单不存在！";
                return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
            }
            if (parentSysMenu.getOnlineFormId() != null) {
                errorMessage = "数据验证失败，不能动态表单菜单添加父菜单！";
                return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
            }
        }
        CallResult result = sysMenuService.verifyRelatedData(sysMenu, null, permCodeIdListString);
        if (!result.isSuccess()) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, result.getErrorMessage());
        }
        Set<Long> permCodeIdSet = null;
        if (result.getData() != null) {
            permCodeIdSet = result.getData().getObject("permCodeIdSet", new TypeReference<Set<Long>>(){});
        }
        sysMenuService.saveNew(sysMenu, permCodeIdSet);
        return Result.succeed(sysMenu.getMenuId());
    }

    /**
     * 更新菜单数据操作。
     *
     * @param sysMenuDto           更新菜单对象。
     * @param permCodeIdListString 与当前菜单Id绑定的权限Id列表，多个权限之间逗号分隔。
     * @return 应答结果对象。
     */
    //@OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public Result<Void> update(
            @MyRequestBody SysMenuDto sysMenuDto, @MyRequestBody String permCodeIdListString) {
        String errorMessage = ValidateUtil.getModelValidationError(sysMenuDto, Default.class, UpdateGroup.class);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysMenu originalSysMenu = sysMenuService.getById(sysMenuDto.getMenuId());
        if (originalSysMenu == null) {
            errorMessage = "数据验证失败，当前菜单并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST , errorMessage);
            
        }
        SysMenu sysMenu = MyModelUtil.copyTo(sysMenuDto, SysMenu.class);
        if (ObjectUtils.notEqual(originalSysMenu.getOnlineFormId(), sysMenu.getOnlineFormId())) {
            if (originalSysMenu.getOnlineFormId() == null) {
                errorMessage = "数据验证失败，不能为当前菜单添加在线表单Id属性！";
                return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
            }
            if (sysMenu.getOnlineFormId() == null) {
                errorMessage = "数据验证失败，不能去掉当前菜单的在线表单Id属性！";
                return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
            }
        }
        if (originalSysMenu.getOnlineFormId() != null
                && originalSysMenu.getMenuType().equals(SysMenuType.TYPE_BUTTON)) {
            errorMessage = "数据验证失败，在线表单的内置菜单不能编辑！";
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        CallResult result = sysMenuService.verifyRelatedData(sysMenu, originalSysMenu, permCodeIdListString);
        if (!result.isSuccess()) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, result.getErrorMessage());
        }
        Set<Long> permCodeIdSet = null;
        if (result.getData() != null) {
            permCodeIdSet = result.getData().getObject("permCodeIdSet", new TypeReference<Set<Long>>(){});
        }
        if (!sysMenuService.update(sysMenu, originalSysMenu, permCodeIdSet)) {
            errorMessage = "数据验证失败，当前权限字并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        return Result.succeed();
    }

    /**
     * 删除指定菜单操作。
     *
     * @param menuId 指定菜单主键Id。
     * @return 应答结果对象。
     */
    //@OperationLog(type = SysOperationLogType.DELETE)
    @PostMapping("/delete")
    public Result<Void> delete(@MyRequestBody Long menuId) {
        if (ObjectUtil.isAnyBlankOrNull(menuId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK); 
        }
        String errorMessage;
        SysMenu menu = sysMenuService.getById(menuId);
        if (menu == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        if (menu.getOnlineFormId() != null && menu.getMenuType().equals(SysMenuType.TYPE_BUTTON)) {
            errorMessage = "数据验证失败，在线表单的内置菜单不能删除！";
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        // 对于在线表单，无需进行子菜单的验证，而是在删除的时候，连同子菜单一起删除。
        if (menu.getOnlineFormId() == null && sysMenuService.hasChildren(menuId)) {
            errorMessage = "数据验证失败，当前菜单存在下级菜单！";
            return Result.failed(ResultCode.HAS_CHILDREN_DATA, errorMessage);
        }
        if (!sysMenuService.remove(menu)) {
            errorMessage = "数据操作失败，菜单不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        return Result.succeed();
    }

    /**
     * 获取全部菜单列表。
     *
     * @return 应答结果对象，包含全部菜单数据列表。
     */
    @PostMapping("/list")
    public Result<List<SysMenuVo>> list() {
        Collection<SysMenu> sysMenuList = sysMenuService.getAllListByOrder("showOrder");
        return Result.succeed(MyModelUtil.copyCollectionTo(sysMenuList, SysMenuVo.class));
    }

    /**
     * 查看指定菜单数据详情。
     *
     * @param menuId 指定菜单主键Id。
     * @return 应答结果对象，包含菜单详情。
     */
    @GetMapping("/view")
    public Result<SysMenuVo> view(@RequestParam Long menuId) {
        if (ObjectUtil.isAnyBlankOrNull(menuId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        SysMenu sysMenu = sysMenuService.getByIdWithRelation(menuId, MyRelationParam.full());
        if (sysMenu == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        SysMenuVo sysMenuVo = MyModelUtil.copyTo(sysMenu, SysMenuVo.class);
        return Result.succeed(sysMenuVo);
    }

    /**
     * 查询菜单的权限资源地址列表。同时返回详细的分配路径。
     *
     * @param menuId 菜单Id。
     * @param url    权限资源地址过滤条件。
     * @return 应答对象，包含从菜单到权限资源的权限分配路径信息的查询结果列表。
     */
    @GetMapping("/listSysPermWithDetail")
    public Result<List<Map<String, Object>>> listSysPermWithDetail(Long menuId, String url) {
        if (ObjectUtil.isBlankOrNull(menuId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        return Result.succeed(sysMenuService.getSysPermListWithDetail(menuId, url));
    }

    /**
     * 查询菜单的用户列表。同时返回详细的分配路径。
     *
     * @param menuId    菜单Id。
     * @param loginName 登录名。
     * @return 应答对象，包含从菜单到用户的完整权限分配路径信息的查询结果列表。
     */
    @GetMapping("/listSysUserWithDetail")
    public Result<List<Map<String, Object>>> listSysUserWithDetail(Long menuId, String loginName) {
        if (ObjectUtil.isBlankOrNull(menuId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        return Result.succeed(sysMenuService.getSysUserListWithDetail(menuId, loginName));
    }
}
