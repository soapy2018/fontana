package com.fontana.JeeOnline.dto;

import com.fontana.JeeOnline.entity.OnlCgformField;
import com.fontana.JeeOnline.entity.OnlCgformHead;
import com.fontana.JeeOnline.entity.OnlCgformIndex;
import lombok.Data;

import java.util.List;

/**
 * @className: OnlCgformDto
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/12 14:38
 */
@Data
public class OnlCgformDto {
 private OnlCgformHead head;
 private List<OnlCgformField> fields;
 private List<String> deleteFieldIds;
 private List<OnlCgformIndex> indexs;
 private List<String> deleteIndexIds;
}


