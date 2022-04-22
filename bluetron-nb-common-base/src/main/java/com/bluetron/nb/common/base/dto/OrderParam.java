package com.bluetron.nb.common.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * @className: OrderParam
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/3/16 13:35
 */
public class OrderParam extends ArrayList<OrderParam.OrderInfo> {

 /**
  * 排序信息对象。
  */
 @AllArgsConstructor
 @NoArgsConstructor
 @Data
 public static class OrderInfo {

  private String fieldName;
  /**
   * 排序方向。默认为asc。
   */
  private String order = "asc";

 }
}


