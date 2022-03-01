package com.bluetron.nb.common.upmsservice.controller;

import com.alibaba.fastjson.TypeReference;
import com.bluetron.nb.common.base.annotation.MyRequestBody;
import com.bluetron.nb.common.base.result.Result;
import com.bluetron.nb.common.base.result.ResultCode;
import com.bluetron.nb.common.base.validate.UpdateGroup;
import com.bluetron.nb.common.util.validate.ValidateUtil;
import com.bluetron.nb.common.base.result.CallResult;
import com.bluetron.nb.common.db.object.MyRelationParam;
import com.bluetron.nb.common.db.util.MyModelUtil;
import com.bluetron.nb.common.upmsapi.dto.SysPermCodeDto;
import com.bluetron.nb.common.upmsapi.vo.SysPermCodeVo;
import com.bluetron.nb.common.upmsservice.entity.SysPermCode;
import com.bluetron.nb.common.upmsservice.service.SysPermCodeService;
import com.bluetron.nb.common.util.lang.ObjectUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 权限字管理接口控制器类。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Api(tags = "权限字管理接口")
@Slf4j
@RestController
@RequestMapping("/sysPermCode")
public class SysPermCodeController {

    @Autowired
    private SysPermCodeService sysPermCodeService;

    /**
     * 新增权限字操作。
     *
     * @param sysPermCodeDto   新增权限字对象。
     * @param permIdListString 与当前权限Id绑定的权限资源Id列表，多个权限资源之间逗号分隔。
     * @return 应答结果对象，包含新增权限字的主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {"sysPermCodeDto.permCodeId"})
    //@OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public Result<Long> add(
            @MyRequestBody SysPermCodeDto sysPermCodeDto, @MyRequestBody String permIdListString) {
        String errorMessage = ValidateUtil.getModelValidationError(sysPermCodeDto);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED);
        }
        SysPermCode sysPermCode = MyModelUtil.copyTo(sysPermCodeDto, SysPermCode.class);
        CallResult result = sysPermCodeService.verifyRelatedData(sysPermCode, null, permIdListString);
        if (!result.isSuccess()) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, result.getErrorMessage());
        }
        Set<Long> permIdSet = null;
        if (result.getData() != null) {
            permIdSet = result.getData().getObject("permIdSet", new TypeReference<Set<Long>>(){});
        }
        sysPermCode = sysPermCodeService.saveNew(sysPermCode, permIdSet);
        return Result.succeed(sysPermCode.getPermCodeId());
    }

    /**
     * 更新权限字操作。
     *
     * @param sysPermCodeDto   更新权限字对象。
     * @param permIdListString 与当前权限Id绑定的权限资源Id列表，多个权限资源之间逗号分隔。
     * @return 应答结果对象。
     */
    //@OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public Result<Void> update(
            @MyRequestBody SysPermCodeDto sysPermCodeDto, @MyRequestBody String permIdListString) {
        String errorMessage = ValidateUtil.getModelValidationError(sysPermCodeDto, Default.class, UpdateGroup.class);
        if (errorMessage != null) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysPermCode originalSysPermCode = sysPermCodeService.getById(sysPermCodeDto.getPermCodeId());
        if (originalSysPermCode == null) {
            errorMessage = "数据验证失败，当前权限字并不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        SysPermCode sysPermCode = MyModelUtil.copyTo(sysPermCodeDto, SysPermCode.class);
        CallResult result = sysPermCodeService.verifyRelatedData(sysPermCode, originalSysPermCode, permIdListString);
        if (!result.isSuccess()) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, result.getErrorMessage());
        }
        Set<Long> permIdSet = null;
        if (result.getData() != null) {
            permIdSet = result.getData().getObject("permIdSet", new TypeReference<Set<Long>>(){});
        }
        try {
            if (!sysPermCodeService.update(sysPermCode, originalSysPermCode, permIdSet)) {
                errorMessage = "数据验证失败，当前权限字并不存在，请刷新后重试！";
                return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
            }
        } catch (DuplicateKeyException e) {
            errorMessage = "数据操作失败，权限字编码已经存在！";
            return Result.failed(ResultCode.DATA_ALREADY_EXISTED, errorMessage);
        }
        return Result.succeed();
    }

    /**
     * 删除指定权限字操作。
     *
     * @param permCodeId 指定的权限字主键Id。
     * @return 应答结果对象。
     */
    //@OperationLog(type = SysOperationLogType.DELETE)
    @PostMapping("/delete")
    public Result<Void> delete(@MyRequestBody Long permCodeId) {
        if (ObjectUtil.isAnyBlankOrNull(permCodeId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        String errorMessage;
        if (sysPermCodeService.hasChildren(permCodeId)) {
            errorMessage = "数据验证失败，当前权限字存在下级权限字！";
            return Result.failed(ResultCode.HAS_CHILDREN_DATA, errorMessage);
        }
        if (!sysPermCodeService.remove(permCodeId)) {
            errorMessage = "数据操作失败，权限字不存在，请刷新后重试！";
            return Result.failed(ResultCode.DATA_NOT_EXIST, errorMessage);
        }
        return Result.succeed();
    }

    /**
     * 查看权限字列表。
     *
     * @return 应答结果对象，包含权限字列表。
     */
    @PostMapping("/list")
    public Result<List<SysPermCodeVo>> list() {
        List<SysPermCode> sysPermCodeList =
                sysPermCodeService.getAllListByOrder("permCodeType", "showOrder");
        return Result.succeed(MyModelUtil.copyCollectionTo(sysPermCodeList, SysPermCodeVo.class));
    }

    /**
     * 查看权限字对象详情。
     *
     * @param permCodeId 指定权限字主键Id。
     * @return 应答结果对象，包含权限字对象详情。
     */
    @GetMapping("/view")
    public Result<SysPermCodeVo> view(@RequestParam Long permCodeId) {
        if (ObjectUtil.isAnyBlankOrNull(permCodeId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        SysPermCode sysPermCode =
                sysPermCodeService.getByIdWithRelation(permCodeId, MyRelationParam.full());
        if (sysPermCode == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        SysPermCodeVo sysPermCodeVo = MyModelUtil.copyTo(sysPermCode, SysPermCodeVo.class);
        return Result.succeed(sysPermCodeVo);
    }

    /**
     * 查询权限字的用户列表。同时返回详细的分配路径。
     *
     * @param permCodeId 权限字Id。
     * @param loginName  登录名。
     * @return 应答对象。包含从权限字到用户的完整权限分配路径信息的查询结果列表。
     */
    @GetMapping("/listSysUserWithDetail")
    public Result<List<Map<String, Object>>> listSysUserWithDetail(Long permCodeId, String loginName) {
        if (ObjectUtil.isBlankOrNull(permCodeId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        return Result.succeed(sysPermCodeService.getSysUserListWithDetail(permCodeId, loginName));
    }

    /**
     * 查询权限字的角色列表。同时返回详细的分配路径。
     *
     * @param permCodeId 权限字Id。
     * @param roleName   角色名。
     * @return 应答对象。包含从权限字到角色的权限分配路径信息的查询结果列表。
     */
    @GetMapping("/listSysRoleWithDetail")
    public Result<List<Map<String, Object>>> listSysRoleWithDetail(Long permCodeId, String roleName) {
        if (ObjectUtil.isBlankOrNull(permCodeId)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        return Result.succeed(sysPermCodeService.getSysRoleListWithDetail(permCodeId, roleName));
    }
}
