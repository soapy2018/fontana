package com.bluetron.nb.common.util.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel 导出时的日期格式
 * @see ExcelExportHelper
 * @author genx
 * @date 2021/5/8 8:57
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelDateFormat {
    String value();
}
