package com.bluetron.nb.common.spring.converter;


import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Description: 
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
