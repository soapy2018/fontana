package com.bluetron.nb.common.util.tools;

import cn.hutool.extra.qrcode.QrConfig;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Description: 
 * @author cqf
 * @date 2021/10/15 16:50
 */
public class QrCodeUtil {

    private QrCodeUtil() {};


    /**
     * Description: 生成二维码BufferedImage
     * @param content
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage generate(String content, int width, int height){
        return cn.hutool.extra.qrcode.QrCodeUtil.generate(content, width, height);
    }

    /**
     * Description: 生成二维码文件到指定目录
     * @param content
     * @param width
     * @param height
     * @param targetFile
     */
    public static void generate(String content, int width, int height, File targetFile){
        cn.hutool.extra.qrcode.QrCodeUtil.generate(content, width, height, targetFile);
    }

    /**
     * Description: 通过QrConfig设定样式，生成二维码文件到指定目录
     * @param content
     * @param config
     * @param targetFile
     */
    public static void generate(String content, QrConfig config, File targetFile){
        cn.hutool.extra.qrcode.QrCodeUtil.generate(content, config, targetFile);
    }

    /**
     * Description: 通过QrConfig设定样式，生成二维码BufferedImage
     * @param content
     * @param config
     * @return
     */
    public static BufferedImage generate(String content, QrConfig config){
        return cn.hutool.extra.qrcode.QrCodeUtil.generate(content, config);
    }
}
