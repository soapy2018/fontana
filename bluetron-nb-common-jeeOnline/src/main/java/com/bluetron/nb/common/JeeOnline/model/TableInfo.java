package com.bluetron.nb.common.JeeOnline.model;

import com.bluetron.nb.common.JeeOnline.entity.OnlCgformField;
import com.bluetron.nb.common.JeeOnline.entity.OnlCgformIndex;
import lombok.Data;

import java.util.List;

/**
 * @className: TableInfoDto
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/12 17:07
 */
@Data
public class TableInfo {
 private String tableName;
 private String isDbSynch;
 private String content;
 private String jformVersion;
 private Integer jformType;
 private String jformPkType;
 private String jformPkSequence;
 private Integer relationType;
 private String subTableStr;
 private Integer tabOrder;
 private List<OnlCgformField> columns;
 private List<OnlCgformIndex> indexes;
 private String treeParentIdFieldName;
 private String treeIdFieldName;
 private String treeFieldName;
 //private b dbConfig;
}


