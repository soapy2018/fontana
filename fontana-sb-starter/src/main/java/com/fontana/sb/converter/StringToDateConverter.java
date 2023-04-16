package com.fontana.sb.converter;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

/**
 * 时间转换器 支持3种格式
 * yyyy-MM-dd HH:mm:ss
 * yyyy-MM-dd
 * 时间戳(毫秒级)
 *
 * @author genx
 * @date 2021/4/8 16:54
 */
@Component
public class StringToDateConverter implements Converter<String, Date> {

    private final String[] FORMATS = {
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd"
    };

    public StringToDateConverter(GenericConversionService conversionService) {
        if (conversionService != null) {
            conversionService.addConverter(this);
        }
    }

    @Override
    public Date convert(String s) {
        Date d = null;
        for (String format : FORMATS) {
            if (s.length() == format.length()) {
                try {
                    d = FastDateFormat.getInstance(format).parse(s);
                } catch (ParseException e) {

                }
                if (d != null) {
                    return d;
                }
            }
        }
        //时间戳
        long time = NumberUtils.toLong(s);
        if (time > 0) {
            return new Date(time);
        }

        throw new IllegalArgumentException("时间格式无法转换: " + s);
    }
}
