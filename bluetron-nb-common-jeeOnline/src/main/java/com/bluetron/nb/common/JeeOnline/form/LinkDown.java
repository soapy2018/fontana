package com.bluetron.nb.common.JeeOnline.form;

import lombok.Data;

/**
 * @className: LinkDown
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/15 15:09
 */
@Data
public class LinkDown {
    private String table;
    private String txt;
    private String key;
    private String linkField;
    private String idField;
    private String pidField;
    private String pidValue;
    private String condition;
}


