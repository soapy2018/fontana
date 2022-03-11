package com.bluetron.nb.common.sb.converter;


import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Description: Get请求入参转换，声明为bean后自动加入，不需要WebMvcConfigurer显示配置
 *
 * @author genx
 * @date 2021/5/12 15:06
 */
@Component
public class DateToStringConverter implements Converter<Date, String> {

    public DateToStringConverter(GenericConversionService conversionService) {
        if (conversionService != null) {
            conversionService.addConverter(this);
        }
    }

    @Override
    public String convert(Date d) {
        if (d == null) {
            return null;
        }
        return FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(d);
    }
}
