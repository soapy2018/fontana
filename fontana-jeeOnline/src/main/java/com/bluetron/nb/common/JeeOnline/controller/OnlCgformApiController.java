package com.fontana.JeeOnline.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fontana.base.result.Result;
import com.fontana.db.util.JdbcUtil;
import com.fontana.JeeOnline.annotation.AutoLog;
import com.fontana.JeeOnline.annotation.OnlineAuth;
import com.fontana.JeeOnline.dto.OnlCgformDto;
import com.fontana.JeeOnline.dto.OnlComplexModel;
import com.fontana.JeeOnline.entity.OnlCgformHead;
import com.fontana.JeeOnline.enums.ModuleType;
import com.fontana.JeeOnline.service.IOnlCgformHeadService;
import com.fontana.JeeOnline.service.IOnlineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @className: OnlCgformApiController
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/12 10:42
 */
@RestController("onlCgformApiController")
@RequestMapping({"/online/cgform/api"})
@Slf4j
public class OnlCgformApiController {

    @Autowired
    private IOnlCgformHeadService onlCgformHeadService;

    @Autowired
    private IOnlineService onlineService;

    @GetMapping({"/checkOnlyTable"})
    public Result<?> checkOnlyTable(@RequestParam("tbname") String tableName, @RequestParam("id") String tableId) {
        OnlCgformHead onlCgformHead;
        if (ObjectUtil.isEmpty(tableId)) {
            if (JdbcUtil.isTableExist(tableName)) {
                return Result.failed("数据表"+tableName+"已存在");
            }

            onlCgformHead = this.onlCgformHeadService.getOne(new LambdaQueryWrapper<OnlCgformHead>().eq(OnlCgformHead::getTableName, tableName));
            if (ObjectUtil.isNotEmpty(onlCgformHead)) {
                return Result.failed("数据表"+tableName+"已存在");
            }
        } else {
            onlCgformHead = this.onlCgformHeadService.getById(tableId);
            if (!tableName.equals(onlCgformHead.getTableName()) && JdbcUtil.isTableExist(tableName)) {
                return Result.failed("数据表"+tableName+"已存在");
            }
        }

        return Result.succeed();
    }

    @PostMapping({"/addAll"})
    public Result<?> a(@RequestBody OnlCgformDto onlCgformDto) {
        try {
            String tableName = onlCgformDto.getHead().getTableName();
            return JdbcUtil.isTableExist(tableName) ? Result.failed("数据库表[" + tableName + "]已存在,请从数据库导入表单") : this.onlCgformHeadService.addAll(onlCgformDto);
        } catch (Exception exp) {
            log.error("OnlCgformApiController.addAll()发生异常：" + exp.getMessage(), exp);
            return Result.failed("操作失败");
        }
    }

    @AutoLog(
            operateType = 1,
            value = "online列表加载",
            module = ModuleType.ONLINE
    )
    @OnlineAuth("getColumns")
    @GetMapping({"/getColumns/{code}"})
    public Result<OnlComplexModel> a(@PathVariable("code") String code) {
        OnlCgformHead onlCgformHead = this.onlCgformHeadService.getById(code);
        if (onlCgformHead == null) {
            return Result.failed("实体不存在");
        } else {
            OnlComplexModel onlComplexModel = this.onlineService.queryOnlineConfig(onlCgformHead);
            onlComplexModel.setIsDesForm(onlCgformHead.getIsDesForm());
            onlComplexModel.setDesFormCode(onlCgformHead.getDesFormCode());
            return Result.succeed(onlComplexModel);
        }
    }

//    @AutoLog(
//            operateType = 1,
//            value = "online列表数据查询",
//            module = ModuleType.ONLINE
//    )
//    @PermissionData
//    @OnlineAuth("getData")
//    @GetMapping({"/getData/{code}"})
//    public Result<Map<String, Object>> a(@PathVariable("code") String code, HttpServletRequest httpServletRequest) {
//
//        OnlCgformHead onlCgformHead = this.onlCgformHeadService.getById(code);
//        if (onlCgformHead == null) {
//            return Result.failed("实体不存在");
//        } else {
//            Map var5 = null;
//
//            try {
//                Map parameterMap = OnlineServiceUtil.getParameterMap(httpServletRequest);
//                boolean  isMultiPage = OnlineServiceUtil.isMultiPage(onlCgformHead);
//                if (isMultiPage) {
//                    var5 = this.onlineJoinQueryService.pageList(onlCgformHead, parameterMap);
//                } else {
//                    var5 = this.onlCgformFieldService.queryAutolistPage(onlCgformHead, parameterMap, (List)null);
//                }
//
//                this.a(onlCgformHead, var5);
//                return Result.succeed(var5;
//            } catch (Exception exp) {
//                log.error(exp.getMessage(), exp);
//                return Result.failed("数据库查询失败，" + exp.getMessage();
//            }
//            return Result.failed();
//        }
//    }
}


