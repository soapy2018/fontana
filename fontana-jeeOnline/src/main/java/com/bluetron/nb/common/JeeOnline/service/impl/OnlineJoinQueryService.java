//package com.fontana.online.service.impl;
//
//import com.alibaba.fastjson.JSONArray;
//import com.baomidou.mybatisplus.core.conditions.Wrapper;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.fontana.online.entity.OnlCgformHead;
//import com.fontana.online.model.OnlTableData;
//import com.fontana.online.service.IOnlineJoinQueryService;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
///**
// * @className: OnlineJoinQueryService
// * @Description: TODO
// * @version: v1.0.0
// * @author: cqf
// * @date: 2022/1/16 16:04
// */
//@Service("onlineJoinQueryService")
//public class OnlineJoinQueryService implements IOnlineJoinQueryService {
// public Map<String, Object> pageList(OnlCgformHead head, Map<String, Object> params, boolean ignoreSelectSubField) {
//  LoginUser var4 = (LoginUser)SecurityUtils.getSubject().getPrincipal();
//  List var5 = this.a(head, var4.getId());
//  JSONArray var6 = b.b(params);
//  MatchTypeEnum var7 = b.c(params);
//  StringBuilder var8 = new StringBuilder();
//  boolean var9 = true;
//  String var10 = "";
//  boolean var11 = false;
//  StringBuffer var12 = new StringBuffer();
//  StringBuffer var13 = new StringBuffer();
//  ArrayList var14 = new ArrayList();
//  ArrayList var15 = new ArrayList();
//  HashMap var16 = new HashMap();
//  HashMap var17 = new HashMap();
//  Object var18 = new ArrayList();
//  HashMap var19 = new HashMap();
//  Iterator var20 = var5.iterator();
//
//  while(true) {
//   c var21;
//   List var22;
//   String var23;
//   String var24;
//   String var25;
//   String var26;
//   org.jeecg.modules.online.config.c.b var29;
//   String var31;
//   boolean var33;
//   boolean var34;
//   boolean var35;
//   boolean var36;
//   boolean var37;
//   do {
//    if (!var20.hasNext()) {
//     String var42 = this.a(var14, var16, var17);
//     String var43 = this.a(var8);
//     String var44 = this.a((List)var15);
//     var23 = "SELECT " + var42 + var12.toString() + " where 1=1  " + var13.toString() + var43 + var44;
//     HashMap var45 = new HashMap();
//     Integer var46 = params.get("pageSize") == null ? 10 : Integer.parseInt(params.get("pageSize").toString());
//     if (var46 == -521) {
//      List var47 = this.onlineMapper.selectByCondition(var23, var19);
//      if (var47 != null && var47.size() != 0) {
//       var45.put("total", var47.size());
//       if (ignoreSelectSubField) {
//        var47 = this.b(var47);
//       }
//
//       var45.put("records", b.a(var47, var17.values()));
//      } else {
//       var45.put("total", 0);
//      }
//
//      if (ignoreSelectSubField) {
//       var45.put("fieldList", var18);
//      }
//     } else {
//      Integer var48 = params.get("pageNo") == null ? 1 : Integer.parseInt(params.get("pageNo").toString());
//      Page var49 = new Page((long)var48, (long)var46);
//      IPage var50 = this.onlineMapper.selectPageByCondition(var49, var23, var19);
//      var45.put("total", var50.getTotal());
//      List var51 = var50.getRecords();
//      if (ignoreSelectSubField) {
//       var51 = this.b(var51);
//      }
//
//      var45.put("records", b.a(var51, var17.values()));
//     }
//
//     return var45;
//    }
//
//    var21 = (c)var20.next();
//    var22 = var21.getSelectFieldList();
//    var23 = var21.getAlias();
//    var24 = var23 + ".";
//    var25 = " " + var23 + " ";
//    var26 = var21.getTableName();
//    List var27 = var21.getAllFieldList();
//    List var28 = var21.getAuthList();
//    if (!var11 && var28 != null && var28.size() > 0) {
//     JeecgDataAutorUtils.installUserInfo(this.sysBaseAPI.getCacheUser(var4.getUsername()));
//     var11 = true;
//    }
//
//    var29 = new org.jeecg.modules.online.config.c.b(var24);
//    var29.setNeedList((List)null);
//    var29.setFirst(false);
//    List var30 = b.g(var27);
//    var31 = var29.a(var30, params, var28, var26 + "@");
//    Map var32 = var29.getSqlParams();
//    var19.putAll(var32);
//    var33 = this.a(params, var21.a(), var26, var24, var27, var15);
//    var34 = this.a(var21);
//    var35 = this.a(var21, var6);
//    var36 = var31.length() > 0;
//    var37 = var34 || var35 || var36 || var33;
//   } while(!var37);
//
//   boolean var38 = !var34 && (var35 || var36);
//   if (var33) {
//    var38 = false;
//   }
//
//   if (ignoreSelectSubField && var21.a() || !ignoreSelectSubField && var34) {
//    this.a(var24, var21.a(), var22, var14, var16);
//   }
//
//   if (ignoreSelectSubField && var21.a()) {
//    var18 = var22;
//   }
//
//   String var39 = "";
//   org.jeecg.modules.online.config.c.b var40 = this.a(var21, var6, var7.getValue(), var29);
//   if (var40 != null) {
//    var39 = var40.getSql().toString();
//    if (var39.length() > 0) {
//     var19.putAll(var40.getSqlParams());
//    }
//   }
//
//   if (var21.a()) {
//    var12.append(" FROM " + b.f(var26) + var25);
//    var10 = var24;
//   } else {
//    var17.put(var23, var26);
//    if (var38) {
//     String var41 = this.a(var21, var10, var31, var39);
//     var13.append(var41);
//    } else {
//     var12.append(" LEFT JOIN ");
//     var12.append(b.f(var26));
//     var12.append(var25);
//     var12.append(" ON ");
//     var12.append(var24);
//     var12.append(var21.getJoinField());
//     var12.append("=");
//     var12.append(var10);
//     var12.append(var21.getMainField());
//    }
//   }
//
//   if (!var38) {
//    var13.append(var31);
//    if (var39.length() > 0) {
//     if (var9) {
//      var8.append(var39);
//      var9 = false;
//     } else {
//      var8.append(" ").append(var7.getValue()).append(" ").append(var39);
//     }
//    }
//   }
//  }
// }
//
// private List<OnlTableData> a(OnlCgformHead onlCgformHead, String userId) {
//  byte var3 = 97;
//  ArrayList var4 = new ArrayList();
//  int var17 = var3 + 1;
//  OnlTableData var5 = this.a(onlCgformHead, var3, true);
//  List var6 = this.onlAuthDataService.queryUserOnlineAuthData(userId, onlCgformHead.getId());
//  var5.setAuthList(var6);
//  var4.add(var5);
//  Integer var7 = onlCgformHead.getTableType();
//  if (var7 != null && var7 == 2) {
//   String var8 = onlCgformHead.getSubTableStr();
//   if (var8 != null && !"".equals(var8)) {
//    String[] var9 = var8.split(",");
//    String[] var10 = var9;
//    int var11 = var9.length;
//
//    for(int var12 = 0; var12 < var11; ++var12) {
//     String var13 = var10[var12];
//     OnlCgformHead var14 = (OnlCgformHead)this.onlCgformHeadService.getOne((Wrapper)(new LambdaQueryWrapper()).eq(OnlCgformHead::getTableName, var13));
//     if (var14 != null) {
//      c var15 = this.a(var14, var17++, false);
//      List var16 = this.onlAuthDataService.queryUserOnlineAuthData(userId, var14.getId());
//      var15.setAuthList(var16);
//      var4.add(var15);
//     }
//    }
//   }
//  }
//
//  return var4;
// }
//}
//
//
