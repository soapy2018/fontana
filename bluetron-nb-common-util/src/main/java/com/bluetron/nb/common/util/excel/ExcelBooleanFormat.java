package com.bluetron.nb.common.util.excel;

/**
 * Created with IntelliJ IDEA.
 * Description: 
 * @author genx
 * @date 2021/6/24 14:13
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelBooleanFormat {

    String trueLabel();

    String falseLabel();
}
