package com.bluetron.nb.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 
 * @author genx
 * @date 2021/4/15 16:50
 */
public class QrCodeUtil {
    private static String DEFAULT_CHARACTER = "UTF-8";

    public static BufferedImage createQRCode(String content, float width, float height, int margin) throws WriterException {
        if (content == null || content.equals("")) {
            throw new IllegalArgumentException("二维码内容不能为空");
        }
        // 二维码基本参数设置
        Map<EncodeHintType, Object> hints = new HashMap(8);
        // 设置编码字符集utf-8
        hints.put(EncodeHintType.CHARACTER_SET, DEFAULT_CHARACTER);
        // 设置纠错等级L/M/Q/H,当二维码被损毁一部分时，纠错等级越高，越可能读取成功；同样的，纠错等级越高，单位面积内点阵的点越多，机器扫描时，识别所需时间越长，当前设置等级为最高等级H
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 可设置范围为0-10，但仅四个变化0 1(2) 3(4 5 6) 7(8 9 10)
        hints.put(EncodeHintType.MARGIN, margin);
        // 生成图片类型为QRCode
        BarcodeFormat format = BarcodeFormat.QR_CODE;
        // 创建位矩阵对象 生成二维码对应的位矩阵对象
        BitMatrix matrix = new MultiFormatWriter().encode(content, format, (int) width, (int) height, hints);
        // 设置位矩阵转图片的参数
        MatrixToImageConfig config = new MatrixToImageConfig(Color.black.getRGB(), Color.white.getRGB());
        // 位矩阵对象转BufferedImage对象
        BufferedImage qrcode = MatrixToImageWriter.toBufferedImage(matrix, config);
        return qrcode;
    }

}
