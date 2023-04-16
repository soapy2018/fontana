package com.fontana.JeeOnline.model;

import lombok.Data;

/**
 * @className: ForeignKey
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/14 14:10
 */
@Data
public class ForeignKey {
    private String field;
    private String table;
    private String key;

    public ForeignKey(String field, String key) {
        this.key = key;
        this.field = field;
    }
}


