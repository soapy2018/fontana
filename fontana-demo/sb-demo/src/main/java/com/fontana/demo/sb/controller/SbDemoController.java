package com.fontana.demo.sb.controller;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import com.fontana.base.result.Result;
import com.fontana.base.result.ResultCode;
import com.fontana.demo.sb.config.ApplicationConfig;
import com.fontana.demo.sb.model.SysUserAddress;
import com.fontana.demo.sb.model.SysUserExcel;
import com.fontana.demo.sb.util.ExcelDiceHandlerImpl;
import com.fontana.redis.util.DictCacheHelper;
import com.fontana.util.excel.ExcelUtil;
import com.fontana.util.json.JsonUtil;
import com.fontana.util.request.WebContextUtil;
import com.fontana.util.updownload.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @className: SbDemoController
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/5/13 14:44
 */
@RestController
@RequestMapping("/sb")
@Api(tags = "sb测试接口")
@RefreshScope
public class SbDemoController {


    @Autowired
    private UpDownloaderFactory upDownloaderFactory;

    @Autowired
    private ApplicationConfig appConfig;

    @Autowired
    private DictCacheHelper dictCacheHelper;

    @Autowired
    private IExcelDictHandler excelDiceHandler;

//    @RequestParam 注解的作用:
//    标注在接口的方法参数上，被标注的参数的值来源于request.getParameter或request.getParameterValues。

//    localhost:8080/openfeign/sysUser/listByIds
//    Content-Type: application/x-www-form-urlencoded
//    body: userIds=1440911410581213417,1440965344985354240,1440965808049098752&withDict=false
    @ApiOperation("excel导出")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response) throws IOException {
        List<SysUserExcel> list;
        dictCacheHelper.getDictItems("statuss");
        excelDiceHandler.getList("statuss");
        String str = "[{\"username\":1001,\"nickname\":\"小明\",\"mobile\":\"13567890013\",\"sex\":0,\"tenantId\":\"default\",\"userStatus\":0," +
                "\"addresses\":[{\"username\":1001, \"address\":\"浙江宁波\"},{\"username\":1001, \"address\":\"浙江杭州\"}],\"createTime\":\"2021-09-23 17:06:09\",\"updateTime\":\"2021-09-25 17:06:09\"}," +
                "{\"username\":1002,\"nickname\":\"小李\",\"mobile\":\"13562390013\",\"sex\":1,\"tenantId\":\"default\",\"userStatus\":1," +
                "\"addresses\":[{\"username\":1002, \"address\":\"浙江宁波\"},{\"username\":1002, \"address\":\"浙江杭州\"}],\"createTime\":\"2021-09-23 17:07:09\",\"updateTime\":\"2021-09-23 18:06:09\"}," +
                "{\"username\":1003,\"nickname\":\"小美\",\"mobile\":\"13566990013\",\"sex\":1,\"tenantId\":\"default\",\"userStatus\":0," +
                "\"addresses\":[{\"username\":1003, \"address\":\"浙江宁波\"},{\"username\":1003, \"address\":\"浙江杭州\"}],\"createTime\":\"2021-06-23 17:06:09\",\"updateTime\":\"2021-07-23 17:06:09\"}]";

        list = JsonUtil.parseArray(str, SysUserExcel.class);
        list.forEach(System.out::println);
        ExcelUtil.exportExcel(list, "用户列表", "用户表", SysUserExcel.class, "测试导出.xls"
            , true, response, excelDiceHandler);
    }



    /**
     * 文件上传操作。
     *
     * @param fieldName  上传文件名。
     * @param asImage    是否作为图片上传。如果是图片，今后下载的时候无需权限验证。否则就是附件上传，下载时需要权限验证。
     * @param uploadFile 上传文件对象。
     */
    @PostMapping("/upload")
    public void upload(
            @RequestParam String fieldName,
            @RequestParam Boolean asImage,
            @RequestParam("uploadFile") MultipartFile uploadFile) throws Exception {
        UploadStoreInfo storeInfo = new UploadStoreInfo();
        storeInfo.setSupportUpload(true);
        storeInfo.setStoreType(UploadStoreTypeEnum.LOCAL_SYSTEM);
        // 这里就会判断参数中指定的字段，是否支持上传操作。
        if (!storeInfo.isSupportUpload()) {
            WebContextUtil.output(HttpServletResponse.SC_FORBIDDEN,
                    Result.failed(ResultCode.INVALID_UPLOAD_FIELD));
            return;
        }
        // 根据字段注解中的存储类型，通过工厂方法获取匹配的上传下载实现类，从而解耦。
        BaseUpDownloader upDownloader = upDownloaderFactory.get(storeInfo.getStoreType());
        UploadResponseInfo responseInfo = upDownloader.doUpload(appConfig.getServiceContextPath(),
                appConfig.getUploadFileBaseDir(), SbDemoController.class.getSimpleName(), fieldName, asImage, uploadFile);
        if (Boolean.TRUE.equals(responseInfo.getUploadFailed())) {
            WebContextUtil.output(HttpServletResponse.SC_FORBIDDEN,
                    Result.failed(ResultCode.UPLOAD_FAILED, responseInfo.getErrorMessage()));
            return;
        }
        //cacheHelper.putSessionUploadFile(responseInfo.getFilename());
        WebContextUtil.output(Result.succeed(responseInfo));
    }

    @Value("${fontana.application.name}") //引入配置
    private String name;

    @RequestMapping("/test1")
    public String test1(){
        return name;
    }

}


