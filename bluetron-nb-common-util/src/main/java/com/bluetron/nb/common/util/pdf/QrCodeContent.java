package com.bluetron.nb.common.util.pdf;

import lombok.Data;

/**
 * 二维码内容
 *
 * @author xingyue.wang
 */
@Data
public class QrCodeContent {

    private String businessType;
    private String no;
    private String tenantId;


}
