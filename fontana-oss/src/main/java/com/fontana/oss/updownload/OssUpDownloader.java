package com.fontana.oss.updownload;

import com.fontana.base.constant.CommonConstants;
import com.fontana.base.constant.StringPool;
import com.fontana.base.result.ResultCode;
import com.fontana.oss.service.IOssService;
import com.fontana.util.updownload.BaseUpDownloader;
import com.fontana.util.updownload.UpDownloaderFactory;
import com.fontana.util.updownload.UploadResponseInfo;
import com.fontana.util.updownload.UploadStoreTypeEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 存储本地文件的上传下载实现类。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Slf4j
@ConditionalOnProperty(prefix = CommonConstants.OSS_PREFIX, name = "enabled", havingValue = "true")
public class OssUpDownloader extends BaseUpDownloader {

    @Autowired
    private UpDownloaderFactory factory;
    @Autowired
    private IOssService ossService;

    // 在服务启动和bean初始化时，将this自身对象，以及关联的存储类型注册到工厂对象中。
    @PostConstruct
    public void doRegister() {
        factory.registerUpDownloader(UploadStoreTypeEnum.OSS_SYSTEM, this);
        log.info("OssUpDownloader register success");
    }

    /**
     * 执行下载操作，从本地文件系统读取数据，并将读取的数据直接写入到HttpServletResponse应答对象。
     *
     * @param rootBaseDir 文件下载的根目录。
     * @param modelName   所在数据表的实体对象名。
     * @param fieldName   关联字段的实体对象属性名。
     * @param fileName    文件名。
     * @param asImage     是否为图片对象。图片是无需权限验证的，因此和附件存放在不同的子目录。
     * @param response    Http 应答对象。
     */
    @SneakyThrows
    @Override
    public void doDownload(
            String rootBaseDir,
            String modelName,
            String fieldName,
            String fileName,
            Boolean asImage,
            HttpServletResponse response) {
        String uploadPath = makeFullPath(null, modelName, fieldName, asImage);
        String fullFileName = uploadPath + StringPool.SLASH + fileName;
        ossService.download(fullFileName, response);
    }

    /**
     * 执行文件上传操作，并存入本地文件系统，再将与该文件下载对应的Url直接写入到HttpServletResponse应答对象，返回给前端。
     *
     * @param serviceContextPath 微服务的上下文路径，如: /admin/upms。
     * @param rootBaseDir        存放上传文件的根目录。
     * @param modelName          所在数据表的实体对象名。
     * @param fieldName          关联字段的实体对象属性名。
     * @param uploadFile         Http请求中上传的文件对象。
     * @param asImage            是否为图片对象。图片是无需权限验证的，因此和附件存放在不同的子目录。
     * @return 存储在本地上传文件名。
     * @throws IOException 文件操作错误。
     */
    @Override
    public UploadResponseInfo doUpload(
            String serviceContextPath,
            String rootBaseDir,
            String modelName,
            String fieldName,
            Boolean asImage,
            MultipartFile uploadFile) throws IOException {
        UploadResponseInfo responseInfo = new UploadResponseInfo();
        if (Objects.isNull(uploadFile) || uploadFile.isEmpty()) {
            responseInfo.setUploadFailed(true);
            responseInfo.setErrorMessage(ResultCode.INVALID_UPLOAD_FILE_ARGUMENT.getMessage());
            return responseInfo;
        }
        String uploadPath = makeFullPath(null, modelName, fieldName, asImage);
        fillUploadResponseInfo(responseInfo, serviceContextPath, uploadFile.getOriginalFilename());
        // 调用ossService方法，把当前要上传的文件，按照计算后的目录存储到oss中。
        ossService.upload(uploadPath + StringPool.SLASH + responseInfo.getFilename(), uploadFile.getInputStream());

        return responseInfo;
    }
}
