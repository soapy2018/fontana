package com.fontana.log.producer.producer.persistence;

/**
 * @author yegenchang
 * @description
 * @date 2022/6/14 10:21
 */
public enum EnumStoreType {
    SOLR("solr"),
    ES("elasticsearch");

    private String value;

    EnumStoreType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
