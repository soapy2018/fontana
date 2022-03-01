package com.bluetron.nb.common.JeeOnline.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bluetron.nb.common.base.result.Result;
import com.bluetron.nb.common.db.query.QueryGenerator;
import com.bluetron.nb.common.JeeOnline.entity.OnlCgformHead;
import com.bluetron.nb.common.JeeOnline.service.IOnlCgformHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @className: OnlCgformHeadController
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/11 14:19
 */
@RestController("onlCgformHeadController")
@RequestMapping({"/online/cgform/head"})
public class OnlCgformHeadController {
 @Autowired
 private IOnlCgformHeadService onlCgformHeadService;

 @GetMapping({"/list"})
 public Result<IPage<OnlCgformHead>> list(OnlCgformHead object, @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize, HttpServletRequest request) {

  QueryWrapper queryWrapper = QueryGenerator.initQueryWrapper(object, request.getParameterMap());
  Page page = new Page((long)pageNo, (long)pageSize);
  IPage pageResult = this.onlCgformHeadService.page(page, queryWrapper);
  if (object.getCopyType() != null && object.getCopyType() == 0) {
   this.onlCgformHeadService.initCopyState(pageResult.getRecords());
  }
  return Result.succeed(pageResult);
 }

}


