package com.fontana.demo.feign.controller;

import com.fontana.base.result.Result;
import com.fontana.base.result.ResultCode;
import com.fontana.base.upload.UploadStoreInfo;
import com.fontana.base.upload.UploadStoreTypeEnum;
import com.fontana.demo.feign.config.ApplicationConfig;
import com.fontana.redis.util.SessionCacheHelper;
import com.fontana.sb.updownload.BaseUpDownloader;
import com.fontana.sb.updownload.UpDownloaderFactory;
import com.fontana.base.upload.UploadResponseInfo;
import com.fontana.upmsapi.client.SysUserClient;
import com.fontana.upmsapi.vo.SysUserVo;
import com.fontana.util.request.WebContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * @className: OpenFeignController
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/5/13 14:44
 */
@RestController
@RequestMapping("/openfeign")
public class OpenFeignController {

    @Resource
    SysUserClient sysUserClient;

    @Autowired
    private UpDownloaderFactory upDownloaderFactory;

    @Autowired
    private ApplicationConfig appConfig;

    @Autowired
    private SessionCacheHelper cacheHelper;

//    @RequestParam 注解的作用:
//    标注在接口的方法参数上，被标注的参数的值来源于request.getParameter或request.getParameterValues。

//    localhost:8080/openfeign/sysUser/listByIds
//    Content-Type: application/x-www-form-urlencoded
//    body: userIds=1440911410581213417,1440965344985354240,1440965808049098752&withDict=false
    @GetMapping("/sysUser/listIds")
    Result<List<SysUserVo>> listByIds(
            @RequestParam("userIds") Set<Long> userIds,
            @RequestParam("withDict") Boolean withDict){
        return sysUserClient.listByIds(userIds, withDict);
    }

    @PostMapping("/sysUser/listByIds")
    Result<List<SysUserVo>> listIds(
            @RequestParam("userIds") Set<Long> userIds,
            @RequestParam("withDict") Boolean withDict){
        return sysUserClient.listByIds(userIds, withDict);
    }

    @PostMapping("/sysUser/listUsers")
    Result<List<SysUserVo>> listUsers(
            @RequestBody Set<Long> userIds){
        return sysUserClient.listUsers(userIds);
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
                appConfig.getUploadFileBaseDir(), OpenFeignController.class.getSimpleName(), fieldName, asImage, uploadFile);
        if (responseInfo.getUploadFailed()) {
            WebContextUtil.output(HttpServletResponse.SC_FORBIDDEN,
                    Result.failed(ResultCode.UPLOAD_FAILED, responseInfo.getErrorMessage()));
            return;
        }
        //cacheHelper.putSessionUploadFile(responseInfo.getFilename());
        WebContextUtil.output(Result.succeed(responseInfo));
    }

}


