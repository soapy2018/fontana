package com.fontana.util.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.Test;

import java.awt.*;

/**
 * Description:
 *
 * @author genx
 * @date 2021/4/15 16:50
 */
public class QrCodeUtilTest {

    @Test
    public void testGenerate1() {
        QrCodeUtil.generate("https://hutool.cn/", 300, 300, FileUtil.file("d:/qrcode1.jpg"));
    }

    @Test
    public void testGenerate2() {

    }

    @Test
    public void testGenerate3() {
        QrConfig config = new QrConfig(300, 300);
        // 设置边距，既二维码和背景之间的边距
        config.setMargin(3);
        // 设置前景色，既二维码颜色（青色）
        config.setForeColor(Color.CYAN);
        // 设置背景色（灰色）
        config.setBackColor(Color.GRAY);
        //设定纠错级别
        config.setErrorCorrection(ErrorCorrectionLevel.H);
        // 生成二维码到文件，也可以到流
        QrCodeUtil.generate("http://hutool.cn/", config, FileUtil.file("D:/qrcode3.jpg"));
    }
}
