package com.fontana.upmsservice.controller;

import com.fontana.base.annotation.MyRequestBody;
import com.fontana.base.result.Result;
import com.fontana.base.result.ResultCode;
import com.fontana.base.validate.UpdateGroup;
import com.fontana.util.validate.ValidateUtil;
import com.fontana.db.util.MyModelUtil;
import com.fontana.upmsapi.dto.SysPermModuleDto;
import com.fontana.upmsapi.vo.SysPermModuleVo;
import com.fontana.upmsservice.entity.SysPerm;
import com.fontana.upmsservice.entity.SysPermModule;
import com.fontana.upmsservice.service.SysPermModuleService;
import com.fontana.util.lang.ObjectUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 权限资源模块管理接口控制器类。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Api(tags = "权限资源模块管理接口")
@Slf4j
@RestController
@RequestMapping("/sysPermModule")
public class SysPermModuleController {

    @Autowired
    private SysPermModuleService sysPermModuleService;

    /**
     * 新增权限资源模块操作。
     *
     * @param sysPermModuleDto 新增权限资源模块对象。
     * @return 应答结果对象，包含新增权限资源模块的主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {"sysPermModuleDto.moduleId"})
    @PostMapping("/add")
    public Result<Long> add(@MyRequestBody SysPermModuleDto sysPermModuleDto) {
        String errorMessage = ValidateUtil.getModelValidationError(sysPermModuleDto);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysPermModule sysPermModule = MyModelUtil.copyTo(sysPermModuleDto, SysPermModule.class);
        if (sysPermModule.getParentId() != null
                && sysPermModuleService.getById(sysPermModule.getParentId()) == null) {
            errorMessage = "数据验证失败，关联的上级权限模块并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_PARENT_ID_NOT_EXIST, errorMessage);
        }
        sysPermModule = sysPermModuleService.saveNew(sysPermModule);
        return Result.succeed(sysPermModule.getModuleId());
    }

    /**
     * 更新权限资源模块操作。
     *
     * @param sysPermModuleDto 更新权限资源模块对象。
     * @return 应答结果对象，包含新增权限资源模块的主键Id。
     */
    @PostMapping("/update")
    public Result<Void> update(@MyRequestBody SysPermModuleDto sysPermModuleDto) {
        String errorMessage = ValidateUtil.getModelValidationError(sysPermModuleDto, Default.class, UpdateGroup.class);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysPermModule sysPermModule = MyModelUtil.copyTo(sysPermModuleDto, SysPermModule.class);
        SysPermModule originalPermModule = sysPermModuleService.getById(sysPermModule.getModuleId());
        if (originalPermModule == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        if (sysPermModule.getParentId() != null
                && !sysPermModule.getParentId().equals(originalPermModule.getParentId())) {
            if (sysPermModuleService.getById(sysPermModule.getParentId()) == null) {
                errorMessage = "数据验证失败，关联的上级权限模块并不存在，请刷新后重试！";
                return Result.failed(ResultCode.DATA_PARENT_ID_NOT_EXIST, errorMessage);
            }
        }
        if (!sysPermModuleService.update(sysPermModule, originalPermModule)) {
            errorMessage = "数据验证失败，当前模块并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        return Result.succeed();
    }

    /**
     * 删除指定权限资源模块操作。
     *
     * @param moduleId 指定的权限资源模块主键Id。
     * @return 应答结果对象。
     */
    @PostMapping("/delete")
    public Result<Void> delete(@MyRequestBody Long moduleId) {
        if (ObjectUtil.isAnyBlankOrNull(moduleId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        String errorMessage;
        if (sysPermModuleService.hasChildren(moduleId)
                || sysPermModuleService.hasModulePerms(moduleId)) {
            errorMessage = "数据验证失败，当前权限模块存在子模块或权限资源，请先删除关联数据！";
            return Result.failed(ResultCode.HAS_CHILDREN_DATA, errorMessage);
        }
        if (!sysPermModuleService.remove(moduleId)) {
            errorMessage = "数据操作失败，权限模块不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        return Result.succeed();
    }

    /**
     * 查看全部权限资源模块列表。
     *
     * @return 应答结果对象，包含权限资源模块列表。
     */
    @PostMapping("/list")
    public Result<List<SysPermModuleVo>> list() {
        List<SysPermModule> permModuleList = sysPermModuleService.getAllListByOrder("showOrder");
        return Result.succeed(MyModelUtil.copyCollectionTo(permModuleList, SysPermModuleVo.class));
    }

    /**
     * 列出全部权限资源模块及其下级关联的权限资源列表。
     *
     * @return 应答结果对象，包含树状列表，
     */
    @PostMapping("/listAll")
    public Result<List<Map<String, Object>>> listAll() {
        List<SysPermModule> sysPermModuleList = sysPermModuleService.getPermModuleAndPermList();
        List<Map<String, Object>> resultList = new LinkedList<>();
        for (SysPermModule sysPermModule : sysPermModuleList) {
            Map<String, Object> permModuleMap = new HashMap<>(5);
            permModuleMap.put("id", sysPermModule.getModuleId());
            permModuleMap.put("name", sysPermModule.getModuleName());
            permModuleMap.put("type", sysPermModule.getModuleType());
            permModuleMap.put("isPerm", false);
            if (!ObjectUtil.isBlankOrNull(sysPermModule.getParentId())) {
                permModuleMap.put("parentId", sysPermModule.getParentId());
            }
            resultList.add(permModuleMap);
            if (CollectionUtils.isNotEmpty(sysPermModule.getSysPermList())) {
                for (SysPerm sysPerm : sysPermModule.getSysPermList()) {
                    Map<String, Object> permMap = new HashMap<>(4);
                    permMap.put("id", sysPerm.getPermId());
                    permMap.put("name", sysPerm.getPermName());
                    permMap.put("isPerm", true);
                    permMap.put("url", sysPerm.getUrl());
                    permMap.put("parentId", sysPermModule.getModuleId());
                    resultList.add(permMap);
                }
            }
        }
        return Result.succeed(resultList);
    }
}
