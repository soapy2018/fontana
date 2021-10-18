package com.bluetron.nb.common.util.pdf;

import lombok.Data;

/**
 * 二维码生成申请
 *
 * @author xingyue.wang
 */
@Data
public class QrCode {

    private QrCodeContent qrcodeContent;
    private String[] textLines;
}
