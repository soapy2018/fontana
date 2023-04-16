package com.fontana.JeeOnline.model;

import lombok.Data;

/**
 * @className: OnlColumn
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/14 13:59
 */
@Data
public class OnlColumn {
    private String title;
    private String dataIndex;
    private String align;
    private String customRender;
    private String scopedSlots;
    private String hrefSlotName;
    private int showLength;
    private boolean sorter = false;
    private String linkField;
    private String tableName;

    public OnlColumn() {
    }

    public OnlColumn(String title, String dataIndex) {
        this.align = "center";
        this.title = title;
        this.dataIndex = dataIndex;
    }
}


